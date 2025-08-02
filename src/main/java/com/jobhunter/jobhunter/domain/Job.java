package com.jobhunter.jobhunter.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jobhunter.jobhunter.util.SecurityUtil;
import com.jobhunter.jobhunter.util.constant.LevelEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String location;
    private double salary;
    private String quantity;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Instant startDay;
    private Instant endDay;

    private boolean active;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
    private Instant createdAt;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobs" })
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Resume> resumes;

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

    public void setDescription(String description) {
        this.description = description;
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

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
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

    public String getDescription() {
        return description;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Company getCompany() {
        return company;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public List<Resume> getResumes() {
        return resumes;
    }

    public Job() {
    }

    public Job(String name, String location, double salary, String quantity, LevelEnum level,
            String description, Instant startDay, Instant endDay, boolean active, Instant createdAt, Instant updatedAt,
            String createdBy, String updatedBy, Company company, List<Skill> skills, List<Resume> resumes) {
        this.name = name;
        this.location = location;
        this.salary = salary;
        this.quantity = quantity;
        this.level = level;
        this.description = description;
        this.startDay = startDay;
        this.endDay = endDay;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.company = company;
        this.skills = skills;
        this.resumes = resumes;
    }

    @PrePersist
    public void beforeSave() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void beforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }
}
