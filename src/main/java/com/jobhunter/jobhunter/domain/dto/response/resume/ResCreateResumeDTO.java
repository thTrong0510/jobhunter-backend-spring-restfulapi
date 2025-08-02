package com.jobhunter.jobhunter.domain.dto.response.resume;

import java.time.Instant;

public class ResCreateResumeDTO {
    private long id;
    private String createdBy;
    private Instant createdAt;

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public ResCreateResumeDTO(long id, String createdBy, Instant createdAt) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public ResCreateResumeDTO() {
    }
}
