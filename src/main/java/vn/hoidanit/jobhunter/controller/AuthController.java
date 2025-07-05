package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.exception.IdInvalidException;

@Controller
@RequestMapping("/api/v1")
public class AuthController {

    private final SecurityUtil securityUtil;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create access token
        ResLoginDTO ResLoginDTO = new ResLoginDTO();

        User user = this.userService.fetchUserByEmail(loginDTO.getUsername());
        ResLoginDTO.setUser(new ResLoginDTO.User(user.getId(), user.getEmail(), user.getName(), user.getRole()));

        ResLoginDTO
                .setAccessToken(
                        this.securityUtil.createAccessToken(authentication.getName(), ResLoginDTO));

        // create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(), ResLoginDTO);
        this.userService.updateRefreshToken(refreshToken, loginDTO.getUsername());

        // set cookie
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(ResLoginDTO);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch the account of current user ")
    public ResponseEntity<ResLoginDTO.UserGetAccount> fetchAccount() {
        User user = this.userService.fetchUserByEmail(
                SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : null);
        ResLoginDTO ResLoginDTO = new ResLoginDTO();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (user != null) {
            ResLoginDTO.setUser(new ResLoginDTO.User(user.getId(), user.getEmail(), user.getName(), user.getRole()));
            userGetAccount.setUser(ResLoginDTO.getUser());
        }
        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "none") String refreshToken)
            throws IdInvalidException {

        if (refreshToken.equals("none")) {
            throw new IdInvalidException("Refresh token in cookie is none");
        }
        // check valid refresh token
        Jwt jwtDecoded = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = jwtDecoded.getSubject();

        // check user by email & refresh token
        User user = this.userService.fetchUserByRefreshToken(refreshToken, email);
        if (user == null) {
            throw new IdInvalidException("Refresh token ko hop le");
        }

        // issue new token and set refresh token as cookies
        // create access token
        ResLoginDTO ResLoginDTO = new ResLoginDTO();

        ResLoginDTO.setUser(new ResLoginDTO.User(user.getId(), user.getEmail(), user.getName(), user.getRole()));

        ResLoginDTO.setAccessToken(this.securityUtil.createAccessToken(email, ResLoginDTO));

        // create refresh token
        String newRefreshToken = this.securityUtil.createRefreshToken(email, ResLoginDTO);
        this.userService.updateRefreshToken(newRefreshToken, email);

        // set cookie
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(ResLoginDTO);
    }

    @GetMapping("/auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", defaultValue = "none") String refreshToken)
            throws IdInvalidException {
        if (refreshToken.equals("none")) {
            throw new IdInvalidException("Refresh token in cookie is none");
        }

        // check valid refresh token
        Jwt jwtDecoded = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = jwtDecoded.getSubject();

        // check user by email & refresh token
        User user = this.userService.fetchUserByRefreshToken(refreshToken, email);
        if (user == null) {
            throw new IdInvalidException("Refresh token ko hop le");
        }

        // delete refresh token
        this.userService.updateRefreshToken(null, email);

        // delete refresh token in cookie
        // set cookie
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(null);
    }
}
