package vn.hoidanit.jobhunter.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestLoginDTO {
    @JsonProperty("access_token")
    private String accessToken;
    private User user;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private long id;
        private String email;
        private String name;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGetAccount {
        private User user;
    }
}
