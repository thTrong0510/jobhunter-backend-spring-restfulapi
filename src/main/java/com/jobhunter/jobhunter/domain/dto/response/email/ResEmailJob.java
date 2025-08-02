package com.jobhunter.jobhunter.domain.dto.response.email;

import java.util.List;

import com.jobhunter.jobhunter.domain.dto.attach.Company_;
import com.jobhunter.jobhunter.domain.dto.attach.Skill_;

public class ResEmailJob {
    private String name;
    private double salary;
    private Company_ company;
    private List<Skill_> skills;

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setCompany(Company_ company) {
        this.company = company;
    }

    public void setSkills(List<Skill_> skills) {
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public Company_ getCompany() {
        return company;
    }

    public List<Skill_> getSkills() {
        return skills;
    }

    public ResEmailJob(String name, double salary, Company_ company, List<Skill_> skills) {
        this.name = name;
        this.salary = salary;
        this.company = company;
        this.skills = skills;
    }

    public ResEmailJob() {
    }

}
