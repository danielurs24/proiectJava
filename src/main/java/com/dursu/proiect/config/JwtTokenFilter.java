package com.dursu.proiect.config;

import com.dursu.proiect.entity.User;
import com.dursu.proiect.repository.UserRepository;
import com.dursu.proiect.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtTokenUtil;
	private final UserRepository userRepository;
	
	public JwtTokenFilter(JwtUtil jwtTokenUtil,
			UserRepository userRepository) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain)
			throws ServletException, IOException {
		logger.info("Entering Internal HTTP Request filtering");
		// Get authorization header and validate
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (isEmpty(header) || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		
		final String token = header.split(" ")[1].trim();
		String username = jwtTokenUtil.validateTokenAndReturnUsername(token);
		if (username == null) {
			chain.doFilter(request, response);
			return;
		}
		
		User user = userRepository
				.findByUsername(username)
				.orElse(null);
		
		UsernamePasswordAuthenticationToken
				authentication = new UsernamePasswordAuthenticationToken(
				user, null,
				user == null ?
						List.of() : user.getAuthorities()
		);
		
		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
		);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
}
