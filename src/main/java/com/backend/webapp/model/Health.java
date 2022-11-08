package com.backend.webapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Health {

	@Id
	private String id;
	private String health;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getHealth() {
		return health;
	}
	
	public void setHealth(String health) {
		this.health = health;
	}
	
}
