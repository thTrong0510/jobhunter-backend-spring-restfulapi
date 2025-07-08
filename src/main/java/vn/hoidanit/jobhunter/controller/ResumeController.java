package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.request.ReqResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    @Autowired
    private FilterBuilder filterBuilder;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, UserService userService) {
        this.resumeService = resumeService;
        this.userService = userService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resumeJson)
            throws IdInvalidException {
        if (!this.resumeService.checkExistedUser(resumeJson.getUser().getId())) {
            throw new IdInvalidException("User id: " + resumeJson.getUser().getId() + " is not Existed");
        }

        if (!this.resumeService.checkExistedJob(resumeJson.getJob().getId())) {
            throw new IdInvalidException("Job id: " + resumeJson.getJob().getId() + "is not Existed");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.createResume(resumeJson));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@Valid @RequestBody ReqResumeDTO resumeDTOJson)
            throws IdInvalidException {
        if (!this.resumeService.checkExistedResume(resumeDTOJson.getId())) {
            throw new IdInvalidException("Resume id: " + resumeDTOJson.getId() + " is not Existed");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.updateResume(resumeDTOJson));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id)
            throws IdInvalidException {
        if (!this.resumeService.checkExistedResume(id)) {
            throw new IdInvalidException("Resume id: " + id + " is not Existed");
        }

        this.resumeService.deleteResume(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("get a resume by id")
    public ResponseEntity<ResFetchResumeDTO> getResume(@PathVariable("id") long id)
            throws IdInvalidException {
        if (!this.resumeService.checkExistedResume(id)) {
            throw new IdInvalidException("Resume id: " + id + " is not Existed");
        }

        ResFetchResumeDTO resGetResumeDTO = this.resumeService.getResumeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resGetResumeDTO);
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes with pagination")
    public ResponseEntity<ResultPaginationDTO> getResumes(@Filter Specification<Resume> spec, Pageable pageable) {

        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUser = this.userService.fetchUserByEmail(email).isPresent()
                ? this.userService.fetchUserByEmail(email).get()
                : null;
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(job -> job.getId()).collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter
                .convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);

        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.fetchResumes(finalSpec,
                pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Fetch all resumes by user")
    public ResponseEntity<ResultPaginationDTO> getResumesByUser(Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchResumesByUser(pageable));
    }
}
