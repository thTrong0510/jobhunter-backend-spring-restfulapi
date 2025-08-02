package com.jobhunter.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import com.jobhunter.jobhunter.domain.User;
import com.jobhunter.jobhunter.domain.dto.attach.auth.UserLogin_;
import com.jobhunter.jobhunter.domain.dto.request.ReqLoginDTO;
import com.jobhunter.jobhunter.domain.dto.response.auth.ResCurrentAccountDTO;
import com.jobhunter.jobhunter.domain.dto.response.auth.ResLoginDTO;
import com.jobhunter.jobhunter.domain.dto.response.user.ResCreateUserDTO;
import com.jobhunter.jobhunter.service.UserService;
import com.jobhunter.jobhunter.util.SecurityUtil;
import com.jobhunter.jobhunter.util.annotation.ApiMessage;
import com.jobhunter.jobhunter.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final SecurityUtil securityUtil;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Value("${jobhunter.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody User user) throws IdInvalidException {
        if (this.userService.checkExistedEmail(user.getEmail())) {
            throw new IdInvalidException("User with email: " + user.getEmail() + " is exist");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userDB = this.userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(userDB));
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
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        User user = this.userService.fetchUserByEmail(loginDTO.getUsername()).get();
        resLoginDTO.setUser(new UserLogin_(user.getId(), user.getName(), user.getEmail(), user.getRole()));

        resLoginDTO
                .setAccessToken(
                        this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO));

        // create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(), resLoginDTO);
        this.userService.updateRefreshToken(refreshToken, loginDTO.getUsername());

        // set cookie
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch the account of current user ")
    public ResponseEntity<ResCurrentAccountDTO> fetchAccount() {
        User user = this.userService.fetchUserByEmail(
                SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : null).get();
        ResCurrentAccountDTO userGetAccount = new ResCurrentAccountDTO();
        if (user != null) {
            userGetAccount.setUser(new UserLogin_(user.getId(), user.getName(), user.getEmail(), user.getRole()));
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

        ResLoginDTO.setUser(new UserLogin_(user.getId(), user.getName(), user.getEmail(), user.getRole()));

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

    @PostMapping("/auth/logout")
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
