package com.jobhunter.jobhunter.domain.dto.response.user;

import java.time.Instant;

import com.jobhunter.jobhunter.domain.dto.attach.Company_;
import com.jobhunter.jobhunter.domain.dto.attach.Role_;
import com.jobhunter.jobhunter.util.constant.GenderEnum;

public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private Company_ company;
    private Role_ role;

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

    public void setCompany(Company_ company) {
        this.company = company;
    }

    public void setRole(Role_ role) {
        this.role = role;
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

    public Company_ getCompany() {
        return company;
    }

    public Role_ getRole() {
        return role;
    }

    public ResUpdateUserDTO(long id, String name, String email, int age, GenderEnum gender, String address,
            Instant createdAt, Company_ company, Role_ role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.createdAt = createdAt;
        this.company = company;
        this.role = role;
    }

    public ResUpdateUserDTO() {
    }
}
