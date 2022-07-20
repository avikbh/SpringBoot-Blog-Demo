package com.app.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	CustomUserDetailService userService;

	@Autowired
	private JwtFilter jwtFilter;

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception
	{
		httpSecurity.csrf().disable().authorizeRequests()
				.antMatchers("/login/**")
				.permitAll()
				.antMatchers("/h2/**")
				.permitAll()
				.antMatchers("/register")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}


//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userService);
//	}

	@Bean
	PasswordEncoder passwordEncoder()
	{
//		return new BCryptPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint(){
		return new CustomAuthenticationEntryPoint();
	}

}

class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
		res.setContentType("application/json;charset=UTF-8");
		res.setStatus(500);
//		res.setContentType("Unable to read JSON value");
		res.getWriter().write("Unable to read JSON value");
	}
}
