package com.pinbyweb.PinByWebProxy.util;

public class BaseResponse {
	
	private int statusCode;
	private String serverStatusCode;
	private String statusDesc;
	

	public BaseResponse() {
	}
	
	
	public BaseResponse(int statusCode, String serverStatusCode, String statusDesc) {
		super();
		this.statusCode = statusCode;
		this.serverStatusCode = serverStatusCode;
		this.statusDesc = statusDesc;
	}


	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public String getServerStatusCode() {
		return serverStatusCode;
	}


	public void setServerStatusCode(String serverStatusCode) {
		this.serverStatusCode = serverStatusCode;
	}


	public String getStatusDesc() {
		return statusDesc;
	}


	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}




}