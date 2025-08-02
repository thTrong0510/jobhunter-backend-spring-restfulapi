package com.jobhunter.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jobhunter.jobhunter.domain.User;
import com.jobhunter.jobhunter.domain.dto.response.user.ResCreateUserDTO;
import com.jobhunter.jobhunter.domain.dto.response.user.ResUpdateUserDTO;
import com.jobhunter.jobhunter.domain.dto.response.user.ResUserDTO;
import com.jobhunter.jobhunter.domain.dto.result.ResultPaginationDTO;

@Service
public interface UserService {

    public User saveUser(User user);

    public User updateUser(User userJson);

    public void deleteUserById(long id);

    public Optional<User> fetchUserById(long id);

    public ResultPaginationDTO fetchUsers(Specification<User> spec, Pageable pageable);

    public Optional<User> fetchUserByEmail(String email);

    public Boolean checkExistedEmail(String email);

    public ResCreateUserDTO convertToResCreateUserDTO(User user);

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user);

    public ResUserDTO convertToResUserDTO(User user);

    public void updateRefreshToken(String refreshToken, String email);

    public User fetchUserByRefreshToken(String refreshToken, String email);
}
