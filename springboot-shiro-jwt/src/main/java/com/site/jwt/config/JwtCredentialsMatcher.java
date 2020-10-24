package com.site.jwt.config;

import com.site.jwt.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;


@Slf4j
public class JwtCredentialsMatcher implements CredentialsMatcher {

 
	/**
	 * JwtCredentialsMatcher只需验证JwtToken内容是否合法
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
 
		String token = authenticationToken.getCredentials().toString();
		String username = authenticationToken.getPrincipal().toString();
		try {
			Algorithm algorithm = Algorithm.HMAC256(JwtUtils.SECRET);
			JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
			verifier.verify(token);
			return true;
		} catch (JWTVerificationException e) {
			log.error(e.getMessage());
		}
		return false;
	}
 
}