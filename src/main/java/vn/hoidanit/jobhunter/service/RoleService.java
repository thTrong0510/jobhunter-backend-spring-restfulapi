package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role createRole(Role role) {
        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(role);
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role updateRole(Role role) {
        Role roleDB = this.fetchById(role.getId());

        // check permission
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        roleDB.setName(role.getName());
        roleDB.setPermissions(role.getPermissions());
        roleDB.setDescription(role.getDescription());
        roleDB.setActive(role.isActive());

        return this.roleRepository.save(roleDB);
    }

    public Role fetchById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    public void deleteRole(Role role) {
        this.roleRepository.delete(role);
    }

    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pRoles = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pRoles.getTotalPages());
        meta.setTotal(pRoles.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pRoles.getContent());

        return rs;

    }
}
