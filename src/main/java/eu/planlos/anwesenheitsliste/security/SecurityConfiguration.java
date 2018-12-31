package eu.planlos.anwesenheitsliste.security;

import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_AREA_ACTUATOR;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_AREA_ADMIN;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_AREA_USER;
import static eu.planlos.anwesenheitsliste.viewhelper.ApplicationPaths.URL_LOGIN;
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
		        
		http.csrf().disable() //TODO csrf
			.authorizeRequests()
				.antMatchers("/")
					.permitAll()
					
				.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**") //TODO
					.permitAll()

					.antMatchers(URL_AREA_USER + "/**")
//						.hasAnyRole("ADMIN", "USER")
						 .hasAnyAuthority("ADMIN", "USER") //or authenticated()

					.antMatchers(URL_AREA_ADMIN + "/**")
//						.hasRole("ADMIN") 				//hasAuthority() or authenticated()
						 .hasAnyAuthority("ADMIN") //or authenticated()

						 .antMatchers(URL_AREA_ACTUATOR + "/**")
						.hasRole("ADMIN")

				.and().formLogin()
				.successHandler(successHandler)
					.loginPage(URL_LOGIN)
//					.loginProcessingUrl(URL_LOGIN)
					
				.and().logout()
					.logoutUrl(URL_LOGOUT)
					.logoutSuccessUrl(URL_LOGIN)
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					
				.and().exceptionHandling()
					.accessDeniedHandler(deniedHandler)
				
				.and().exceptionHandling()
					.accessDeniedPage(URL_LOGIN);
		
//					.loginProcessingUrl("/login")
//					.failureUrl("/loginpage")
//					.usernameParameter("loginName")
//					.passwordParameter("password")
//					.defaultSuccessUrl("/loginsuccess")
		


	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		auth
			.userDetailsService(userDetailService)
			.passwordEncoder(bCryptPasswordEncoder);
	}

}
