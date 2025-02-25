package vn.hoidanit.jobhunter.domain.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
