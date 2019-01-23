package eu.planlos.anwesenheitsliste.configuration;

import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_AREA_ACTUATOR;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_403;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_ERROR_DEFAULT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_AREA_ADMIN;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_AREA_DEV;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_403_TEST;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_AREA_USER;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_HOME;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_LOGIN;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_LOGIN_FORM;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_LOGIN_FORM_ERROR;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_LOGOUT;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_DATENSCHUTZ;
import static eu.planlos.anwesenheitsliste.ApplicationPath.URL_IMPRESSUM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import eu.planlos.anwesenheitsliste.ApplicationRole;
import eu.planlos.anwesenheitsliste.security.LoginAuthenticationSuccessHandler;
import eu.planlos.anwesenheitsliste.service.UserDetailsServiceImpl;

//@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailService;

	@Autowired
	private LoginAuthenticationSuccessHandler successHandler;
	
	//@Autowired
	//private LoginAccessDeniedHandler deniedHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		        
		http.csrf().disable()
		
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			
			.and().authorizeRequests()
				
				/*
				 * PUBLIC
				 */
			
				// Ressources like CSS and JS
				.antMatchers("/webjars/**", "/css/**") //"/sbadmin/**", 
					.permitAll()
				
				.antMatchers("/", URL_LOGIN_FORM, URL_LOGIN, URL_DATENSCHUTZ, URL_IMPRESSUM, URL_ERROR_DEFAULT)
					.permitAll()

				/*
				 * ROLE SPECIFIC
				 */
					
				// User area
				.antMatchers(URL_AREA_USER + "/**")
					 .hasAnyAuthority(ApplicationRole.ROLE_ADMIN, ApplicationRole.ROLE_USER)

				// Admin area
				.antMatchers(URL_AREA_ADMIN + "/**")
					 .hasAnyAuthority(ApplicationRole.ROLE_ADMIN)

				// DEV area
				.antMatchers(URL_403_TEST)
					.hasAnyAuthority("ROLE_NONEXISTENT")
				.antMatchers(URL_AREA_DEV + "/**")
					.hasAnyAuthority(ApplicationRole.ROLE_ADMIN)
									 
				// Technical like actuator
				.antMatchers(URL_AREA_ACTUATOR + "/**")
					 .hasAnyAuthority(ApplicationRole.ROLE_ADMIN)

				/*
				 * DENY REMAINING
				 */
				.antMatchers("/**")
					.denyAll()
						
					
			// Login procedure
			.and().formLogin()
				
				// Overrides the default created login form site
				.loginPage(URL_LOGIN_FORM)
				
				// Names URL on which Spring should listen itself
				.loginProcessingUrl(URL_LOGIN)
				
				// Controller
				.successHandler(successHandler)
				
				// NOT USED - would redirect to given page, but is handled by 
				//.defaultSuccessUrl(defaultSuccessUrl, alwaysUse)
				
				// Which site to load after login error
				.failureUrl(URL_LOGIN_FORM_ERROR)
										
			// Logout procedure
			.and().logout()
				.logoutUrl(URL_LOGOUT)
				.logoutSuccessUrl(URL_HOME)
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				
			// Exception handling
			.and().exceptionHandling()
			
				// Use either own handler or
				//.accessDeniedHandler(deniedHandler);
				// ... use this default handler
				.accessDeniedPage(URL_ERROR_403)
				;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		auth
			.userDetailsService(userDetailService)
			.passwordEncoder(bCryptPasswordEncoder);
	}

}
