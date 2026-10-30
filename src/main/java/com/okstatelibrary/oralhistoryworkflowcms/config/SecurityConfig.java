package com.okstatelibrary.oralhistoryworkflowcms.config;

import com.okstatelibrary.oralhistoryworkflowcms.service.Impl.UserSecurityService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private static final String SALT = "salt";

	private static final String[] PUBLIC_MATCHERS = {
			"/css/**",
			"/js/**",
			"/images/**",
			"/",
			"/index",
			"/api/interviews",
			"/api/interview/*"
	};

	private final UserSecurityService userSecurityService;

	public SecurityConfig(UserSecurityService userSecurityService) {
		this.userSecurityService = userSecurityService;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		// return new BCryptPasswordEncoder(12);

		return new BCryptPasswordEncoder(12) ; // , new SecureRandom(SALT.getBytes()));
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userSecurityService);
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
			throws Exception {

		http
				.authenticationProvider(authenticationProvider())

				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(PUBLIC_MATCHERS).permitAll()
						.anyRequest().authenticated()
				)

				.csrf(csrf -> csrf.disable())

				.cors(cors -> cors.disable())

				.formLogin(form -> form
						.loginPage("/login")
						.failureUrl("/login?error")
						.defaultSuccessUrl("/index")
						.permitAll()
				)

				.logout(logout -> logout
						.logoutRequestMatcher(
								new AntPathRequestMatcher("/logout")
						)
						.logoutSuccessUrl("/")
						.deleteCookies("remember-me")
						.permitAll()
				)

				.rememberMe(remember -> remember
						.userDetailsService(userSecurityService)
				);

		return http.build();
	}
}