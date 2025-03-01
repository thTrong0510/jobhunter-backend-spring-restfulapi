package vn.hoidanit.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestLoginDTO {
    private String accessToken;
    private UserDTO userDTO;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        private long id;
        private String email;
        private String name;
    }
}
