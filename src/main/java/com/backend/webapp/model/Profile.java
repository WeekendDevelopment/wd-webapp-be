package com.backend.webapp.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document

public class Profile {

    private String gender;

    private String city;

    private String address;

    private String preferredGender;
    private List<MateDescription> mateQualities;

    public enum MateDescription {
        FRIENDLY,TALKATIVE,GAMER,EMPLOYEE,SHARE_WORK_EQUALLY,SLEEP_EARLY,NON_ALCHOLIC
    }

    public List<MateDescription> getMateQualities() {
        return mateQualities;
    }

    public void setMateQualities(List<MateDescription> mateQualities) {
        this.mateQualities = mateQualities;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
        return preferredGender;
    }

    public void setFindMateGender(String findMateGender) {
        this.preferredGender = findMateGender;
    }


}
