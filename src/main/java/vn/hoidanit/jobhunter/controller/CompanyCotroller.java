package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.exception.IdInvalidException;

@Controller
public class CompanyCotroller {

    private final CompanyService companyService;

    public CompanyCotroller(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company companyJson) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.saveCompany(companyJson));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompanies() {
        return ResponseEntity.ok(this.companyService.fetchCompanies());
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> getCompanies(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(this.companyService.saveCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 100) {
            throw new IdInvalidException("Invalid Id");
        }
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.ok(null);
    }
}
