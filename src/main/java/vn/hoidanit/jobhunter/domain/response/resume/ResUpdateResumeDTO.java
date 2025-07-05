package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResUpdateResumeDTO {
    private String updatedBy;
    private Instant updatedAt;

    public ResUpdateResumeDTO(String updatedBy, Instant updatedAt) {
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
