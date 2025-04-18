// package com.sparkmind.demo.security;

// import java.sql.Date;

// import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
// import org.springframework.stereotype.Component;

// import io.jsonwebtoken.SignatureAlgorithm;
// import lombok.Value;
// import lombok.extern.slf4j.Slf4j;

// @Component
// @Slf4j
// public class JwtTokenProvider {
//     @Value("${app.jwt.secret}")
//     private String jwtSecret;

//     @Value("${app.jwt.access-token-expiration-ms}")
//     private long jwtAccessExpirationMs;
//     //TODO
//     // @Value("${app.jwt.refresh-token-expiration-ms}") 
//     // private long jwtRefreshExpirationMs;
//     public String generateAccessToken(Authentication authentication){
//         String username = authentication.getName();
//         Date now = new Date();
//         Date expiryDate = new Date(now.getTime() + jwtAccessExpirationMs);
//         String roles = authentication.getAuthorities().stream()
//                 .map(GrantedAuthority::getAuthority)
//                 .collect(Collectors.joining(","));
//         return Jwts.builder()
//                 .setSubject(username)
//                 .claim("roles", roles)
//                 .setIssuedAt(now)
//                 .setExpiration(expiryDate)
//                 .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                 .compact();
//     }
// }
