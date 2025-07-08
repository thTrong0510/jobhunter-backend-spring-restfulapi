package vn.hoidanit.jobhunter.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findById(long id);

    Optional<Role> findByName(String name);

    Page<Role> findAll(Pageable pageable);

    Page<Role> findAll(Specification<Role> spec, Pageable pageable);

    boolean existsByName(String name);
}
