package com.dursu.proiect.config;

import com.dursu.proiect.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtTokenFilter jwtTokenFilter;
	
	public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
		this.jwtTokenFilter = jwtTokenFilter;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	protected DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
//		http
//				.csrf((csrf) -> csrf
//						.ignoringRequestMatchers("/**")
//				);
//		http.authorizeHttpRequests((authorize) -> authorize
//				//.requestMatchers("/**").permitAll()
//				//.requestMatchers("/user/**").
//				.anyRequest().authenticated());
//		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//	}
	
	private static final String[] AUTH_WHITELIST = {
			"/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/configuration/ui", "/configuration/security",
			"/auth/**",
			"/api/**"
	};
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf((csrf) -> csrf
						.ignoringRequestMatchers("/**")
				);
		
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/product/**", "/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
				.requestMatchers("/user/**").hasAuthority(Role.USER)
				.requestMatchers("/admin/**").hasAuthority(Role.ADMIN));
		
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
