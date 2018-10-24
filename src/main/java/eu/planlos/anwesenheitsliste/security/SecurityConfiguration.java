package eu.planlos.anwesenheitsliste.security;

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

					.antMatchers("/user/**")
//						.hasAnyRole("ADMIN", "USER")
						 .hasAnyAuthority("ADMIN", "USER") //or authenticated()

					.antMatchers("/admin/**")
//						.hasRole("ADMIN") 				//hasAuthority() or authenticated()
						 .hasAnyAuthority("ADMIN", "USER") //or authenticated()

						 .antMatchers("/actuator**")
						.hasRole("ADMIN")

				.and().formLogin()
				.successHandler(successHandler)
					.loginPage("/login")
//					.loginProcessingUrl("/login")
					
				.and().logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login")
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					
				.and().exceptionHandling()
					.accessDeniedHandler(deniedHandler)
				
				.and().exceptionHandling()
					.accessDeniedPage("/login");
		
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
