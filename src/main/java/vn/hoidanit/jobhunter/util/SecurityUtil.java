package vn.hoidanit.jobhunter.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {

    // Algorithm
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    public final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // lấy tham số môi trường
    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtKey;

    @Value("${hoidanit.jwt.token-validity-in-seconds}")
    private long jwtKeyExpiration;

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();// thời gian tạo
        Instant validity = now.plus(this.jwtKeyExpiration, ChronoUnit.SECONDS);// thời gian hết hạn now + 1 ngày

        // @formatter:off này là Payload
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(validity)
            .subject(authentication.getName())
            .claim("Nguyen Van Thanh Trong", authentication.getAuthorities())
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build(); // header
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue(); //return String
    }
}
