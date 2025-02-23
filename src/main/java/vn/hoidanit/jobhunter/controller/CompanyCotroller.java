package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;

@Controller
public class CompanyCotroller {
    @PostMapping("/companies")
    public ResponseEntity<Company> createUser(@Valid @RequestBody Company companyJson) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyJson);
    }
}
