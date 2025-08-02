package com.jobhunter.jobhunter.domain.dto.response.user;

import java.time.Instant;

import com.jobhunter.jobhunter.domain.dto.attach.Company_;
import com.jobhunter.jobhunter.domain.dto.attach.Role_;
import com.jobhunter.jobhunter.util.constant.GenderEnum;

public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    Instant createdAt;
    Instant updatedAt;

    private Company_ company;
    private Role_ roleUser;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCompany(Company_ company) {
        this.company = company;
    }

    public void setRoleUser(Role_ roleUser) {
        this.roleUser = roleUser;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Company_ getCompany() {
        return company;
    }

    public Role_ getRoleUser() {
        return roleUser;
    }

    public ResUserDTO(long id, String name, String email, int age, GenderEnum gender, String address, Instant createdAt,
            Instant updatedAt, Company_ company, Role_ roleUser) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.company = company;
        this.roleUser = roleUser;
    }

    public ResUserDTO() {
    }
}
