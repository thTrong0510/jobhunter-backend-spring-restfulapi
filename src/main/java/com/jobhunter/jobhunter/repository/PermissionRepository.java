package com.jobhunter.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jobhunter.jobhunter.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    Page<Permission> findAll(Pageable pageable);

    Page<Permission> findAll(Specification<Permission> spec, Pageable pageable);

    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);

    Boolean existsByName(String name);

    List<Permission> findByIdIn(List<Long> id);

    Optional<Permission> findById(long id);
}
