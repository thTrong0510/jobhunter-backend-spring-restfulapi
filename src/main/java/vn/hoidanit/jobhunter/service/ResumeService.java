package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.request.ReqResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public ResCreateResumeDTO createResume(Resume resume) {
        Resume newResume = this.resumeRepository.save(resume);
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO(newResume.getId(), newResume.getCreatedBy(),
                newResume.getCreatedAt());
        return resCreateResumeDTO;
    }

    public ResUpdateResumeDTO updateResume(ReqResumeDTO reqResumeDTO) {
        Resume resume = this.resumeRepository.findById(reqResumeDTO.getId());
        resume.setStatus(reqResumeDTO.getStatus());
        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO resUpdateResumeDTO = new ResUpdateResumeDTO(resume.getUpdatedBy(), resume.getUpdatedAt());
        return resUpdateResumeDTO;
    }

    public void deleteResume(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResumeById(long id) {
        Resume resume = this.resumeRepository.findById(id);

        ResFetchResumeDTO.User user = new ResFetchResumeDTO.User(resume.getUser().getId(), resume.getUser().getName());
        ResFetchResumeDTO.Job job = new ResFetchResumeDTO.Job(resume.getJob().getId(), resume.getJob().getName());

        ResFetchResumeDTO resGetResumeDTO = new ResFetchResumeDTO(resume.getId(), resume.getEmail(), resume.getUrl(),
                resume.getStatus(), resume.getCreatedAt(), resume.getUpdatedAt(), resume.getCreatedBy(),
                resume.getUpdatedBy());

        if (resume.getJob() != null) {
            resGetResumeDTO.setCompanyName(resume.getJob().getCompany().getName());
        }
        resGetResumeDTO.setUser(user);
        resGetResumeDTO.setJob(job);

        return resGetResumeDTO;
    }

    public ResultPaginationDTO fetchResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        List<ResFetchResumeDTO> listResGetResumeDTO = pageResume.getContent().stream()
                .map(item -> this.getResumeById(item.getId()))
                .collect(Collectors.toList());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setResult(listResGetResumeDTO);
        result.setMeta(meta);

        return result;
    }

    public ResultPaginationDTO fetchResumesByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setResult(pageResume.getContent());
        result.setMeta(meta);

        return result;
    }

    public Boolean checkResumeExistByUserAndJob(Resume resume) {
        if (resume.getUser() == null)
            return false;
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty())
            return false;

        if (resume.getJob() == null)
            return false;

        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty())
            return false;

        return true;

    }

    public Boolean checkExistedUser(long id) {
        return this.userRepository.existsById(id);
    }

    public Boolean checkExistedJob(long id) {
        return this.jobRepository.existsById(id);
    }

    public Boolean checkExistedResume(long id) {
        return this.resumeRepository.existsById(id);
    }
}
