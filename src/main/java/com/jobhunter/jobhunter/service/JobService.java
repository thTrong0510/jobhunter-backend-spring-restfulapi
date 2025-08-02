package com.jobhunter.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jobhunter.jobhunter.domain.Company;
import com.jobhunter.jobhunter.domain.Job;
import com.jobhunter.jobhunter.domain.dto.attach.Meta;
import com.jobhunter.jobhunter.domain.dto.response.job.ResCreateJobDTO;
import com.jobhunter.jobhunter.domain.dto.result.ResultPaginationDTO;
import com.jobhunter.jobhunter.repository.CompanyRepository;
import com.jobhunter.jobhunter.repository.JobRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillService skillService;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillService skillService, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
        this.companyRepository = companyRepository;
    }

    public Optional<Job> fetchJobByid(long id) {
        return this.jobRepository.findById(id);
    }

    public ResCreateJobDTO createJob(Job job) {

        // set exactly skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream().map(skill -> skill.getId()).collect(Collectors.toList());
            job.setSkills(this.skillService.fetchSkillsByListId(reqSkills));
        }

        // create job
        Job currentJob = this.jobRepository.save(job);

        // set DTO
        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO(currentJob.getId(), currentJob.getName(),
                currentJob.getLocation(),
                currentJob.getSalary(), currentJob.getQuantity(), currentJob.getLevel(), currentJob.getStartDay(),
                currentJob.getEndDay(), currentJob.isActive(),
                currentJob.getCreatedAt(), currentJob.getCreatedBy(), null, null);
        if (currentJob.getSkills() != null) {
            List<String> nameSkills = currentJob.getSkills().stream().map(skills -> skills.getName())
                    .collect(Collectors.toList());
            resCreateJobDTO.setSkills(nameSkills);
        }

        if (currentJob.getCompany() != null) {
            resCreateJobDTO.setCompany(currentJob.getCompany());
        }

        return resCreateJobDTO;
    }

    public ResCreateJobDTO updateJob(Job j, Job jobInDb) {

        // set exactly skills
        if (j.getSkills() != null) {
            List<Long> reqSkills = j.getSkills().stream().map(skill -> skill.getId()).collect(Collectors.toList());
            jobInDb.setSkills(this.skillService.fetchSkillsByListId(reqSkills));
        }

        // check company
        if (j.getCompany() != null) {
            Optional<Company> comOptional = this.companyRepository.findById(j.getCompany().getId());
            if (comOptional.isPresent()) {
                jobInDb.setCompany(comOptional.get());
            }
        }

        // update correct info
        jobInDb.setName(j.getName());
        jobInDb.setSalary(j.getSalary());
        jobInDb.setQuantity(j.getQuantity());
        jobInDb.setLocation(j.getLocation());
        jobInDb.setLevel(j.getLevel());
        jobInDb.setStartDay(j.getStartDay());
        jobInDb.setEndDay(j.getEndDay());
        jobInDb.setActive(j.isActive());

        // update job
        Job currentJob = this.jobRepository.save(jobInDb);

        // set DTO
        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO(currentJob.getId(), currentJob.getName(),
                currentJob.getLocation(),
                currentJob.getSalary(), currentJob.getQuantity(), currentJob.getLevel(), currentJob.getStartDay(),
                currentJob.getEndDay(), currentJob.isActive(),
                currentJob.getCreatedAt(), currentJob.getCreatedBy(), null, currentJob.getCompany());
        if (currentJob.getSkills() != null) {
            List<String> nameSkills = currentJob.getSkills().stream().map(skills -> skills.getName())
                    .collect(Collectors.toList());
            resCreateJobDTO.setSkills(nameSkills);
        }

        return resCreateJobDTO;
    }

    public ResultPaginationDTO fetchAllJobs(Specification<Job> specification, Pageable pageable) {
        Page<Job> jobPage = this.jobRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();

        // lấy từ pageable tức là lấy từ mấy params
        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        // này là lấy khi đã xún database rồi truy vấn lên lại
        meta.setPages(jobPage.getTotalPages());
        meta.setTotal(jobPage.getTotalElements());

        result.setMeta(meta);

        List<ResCreateJobDTO> resCreateJobDTOs = jobPage.getContent().stream().map(
                item -> new ResCreateJobDTO(item.getId(), item.getName(),
                        item.getLocation(),
                        item.getSalary(), item.getQuantity(), item.getLevel(), item.getStartDay(),
                        item.getEndDay(), item.isActive(),
                        item.getCreatedAt(), item.getCreatedBy(),
                        item.getSkills().stream().map(skills -> skills.getName()).collect(Collectors.toList()),
                        item.getCompany()))
                .collect(Collectors.toList());

        result.setResult(resCreateJobDTOs);

        return result;

    }

    // item.getSkills().stream().map(skills -> skills.getName())
    public void deleteJobById(long id) {
        this.jobRepository.deleteById(id);
    }
}
