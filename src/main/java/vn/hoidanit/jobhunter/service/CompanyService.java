package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company saveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> fetchCompanies() {
        return this.companyRepository.findAll();
    }

    public void deleteCompanyById(long id) {
        this.companyRepository.deleteById(id);
    }
}
