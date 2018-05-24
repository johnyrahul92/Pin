package com.pinbyweb.PinByWebProxy.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class GenerateSecureToken {

	private static final Logger logger = LogManager.getLogger(GenerateSecureToken.class);

	@Autowired
	Environment env;

	private String generateIpAddress() throws UnknownHostException {

		return InetAddress.getLocalHost().getHostAddress();
	}

	private synchronized String generateCurrentTimeStamp() throws IOException {

		// String dateFormat = RESOURCES.getString("DateFormat").trim();
		SimpleDateFormat sdfDate = new SimpleDateFormat(env.getProperty("DateFormat"));
		Date now = new Date();
		String strDate = sdfDate.format(now);

		return strDate;
	}

	private String generateRandomNumber() {

		int range = Integer.parseInt(env.getProperty("Range"));
		int min = Integer.parseInt(env.getProperty("Min"));

		String randomNumber = Integer.toString((int) ((Math.random() * range) + min));
		String paddedrandomNumber = ("00000" + randomNumber).substring(randomNumber.length());

		return paddedrandomNumber;
	}

	public String generateSecureToken() throws IOException {

		String ipAddress = generateIpAddress();
		System.out.println("ipAddress-->" + ipAddress);
		ipAddress = ipAddress + generateCurrentTimeStamp();
		System.out.println("ipAddress+generateCurrentTimeStamp()" + ipAddress);
		ipAddress = ipAddress + generateRandomNumber();
		System.out.println("ipAddress+generateRandomNumber()" + ipAddress);
		logger.info("secure token generated");

		return ipAddress;
	}
}
