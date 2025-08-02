package com.jobhunter.jobhunter.domain.dto.attach.auth;

import com.jobhunter.jobhunter.domain.dto.attach.General_;

public class UserInsideToken_ extends General_ {
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public UserInsideToken_(long id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    public UserInsideToken_() {
    }
}
