package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

@Setter
@Getter
public class ResFetchResumeDTO {
    private long id;
    private String email;
    private String url;
    private ResumeStateEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

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

    private String companyName;
    private User user;
    private Job job;

    @Setter
    @Getter
    public static class User {
        private long id;
        private String name;

        public User(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Setter
    @Getter
    public static class Job {
        private long id;
        private String name;

        public Job(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}
