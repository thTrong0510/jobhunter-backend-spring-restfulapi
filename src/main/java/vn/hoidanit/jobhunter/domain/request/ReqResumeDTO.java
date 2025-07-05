package vn.hoidanit.jobhunter.domain.request;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

@Setter
@Getter
public class ReqResumeDTO {
    private long id;
    private ResumeStateEnum status;

    public ReqResumeDTO(long id, ResumeStateEnum status) {
        this.id = id;
        this.status = status;
    }
}
