package com.jobhunter.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jobhunter.jobhunter.domain.Company;
import com.jobhunter.jobhunter.domain.Role;
import com.jobhunter.jobhunter.domain.User;
import com.jobhunter.jobhunter.domain.dto.attach.Company_;
import com.jobhunter.jobhunter.domain.dto.attach.Meta;
import com.jobhunter.jobhunter.domain.dto.attach.Role_;
import com.jobhunter.jobhunter.domain.dto.response.user.ResCreateUserDTO;
import com.jobhunter.jobhunter.domain.dto.response.user.ResUpdateUserDTO;
import com.jobhunter.jobhunter.domain.dto.response.user.ResUserDTO;
import com.jobhunter.jobhunter.domain.dto.result.ResultPaginationDTO;
import com.jobhunter.jobhunter.repository.CompanyRepository;
import com.jobhunter.jobhunter.repository.RoleRepository;
import com.jobhunter.jobhunter.repository.UserRepository;
import com.jobhunter.jobhunter.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
    }

    public User saveUser(User user) {
        if (user.getCompany() != null) {
            Optional<Company> comOptional = this.companyRepository.findById(user.getCompany().getId());
            user.setCompany(comOptional.isPresent() ? comOptional.get() : null);
        }

        if (user.getRole() != null) {
            Optional<Role> roleOptional = this.roleRepository.findById(user.getRole().getId());
            user.setRole(roleOptional.isPresent() ? roleOptional.get() : null);
        }

        return this.userRepository.save(user);
    }

    public User updateUser(User userJson) {
        User user = this.fetchUserById(userJson.getId()).get();
        if (user != null) {
            user.setEmail(userJson.getEmail());
            user.setName(userJson.getName());
            user.setPassword(userJson.getPassword());
            user.setAddress(userJson.getAddress());
            user.setAge(userJson.getAge());
            user.setGender(userJson.getGender());
            if (userJson.getCompany() != null) {
                Optional<Company> companyOptional = this.companyRepository.findById(userJson.getCompany().getId());
                user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            }
            if (userJson.getRole() != null) {
                Optional<Role> roleOptional = this.roleRepository.findById(user.getRole().getId());
                user.setRole(roleOptional.isPresent() ? roleOptional.get() : null);
            }

            user = this.saveUser(user);
        }
        return saveUser(user);
    }

    public void deleteUserById(long id) {
        if (this.userRepository.findById(id) != null) {
            this.userRepository.deleteById(id);
        }
    }

    public Optional<User> fetchUserById(long id) {
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
                .map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setResult(listResUserDTOs);
        result.setMeta(meta);

        return result;
    }

    public Optional<User> fetchUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public Boolean checkExistedEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        Company_ company = new Company_();
        if (user.getCompany() != null) {
            company.setId(user.getCompany().getId());
            company.setName(user.getCompany().getName());
        }

        Role_ role = new Role_();
        if (user.getRole() != null) {
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
        }

        return new ResCreateUserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getGender(),
                user.getAddress(), user.getCreatedAt(), company, role);
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        Company_ company = new Company_();
        if (user.getCompany() != null) {
            company.setId(user.getCompany().getId());
            company.setName(user.getCompany().getName());
        }

        Role_ role = new Role_();
        if (user.getRole() != null) {
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
        }

        return new ResUpdateUserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getGender(),
                user.getAddress(), user.getUpdatedAt(), company, role);
    }

    public ResUserDTO convertToResUserDTO(User user) {
        Company_ company = new Company_();
        if (user.getCompany() != null) {
            company.setId(user.getCompany().getId());
            company.setName(user.getCompany().getName());
        }

        Role_ role = new Role_();
        if (user.getRole() != null) {
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
        }
        return new ResUserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getGender(),
                user.getAddress(), user.getCreatedAt(), user.getUpdatedAt(), company, role);
    }

    public void updateRefreshToken(String refreshToken, String email) {
        User user = this.userRepository.findByEmail(email).isPresent() ? this.userRepository.findByEmail(email).get()
                : null;
        if (user != null) {
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        }
    }

    public User fetchUserByRefreshToken(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }
}
