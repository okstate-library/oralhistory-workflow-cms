package com.okstatelibrary.oralhistoryworkflowcms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.okstatelibrary.oralhistoryworkflowcms.service.Impl.UserSecurityService;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private Environment env;

	private static final String SALT = "salt"; // Salt should be protected carefully
	private static final String[] PUBLIC_MATCHERS = {
			// "/webjars/**",
			"/css/**", "/js/**", "/images/**", "/", "/index",
            "/api/interviews", 
            "/api/interview/*", 
            
//            "/error/**/*",
//            "/console/**"
	};

	@Autowired
	private UserSecurityService userSecurityService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

//    	http.authorizeRequests()
//        .antMatchers("/").hasAnyAuthority("USER", "CREATOR", "EDITOR", "ADMIN")
//        .antMatchers("/new").hasAnyAuthority("ADMIN", "CREATOR")
//        .antMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
//        .antMatchers("/delete/**").hasAuthority("ADMIN")
//        .anyRequest().authenticated()
//        .and()
//        .formLogin().permitAll()
//        .and()
//        .logout().permitAll()
//        .and()
//        .exceptionHandling().accessDeniedPage("/403")
//        ;
		http.authorizeRequests().
//                antMatchers("/**").
				antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();

		http.csrf().disable().cors().disable().formLogin().failureUrl("/login?error").defaultSuccessUrl("/index")
				.loginPage("/login").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
				.deleteCookies("remember-me").permitAll().and().rememberMe();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}

}