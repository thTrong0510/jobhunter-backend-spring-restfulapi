package com.jobhunter.jobhunter.controller;

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
import com.jobhunter.jobhunter.domain.Role;
import com.jobhunter.jobhunter.domain.dto.result.ResultPaginationDTO;
import com.jobhunter.jobhunter.service.RoleService;
import com.jobhunter.jobhunter.util.annotation.ApiMessage;
import com.jobhunter.jobhunter.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a Role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        // // check id sửa lại khi tạo mới thì ko check id ý kiến cá nhân
        // if (this.roleService.fetchById(role.getId()) == null) {
        // throw new IdInvalidException("Role with id: " + role.getId() + " is
        // existed");
        // }

        // check name
        if (this.roleService.existByName(role.getName())) {
            throw new IdInvalidException("Role with name: " + role.getName() + " is existed");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws IdInvalidException {
        if (this.roleService.fetchById(role.getId()) == null) {
            throw new IdInvalidException("Role with id: " + role.getId() + " is not exist");
        }

        return ResponseEntity.ok(this.roleService.updateRole(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws IdInvalidException {
        if (this.roleService.fetchById(id) == null) {
            throw new IdInvalidException("Role with id: " + id + " is not exist");
        }

        this.roleService.deleteRole(this.roleService.fetchById(id));
        return ResponseEntity.ok(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch roles")
    public ResponseEntity<ResultPaginationDTO> getRoles(@Filter Specification<Role> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.roleService.getRoles(spec, pageable));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role by id")
    public ResponseEntity<Role> getRoleByid(@PathVariable("id") long id) throws IdInvalidException {
        if (this.roleService.fetchById(id) == null) {
            throw new IdInvalidException("Role with id: " + id + " is not exist");
        }

        return ResponseEntity.ok(this.roleService.fetchById(id));
    }
}
