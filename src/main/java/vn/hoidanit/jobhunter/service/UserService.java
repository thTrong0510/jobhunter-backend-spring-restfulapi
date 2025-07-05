package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;

@Service
public interface UserService {

    public User saveUser(User user);

    public User updateUser(User userJson);

    public void deleteUserById(long id);

    public Optional<User> fetchUserById(long id);

    public ResultPaginationDTO fetchUsers(Specification<User> spec, Pageable pageable);

    public User fetchUserByEmail(String email);

    public Boolean checkExistedEmail(String email);

    public ResCreateUserDTO convertToResCreateUserDTO(User user);

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user);

    public ResUserDTO convertToResUserDTO(User user);

    public void updateRefreshToken(String refreshToken, String email);

    public User fetchUserByRefreshToken(String refreshToken, String email);
}
