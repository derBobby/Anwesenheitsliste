package eu.planlos.anwesenheitsliste.security;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_AREA_ACTUATOR;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_AREA_ADMIN;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_AREA_USER;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGIN_FORM;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGIN_FORM_ERROR;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGIN;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_HOME;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGOUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailService;

	@Autowired
	private LoginAuthenticationSuccessHandler successHandler;
	
	@Autowired
	private LoginAccessDeniedHandler deniedHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		        
		http.csrf().disable()
			.authorizeRequests()
			
				.antMatchers("/")
					.permitAll()
					
			// Ressources like CSS and JS
				.antMatchers("/webjars/**", "/sbadmin/**", "/css/**") //TODO
					.permitAll()

			// User area
				.antMatchers(URL_AREA_USER + "/**")
					 .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") //or authenticated()

			// Admin area
				.antMatchers(URL_AREA_ADMIN + "/**")
					 .hasAnyAuthority("ROLE_ADMIN") //or authenticated()

			// Technical like actuator
				.antMatchers(URL_AREA_ACTUATOR + "/**")
					 .hasAnyAuthority("ROLE_ADMIN") //or authenticated()
						 
			// Login procedure
				.and().formLogin()
				
				//Overrides the default created login form site
					.loginPage(URL_LOGIN_FORM)
					
				//Names URL on which Spring should listen itself
					.loginProcessingUrl(URL_LOGIN)
					
				//Controller
					.successHandler(successHandler)
					
				//NOT USED - would redirect to given page, but is handled by 
					//.defaultSuccessUrl(defaultSuccessUrl, alwaysUse)
					
				//Which site to load after login error
					.failureUrl(URL_LOGIN_FORM_ERROR)
										
			// Logout procedure
				.and().logout()
					.logoutUrl(URL_LOGOUT)
					.logoutSuccessUrl(URL_HOME)
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					
			// Exception handling
				.and().exceptionHandling()
				
					//Use either own handler or
					.accessDeniedHandler(deniedHandler); //TODO CONTINUE HERE
					//... use this default handler
					//.accessDeniedPage(URL_403);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		auth
			.userDetailsService(userDetailService)
			.passwordEncoder(bCryptPasswordEncoder);
	}

}
