package com.photoapp.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.photoapp.users.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	Environment environment;
	UserService userService;
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public WebSecurity(Environment environment, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.environment = environment;
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// disable csrf
		http.csrf().disable();

		// (old way) permit request to this path
		// http.authorizeRequests().antMatchers("/users/**").permitAll();

		// (new way) allow only IP address of Zuul API Gateway, register
		// AuthenticationFilter with
		// Http Security
		http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("zuul.api.gateway.ip.address.local.mac.machine"))
				.and().addFilter(getAuthenticationFilter());

		// disable frameoptions for Database H2
		http.headers().frameOptions().disable();
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, environment,
				authenticationManager());
		
		// old way
		// authenticationFilter.setAuthenticationManager(authenticationManager());

		// customize User Authentication URL
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
}
