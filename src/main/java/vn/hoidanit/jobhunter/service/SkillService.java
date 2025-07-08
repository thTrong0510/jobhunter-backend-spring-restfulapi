package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import org.springframework.data.domain.Pageable;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean checkExistedNameSkill(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill saveSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Optional<Skill> fetchSkillById(long id) {
        return this.skillRepository.findById(id);
    }

    public List<Skill> fetchSkillsByListId(List<Long> ids) {
        return this.skillRepository.findByIdIn(ids);
    }

    public Skill updateSkill(Skill skillJson) {
        Skill skill = this.skillRepository.findById(skillJson.getId()).get();
        skill.setName(skillJson.getName());
        return this.saveSkill(skill);
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> skillPage = this.skillRepository.findAll(specification, pageable);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(skillPage.getTotalPages());
        meta.setTotal(skillPage.getTotalElements());

        // List<Skill> listSkills = skillPage.getContent().stream()
        // .map(item -> new Skill(item.getId(), item.getName(), item.getCreatedAt(),
        // item.getUpdatedAt(),
        // item.getCreatedBy(), item.getUpdatedBy()))
        // .collect(Collectors.toList());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setResult(skillPage.getContent());
        result.setMeta(meta);

        return result;
    }

    public void deleteSkillByid(long id) {

        Skill currentSkill = this.skillRepository.findById(id).get();

        // xoa skill lien quan den job
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete subscriber (inside subscriber_skill)
        currentSkill.getSubscribers().forEach(subscriber -> subscriber.getSkills().remove(currentSkill));

        // xoa skill
        this.skillRepository.delete(this.skillRepository.findById(id).get());
    }
}
