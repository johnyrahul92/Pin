package com.pinbyweb.PinByWebProxy.util;

import com.sun.net.ssl.internal.ssl.Provider;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class PinNowWebChannel {

	private static final Logger logger = LogManager.getLogger(PinNowWebChannel.class);

	@Autowired
	Environment env;

	public String retrievePinNowWebSecuredUrl(String secureToken, String cardNumber, String accountNumber,
			String sortCode) throws  Exception {

		String issuerId = env.getProperty("issuerid");
		String pinByWebApiUrl = env.getProperty("PinByWebApiUrl");
		String sslPath="";
		String sslPassword="";

		String returnUrl = "";
		String token = "";
		PinWebAuthenticationReply cardAuthenticateUrl;
		PinNowValidationBO pinvalidationBo = new PinNowValidationBO();
		Gson gson = new Gson();
		Gson gsonConverter = new Gson();

		if (cardNumber != null) {
			token = secureToken + ":" + cardNumber;
			logger.info("secureToken+\":\"+cardNumber-->" + token);
		}

		try {
			logger.info("About to encrypt token");
			byte[] issuerIdentity = encrypt(token);
			logger.info(issuerIdentity.toString());

			logger.info("Finished encrypting token");

			logger.info("About to sign message");
			byte[] signedBytes = issuerSignMessage(token);
			logger.info("Finished signing message");

			String issuerToken = new String(issuerIdentity);

			pinvalidationBo.setSecureToken(issuerToken);
			pinvalidationBo.setSignedBytes(signedBytes);
			pinvalidationBo.setIssuerId(issuerId);

			String queryString = gsonConverter.toJson(pinvalidationBo);
			byte[] query = issuerIdentity;

			/*System.setProperty("javax.net.ssl.trustStore", sslPath);
			System.setProperty("javax.net.ssl.trustStorePassword", sslPassword);
			Security.addProvider(new Provider());
			char[] passphrase = sslPassword.toCharArray();
			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(new FileInputStream(this.keystorePath), passphrase);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keystore);
			SSLContext context = SSLContext.getInstance("TLS");
			TrustManager[] trustManagers = tmf.getTrustManagers();
			context.init((KeyManager[]) null, trustManagers, (SecureRandom) null);
			SSLSocketFactory sf = context.getSocketFactory();*/
			
			URL url = new URL(pinByWebApiUrl);

			HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();

			httpsCon.setRequestProperty("Content-length", String.valueOf(query.length));
			httpsCon.setRequestMethod("POST");
			httpsCon.setRequestProperty("Accept", "application/json");
			httpsCon.connect();

			DataOutputStream dataStreamOutput = null;
			dataStreamOutput = new DataOutputStream(httpsCon.getOutputStream());
			dataStreamOutput.writeBytes(queryString);

			System.out.println("queryString-->" + queryString);

			dataStreamOutput.flush();

			if (httpsCon.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code :" + httpsCon.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));

			String output;
			output = br.readLine();
			cardAuthenticateUrl = gson.fromJson(output, PinWebAuthenticationReply.class);
			String securedUrl = cardAuthenticateUrl.getSecuredUrl();

			if (securedUrl != null) {
				returnUrl = securedUrl;
			} else {
				returnUrl = cardAuthenticateUrl.getErrorCode();
			}
		} catch (Exception e) {

			logger.info(e.getMessage(), e);
			throw e;
		}
		logger.info("returnUrl-->" + returnUrl);
		return returnUrl;

	}

	private byte[] encrypt(String unencryptedString) throws InvalidKeyException, FileNotFoundException, IOException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException,
			BadPaddingException, KeyStoreException, CertificateException {

		byte[] encodedencryptedPlainBytes = null;

		String transformation = env.getProperty("Transformation").trim();

		PublicKey publicKey = getPublicKey();
		logger.info("Recieved Public key ");
		Cipher publicEncryptCipher = Cipher.getInstance(transformation);
		publicEncryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedPlainBytes = publicEncryptCipher.doFinal(unencryptedString.getBytes());

		Base64 base64 = new Base64();
		encodedencryptedPlainBytes = base64.encode(encryptedPlainBytes);

		return encodedencryptedPlainBytes;
	}

	private byte[] issuerSignMessage(String unsignedMsg)
			throws SignatureException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException,
			FileNotFoundException, IOException, UnrecoverableKeyException, KeyStoreException, CertificateException {

		String sign = env.getProperty("Signature").trim();
		;

		PrivateKey privkey = getPrivateKey();
		Signature signalg = Signature.getInstance(sign);
		signalg.initSign(privkey);
		signalg.update(unsignedMsg.getBytes());
		byte[] signature = signalg.sign();

		return signature;
	}

	private PrivateKey getPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, UnrecoverableKeyException {

		PrivateKey privateKey = null;
		FileInputStream privateKeyFile = null;
		try {
			String privateKeystorePath = env.getProperty("PrivateKeystorePath").trim();
			String privateKeystorePassword = env.getProperty("PrivateKeystorePassword").trim();
			String privateKeyPassword = env.getProperty("PrivateKeyPassword").trim();
			String privateKeyAlias = env.getProperty("PrivateKeyAlias").trim();

			privateKeyFile = new FileInputStream(privateKeystorePath);

			KeyStore privateKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());

			privateKeyStore.load(privateKeyFile, privateKeystorePassword.toCharArray());

			privateKey = (PrivateKey) privateKeyStore.getKey(privateKeyAlias, privateKeyPassword.toCharArray());
		} finally {
			if (privateKeyFile != null)
				privateKeyFile.close();
		}

		return privateKey;
	}

	private PublicKey getPublicKey()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		String publicKeystorePath = env.getProperty("PublicKeystorePath").trim();
		String publicKeyStorePassword = env.getProperty("PublicKeystorePassword").trim();
		String publicKeyAlias = env.getProperty("PublicKeyAlias").trim();

		PublicKey publicKey = null;
		FileInputStream publicKeyFile = null;
		try {
			logger.info("publicKeystorePath for encryption-->" + publicKeystorePath);
			publicKeyFile = new FileInputStream(publicKeystorePath);

			KeyStore publicKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			publicKeyStore.load(publicKeyFile, publicKeyStorePassword.toCharArray());
			Certificate publicCertificate = publicKeyStore.getCertificate(publicKeyAlias);
			publicKey = publicCertificate.getPublicKey();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (publicKeyFile != null) {
				publicKeyFile.close();
			}
		}

		return publicKey;
	}

}
