package com.jobhunter.jobhunter.domain.dto.response.resume;

import java.time.Instant;

public class ResUpdateResumeDTO {
    private String updatedBy;
    private Instant updatedAt;

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public ResUpdateResumeDTO(String updatedBy, Instant updatedAt) {
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
