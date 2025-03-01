package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    public void deleteUserById(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        return this.userRepository.findById(id);
    }

    public ResultPaginationDTO fetchUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        List<ResUserDTO> listResUserDTOs = pageUser.getContent().stream()
                .map(item -> new ResUserDTO(item.getId(), item.getName(), item.getEmail(), item.getAge(),
                        item.getGender(), item.getAddress(), item.getCreatedAt(), item.getUpdatedAt()))
                .collect(Collectors.toList());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setResult(listResUserDTOs);
        result.setMeta(meta);

        return result;
    }

    public User fetchUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Boolean checkExistedEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        return new ResCreateUserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getGender(),
                user.getAddress(), user.getCreatedAt());
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        return new ResUpdateUserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getGender(),
                user.getAddress(), user.getUpdatedAt());
    }

    public ResUserDTO convertToResUserDTO(User user) {
        return new ResUserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getGender(),
                user.getAddress(), user.getCreatedAt(), user.getUpdatedAt());
    }

    public void updateRefreshToken(String refreshToken, String email) {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        }
    }
}
