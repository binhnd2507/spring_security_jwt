package com.nguyenduybinh.Jwt.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.nguyenduybinh.Jwt.entities.CustomUserDetails;
import com.nguyenduybinh.Jwt.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenUtil {

	private final String jwtSecret = "bryptToken";
	private final String jwtIssuer = "binhnd2507@gmail.com";
	
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class); 
	
	public String generateTokenByCustomUserDetails(CustomUserDetails customUserDetails) {
		return Jwts.builder()
				.setSubject(String.format("%s,%s,%s", customUserDetails.getUser().getId(), customUserDetails.getUser().getUsername(), customUserDetails.getUser().getPhone()))
				.setIssuer(jwtIssuer)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // 2 hours
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String getUserIdByToken(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject().split(",")[0];
	}
	
	public String getUsernameByToken(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject().split(",")[1];
	}
	
	public String getPhoneByToken(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject().split(",")[2];
	}
	
	public Date getExpirationDateByToken(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();
		return claims.getExpiration();
	}
	
	public Boolean validate(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature - {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token - {}", e.getMessage());
		} catch (ExpiredJwtException e) {
            logger.error("Expired JWT token - {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token - {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty - {}", e.getMessage());
        }
		return false;
	}
}
