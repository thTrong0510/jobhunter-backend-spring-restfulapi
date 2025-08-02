package com.jobhunter.jobhunter.domain.dto.response.job;

import java.time.Instant;
import java.util.List;

import com.jobhunter.jobhunter.domain.Company;
import com.jobhunter.jobhunter.util.constant.LevelEnum;

public class ResCreateJobDTO {
    private long id;

    private String name;
    private String location;
    private double salary;
    private String quantity;
    private LevelEnum level;

    private Instant startDay;
    private Instant endDay;

    private boolean active;

    private Instant createdAt;

    private String createdBy;

    private List<String> skills;
    private Company company;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public void setStartDay(Instant startDay) {
        this.startDay = startDay;
    }

    public void setEndDay(Instant endDay) {
        this.endDay = endDay;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getSalary() {
        return salary;
    }

    public String getQuantity() {
        return quantity;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public Instant getStartDay() {
        return startDay;
    }

    public Instant getEndDay() {
        return endDay;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public List<String> getSkills() {
        return skills;
    }

    public Company getCompany() {
        return company;
    }

    public ResCreateJobDTO() {
    }

    public ResCreateJobDTO(long id, String name, String location, double salary, String quantity, LevelEnum level,
            Instant startDay, Instant endDay, boolean active, Instant createdAt, String createdBy, List<String> skills,
            Company company) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.salary = salary;
        this.quantity = quantity;
        this.level = level;
        this.startDay = startDay;
        this.endDay = endDay;
        this.active = active;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.skills = skills;
        this.company = company;
    }
}
