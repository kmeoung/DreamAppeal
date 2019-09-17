package com.truevalue.dreamappeal.bean;

import java.util.List;

public abstract class BeanUser {

    private String birth;
    private List<AffiliationEntity> affiliation;
    private String email;
    private String location;
    private int gender;
    private String name;

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public List<AffiliationEntity> getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(List<AffiliationEntity> affiliation) {
        this.affiliation = affiliation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class AffiliationEntity {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
