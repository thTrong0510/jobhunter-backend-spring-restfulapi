package com.jobhunter.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobhunter.jobhunter.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    Company save(Company company);

    List<Company> findAll();

    void deleteById(long id);

    Optional<Company> findById(long id);

    Page<Company> findAll(Pageable pageable);

    List<Company> findAll(Specification<Company> specification);

    Page<Company> findAll(Specification<Company> specification, Pageable pageable);
}
