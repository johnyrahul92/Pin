package com.pinbyweb.PinByWebProxy.Controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pinbyweb.PinByWebProxy.Exception.PinByWebException;
import com.pinbyweb.PinByWebProxy.util.GenerateSecureToken;
import com.pinbyweb.PinByWebProxy.util.PinNowWebChannel;
import com.pinbyweb.PinByWebProxy.util.PinWebAuthenticationReply;



@RestController
@RequestMapping("/pinbyweb")
public class PinByWebController {	
	private static final Logger logger = LogManager.getLogger(PinByWebController.class);
	
	@Autowired
	GenerateSecureToken generateSecureToken;	
	
	@Autowired
	PinNowWebChannel pinNowWebChannel;
	
	@Autowired
	Environment env;
	
	@PostMapping(value="/generateSecureUrl")
	@ResponseBody PinWebAuthenticationReply getScecureUrl() throws InvalidKeyException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, KeyStoreException, CertificateException, SignatureException, Exception{
		
		System.out.println(env.getProperty("rahul"));
		
		PinWebAuthenticationReply pinWebAuthenticationReply = new PinWebAuthenticationReply();
		
		String secureToken=generateSecureToken.generateSecureToken();
		
		logger.info("Generated Secure Token : "+ secureToken);
		
		 String secureUrl =pinNowWebChannel.retrievePinNowWebSecuredUrl(secureToken, "5234378787887878", null, null);
		
		logger.info("Secure URL : " + secureUrl );
		
		
		return pinWebAuthenticationReply;
	}

}
