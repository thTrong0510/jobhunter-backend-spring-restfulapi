package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CompanyCotroller {

    private final CompanyService companyService;

    public CompanyCotroller(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("Create a company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company companyJson) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.saveCompany(companyJson));
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getCompanies(@Filter Specification<Company> spec, Pageable pageable) {
        return ResponseEntity.ok(this.companyService.fetchCompanies(spec, pageable));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch a company by id")
    public ResponseEntity<Company> getCompany(@PathVariable("id") long id) throws IdInvalidException {
        if (!this.companyService.fetchCompanyById(id).isPresent()) {
            throw new IdInvalidException("Company with id: " + id + " is not existed");
        }
        return ResponseEntity.ok(this.companyService.fetchCompanyById(id).get());
    }

    @PutMapping("/companies")
    @ApiMessage("Update a company")
    public ResponseEntity<Company> getCompanies(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(this.companyService.saveCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("Delete a company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
        if (this.companyService.fetchCompanyById(id) == null) {
            throw new IdInvalidException("Id user: " + id + " not found");
        }
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.ok(null);
    }
}
