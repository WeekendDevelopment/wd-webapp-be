package com.backend.webapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document
@JsonInclude(Include.NON_NULL)
public class Users extends Profile {

    @Id
    private String id;

    @JsonIgnore()
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users email(String email) {
        this.setEmail(email);
        return this;
    }

}
