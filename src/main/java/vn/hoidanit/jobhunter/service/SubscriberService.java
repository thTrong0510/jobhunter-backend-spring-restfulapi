package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public Subscriber createSubscriber(Subscriber subscriberRequest) {
        // check skill
        if (subscriberRequest.getSkills() != null && subscriberRequest.getSkills().size() > 0) {
            List<Long> reqSkills = subscriberRequest.getSkills().stream().map(item -> item.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriberRequest.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriberRequest);
    }

    public Subscriber updateSubscriber(Subscriber subscriberDB, Subscriber subscriberRequest) {
        // check skill
        if (subscriberRequest.getSkills() != null && subscriberRequest.getSkills().size() > 0) {
            List<Long> reqSkills = subscriberRequest.getSkills().stream().map(item -> item.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriberDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriberDB);
    }

    public Subscriber fetchById(long id) {
        return this.subscriberRepository.findById(id).isPresent() ? this.subscriberRepository.findById(id).get() : null;
    }

    public Subscriber fetchByEmail(String email) {
        return this.subscriberRepository.findByEmail(email).isPresent()
                ? this.subscriberRepository.findByEmail(email).get()
                : null;
    }

    public Boolean isExistEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }
}
