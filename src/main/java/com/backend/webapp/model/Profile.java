package com.backend.webapp.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document

public class Profile {

    private String Sex;

    private String city;

    private String address;

    private String findMateGender;

    private String mateDescription;

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFindMateGender() {
        return findMateGender;
    }

    public void setFindMateGender(String findMateGender) {
        this.findMateGender = findMateGender;
    }

    public String getMateDescription() {
        return mateDescription;
    }

    public void setMateDescription(String mateDescription) {
        this.mateDescription = mateDescription;
    }
}
