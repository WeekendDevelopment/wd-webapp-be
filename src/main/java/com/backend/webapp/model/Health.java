package com.backend.webapp.model;

public class Health {
	
	private Status status;

	public enum Status {
		UP,
		DOWN
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
