package com.jobhunter.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jobhunter.jobhunter.domain.Company;
import com.jobhunter.jobhunter.domain.dto.attach.Meta;
import com.jobhunter.jobhunter.domain.dto.result.ResultPaginationDTO;
import com.jobhunter.jobhunter.repository.CompanyRepository;
import com.jobhunter.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company saveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO fetchCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        Meta meta = new Meta();

        // lấy từ pageable tức là lấy từ mấy params
        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        // này là lấy khi đã xún database rồi truy vấn lên lại
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setResult(pageCompany.getContent());
        result.setMeta(meta);

        return result;
    }

    public void deleteCompanyById(long id) {
        Optional<Company> comOptional = this.companyRepository.findById(id);
        if (comOptional.isPresent()) {
            this.userRepository.deleteAll(comOptional.get().getUsers());
        }
        this.companyRepository.deleteById(id);
    }

    public Optional<Company> fetchCompanyById(long id) {
        return this.companyRepository.findById(id);
    }
}
