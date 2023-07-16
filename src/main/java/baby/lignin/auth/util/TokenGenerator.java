package baby.lignin.auth.util;

import baby.lignin.auth.model.Token;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenGenerator {

    private static final String MEMBER_ID_CLAIM_KEY = "memberId";
    @Value("${spring.auth.jwt.secret_key}")
    private String secretKey;
    @Value("${spring.auth.jwt.access_time}")
    private Long accessTokenValidTime;
    @Value("${spring.auth.jwt.refresh_time}")
    private Long refreshTokenValidTime;

    String generateAccessToken(Long memberId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(MEMBER_ID_CLAIM_KEY, memberId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    String generateRefreshToken(Long memberId) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(MEMBER_ID_CLAIM_KEY, memberId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Token generateToken(Long memberId) {
        return Token.builder()
                .accessToken(generateAccessToken(memberId))
                .refreshToken(generateRefreshToken(memberId))
                .build();
    }
}
