package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.exception.IdInvalidException;

@Controller
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skillJson) throws IdInvalidException {
        if (this.skillService.checkExistedNameSkill(skillJson.getName())) {
            throw new IdInvalidException(skillJson.getName() + "was Existed skill");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.saveSkill(skillJson));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skillJson) throws IdInvalidException {
        Optional<Skill> skill = this.skillService.fetchSkillById(skillJson.getId());
        if (!skill.isPresent()) {
            throw new IdInvalidException("Id skill: " + skill.get().getId() + " not found");
        }
        if (!skill.get().getName().equals(skillJson.getName())) {
            if (this.skillService.checkExistedNameSkill(skillJson.getName())) {
                throw new IdInvalidException(skill.get().getName() + " was Existed skill");
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(this.skillService.updateSkill(skillJson));
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<ResultPaginationDTO> fetchSkills(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.skillService.fetchAllSkills(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkills(@PathVariable long id) throws IdInvalidException {
        Optional<Skill> currentSkill = this.skillService.fetchSkillById(id);
        if (!currentSkill.isPresent()) {
            throw new IdInvalidException("Id skill: " + currentSkill.get().getId() + " not found");
        }
        this.skillService.deleteSkillByid(id);
        return ResponseEntity.ok(null);
    }
}
