package com.jobhunter.jobhunter.domain.dto.response.file;

import java.time.Instant;

public class ResUploadFileDTO {
    private String fileName;
    private Instant uploadedAt;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public ResUploadFileDTO() {
    }

    public ResUploadFileDTO(String fileName, Instant uploadedAt) {
        this.fileName = fileName;
        this.uploadedAt = uploadedAt;
    }
}
