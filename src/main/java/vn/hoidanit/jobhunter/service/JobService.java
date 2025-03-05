package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillService skillService;

    public JobService(JobRepository jobRepository, SkillService skillService) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
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
                currentJob.getCreatedAt(), currentJob.getCreatedBy(), null);
        if (currentJob.getSkills() != null) {
            List<String> nameSkills = currentJob.getSkills().stream().map(skills -> skills.getName())
                    .collect(Collectors.toList());
            resCreateJobDTO.setSkills(nameSkills);
        }

        return resCreateJobDTO;
    }

    public ResCreateJobDTO updateJob(Job job) {

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
                currentJob.getCreatedAt(), currentJob.getCreatedBy(), null);
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
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

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
                        item.getSkills().stream().map(skills -> skills.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        result.setResult(resCreateJobDTOs);

        return result;

    }

    // item.getSkills().stream().map(skills -> skills.getName())
    public void deleteJobById(long id) {
        this.jobRepository.deleteById(id);
    }
}
