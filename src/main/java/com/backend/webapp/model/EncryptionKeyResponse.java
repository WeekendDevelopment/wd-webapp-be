package com.backend.webapp.model;

public class EncryptionKeyResponse extends BaseResponse {

	private String encryptionKey;

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	public EncryptionKeyResponse encryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
		return this;
	}
	
}
