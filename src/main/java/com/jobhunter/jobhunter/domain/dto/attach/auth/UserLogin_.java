package com.jobhunter.jobhunter.domain.dto.attach.auth;

import com.jobhunter.jobhunter.domain.Role;

public class UserLogin_ extends UserInsideToken_ {
    private Role role;

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public UserLogin_(long id, String name, String email, Role role) {
        super(id, name, email);
        this.role = role;
    }

    public UserLogin_() {
    }

}
