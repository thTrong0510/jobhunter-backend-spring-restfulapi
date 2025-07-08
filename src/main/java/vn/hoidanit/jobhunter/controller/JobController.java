package vn.hoidanit.jobhunter.controller;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.exception.IdInvalidException;

@Controller
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a Job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(this.jobService.createJob(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a Job")
    public ResponseEntity<ResCreateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        if (this.jobService.fetchJobByid(job.getId()).isPresent()) {
            throw new IdInvalidException("id " + job.getId() + "not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(this.jobService.updateJob(job, this.jobService.fetchJobByid(job.getId()).get()));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
        if (this.jobService.fetchJobByid(id).isPresent()) {
            throw new IdInvalidException("id " + id + "not found");
        }
        this.jobService.deleteJobById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all Jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.fetchAllJobs(specification, pageable));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get all Jobs")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
        if (this.jobService.fetchJobByid(id).isEmpty()) {
            throw new IdInvalidException("Job with id: " + id + " is not exist");
        }
        return ResponseEntity.ok().body(this.jobService.fetchJobByid(id).get());
    }
}