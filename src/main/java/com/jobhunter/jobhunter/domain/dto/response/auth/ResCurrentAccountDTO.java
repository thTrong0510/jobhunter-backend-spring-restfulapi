package com.jobhunter.jobhunter.domain.dto.response.auth;

import com.jobhunter.jobhunter.domain.dto.attach.auth.UserLogin_;

public class ResCurrentAccountDTO {
    private UserLogin_ user;

    public void setUser(UserLogin_ user) {
        this.user = user;
    }

    public UserLogin_ getUser() {
        return user;
    }

    public ResCurrentAccountDTO(UserLogin_ user) {
        this.user = user;
    }

    public ResCurrentAccountDTO() {
    };
}
