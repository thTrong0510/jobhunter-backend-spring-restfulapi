package com.jobhunter.jobhunter.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.jobhunter.jobhunter.domain.Permission;
import com.jobhunter.jobhunter.domain.Role;
import com.jobhunter.jobhunter.domain.User;
import com.jobhunter.jobhunter.service.UserService;
import com.jobhunter.jobhunter.util.SecurityUtil;
import com.jobhunter.jobhunter.util.exception.PermissionException;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email != null && !email.isEmpty()) {
            User currentUser = this.userService.fetchUserByEmail(email).isPresent()
                    ? this.userService.fetchUserByEmail(email).get()
                    : null;
            if (currentUser != null) {
                Role role = currentUser.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    if (permissions != null && permissions.size() > 0) {
                        Boolean isAllow = permissions
                                .stream().anyMatch(
                                        item -> item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                        if (!isAllow) {
                            throw new PermissionException("Have no Permission to access feature");
                        }
                    }
                } else {
                    throw new PermissionException("Have no Permission to access feature");

                }

            }
        }

        return true;
    }
}
