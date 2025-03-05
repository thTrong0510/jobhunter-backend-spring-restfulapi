package vn.hoidanit.jobhunter.domain.response.job;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateJobDTO {
    private long id;

    private String name;
    private String location;
    private String salary;
    private String quantity;
    private LevelEnum level;

    private Instant startDay;
    private Instant endDay;

    private boolean active;

    private Instant createdAt;

    private String createdBy;

    private List<String> skills;

}
