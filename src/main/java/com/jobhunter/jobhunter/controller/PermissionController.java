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
import com.jobhunter.jobhunter.domain.Permission;
import com.jobhunter.jobhunter.domain.dto.result.ResultPaginationDTO;
import com.jobhunter.jobhunter.service.PermissionService;
import com.jobhunter.jobhunter.util.annotation.ApiMessage;
import com.jobhunter.jobhunter.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {

        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission is existed");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("update a permission")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission)
            throws IdInvalidException {
        if (this.permissionService.fetchPermissionById(permission.getId()).isEmpty()) {
            throw new IdInvalidException("Permission id: " + permission.getId() + " is not exist");
        }

        if (this.permissionService.isPermissionExist(permission)) {
            if (this.permissionService.isSameName(permission)) {
                throw new IdInvalidException("Permission is existed");

            }
        }

        return ResponseEntity.ok().body(this.permissionService.updatePermission(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) throws IdInvalidException {
        if (this.permissionService.fetchPermissionById(id).isEmpty()) {
            throw new IdInvalidException("Permission id: " + id + " is not exist");
        }
        this.permissionService.deletePermissionById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch permissions")
    public ResponseEntity<ResultPaginationDTO> getPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.permissionService.getPermissions(spec, pageable));
    }
}
