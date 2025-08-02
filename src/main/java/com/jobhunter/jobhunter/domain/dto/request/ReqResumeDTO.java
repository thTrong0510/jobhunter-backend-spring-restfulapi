package com.jobhunter.jobhunter.domain.dto.request;

import com.jobhunter.jobhunter.util.constant.ResumeStateEnum;

public class ReqResumeDTO {
    private long id;
    private ResumeStateEnum status;

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(ResumeStateEnum status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public ResumeStateEnum getStatus() {
        return status;
    }

    public ReqResumeDTO() {
    }

    public ReqResumeDTO(long id, ResumeStateEnum status) {
        this.id = id;
        this.status = status;
    }
}
