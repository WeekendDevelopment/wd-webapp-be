package com.backend.webapp.model;

public class HealthStatus {
	
	private Status serviceStatus;
	private Status databaseStatus;

	public enum Status {
		UP,
		DOWN
	}

	public Status getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(Status serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public Status getDatabaseStatus() {
		return databaseStatus;
	}

	public void setDatabaseStatus(Status databaseStatus) {
		this.databaseStatus = databaseStatus;
	}
	
	
}
