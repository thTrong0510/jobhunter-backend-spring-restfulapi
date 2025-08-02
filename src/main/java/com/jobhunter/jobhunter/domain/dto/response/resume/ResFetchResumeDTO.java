package com.jobhunter.jobhunter.domain.dto.response.resume;

import java.time.Instant;

import com.jobhunter.jobhunter.domain.dto.attach.Job_;
import com.jobhunter.jobhunter.domain.dto.attach.User_;
import com.jobhunter.jobhunter.util.constant.ResumeStateEnum;

public class ResFetchResumeDTO {
    private long id;
    private String email;
    private String url;
    private ResumeStateEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private String companyName;
    private User_ user;
    private Job_ job;

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(ResumeStateEnum status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

    public void setJob(Job_ job) {
        this.job = job;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }

    public ResumeStateEnum getStatus() {
        return status;
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

    public String getCompanyName() {
        return companyName;
    }

    public User_ getUser() {
        return user;
    }

    public Job_ getJob() {
        return job;
    }

    public ResFetchResumeDTO(long id, String email, String url, ResumeStateEnum status, Instant createdAt,
            Instant updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.email = email;
        this.url = url;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public ResFetchResumeDTO() {
    }

}
