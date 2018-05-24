package com.pinbyweb.PinByWebProxy.Exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.pinbyweb.PinByWebProxy.util.BaseResponse;





@ControllerAdvice
public class PinByWebExceptionHandler {
	
	private static final Logger logger = LogManager.getLogger(PinByWebExceptionHandler.class);
	
	
	@ExceptionHandler(PinByWebException.class)
	public ResponseEntity<BaseResponse> handleCOBValidationException(PinByWebException ex) {
		logger.info("Inside PinNowwebException Handler");
		
		return new ResponseEntity<BaseResponse>(new BaseResponse(0, "hkuhds", "sdasd"), HttpStatus.BAD_GATEWAY);
	}

}
