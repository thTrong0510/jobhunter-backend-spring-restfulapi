package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository PermissionRepository) {
        this.permissionRepository = PermissionRepository;
    }

    public Permission createPermission(Permission Permission) {
        return this.permissionRepository.save(Permission);
    }

    public Boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(),
                permission.getApiPath(), permission.getMethod());
    }

    public Optional<Permission> fetchPermissionById(long id) {
        return this.permissionRepository.findById(id);
    }

    public Permission updatePermission(Permission permission) {
        Permission permissionDB = this.fetchPermissionById(permission.getId()).get();
        permissionDB.setModule(permission.getModule());
        permissionDB.setApiPath(permission.getApiPath());
        permissionDB.setMethod(permission.getMethod());
        permissionDB.setName(permission.getName());

        return this.permissionRepository.save(permissionDB);
    }

    public void deletePermissionById(long id) {
        // delete permission_role
        Optional<Permission> permOptional = this.fetchPermissionById(id);
        Permission currentPermission = permOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete permission
        this.permissionRepository.delete(currentPermission);
    }

    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPermissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);// lúc đầu đã -1
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pPermissions.getTotalPages());
        meta.setTotal(pPermissions.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pPermissions.getContent());

        return rs;

    }
}
