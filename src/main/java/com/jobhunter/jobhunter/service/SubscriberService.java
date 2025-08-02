package com.jobhunter.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobhunter.jobhunter.domain.Job;
import com.jobhunter.jobhunter.domain.Skill;
import com.jobhunter.jobhunter.domain.Subscriber;
import com.jobhunter.jobhunter.domain.dto.attach.Company_;
import com.jobhunter.jobhunter.domain.dto.attach.Skill_;
import com.jobhunter.jobhunter.domain.dto.response.email.ResEmailJob;
import com.jobhunter.jobhunter.repository.JobRepository;
import com.jobhunter.jobhunter.repository.SkillRepository;
import com.jobhunter.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
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

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new Company_(job.getCompany().getId(), job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<Skill_> s = skills.stream().map(skill -> new Skill_(skill.getId(), skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);

        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

}
