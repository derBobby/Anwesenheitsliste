package eu.planlos.anwesenheitsliste.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.ApplicationRole;
import eu.planlos.anwesenheitsliste.model.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {

		List<GrantedAuthority> authoritiesList = new ArrayList<>();
		eu.planlos.anwesenheitsliste.model.User user = userRepo.findByLoginName(loginName);
		
		if(user == null) {
			logger.error("Tried to load non-existing user with loginName=" + loginName);
			throw new UsernameNotFoundException("Kein Account mit Loginname " + loginName + " gefunden.");
		}
		
		if(user.getIsAdmin()) {
			logger.debug("Adding admin authority to UserDetails.");
			authoritiesList.add(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN));
		}
		authoritiesList.add(new SimpleGrantedAuthority(ApplicationRole.ROLE_USER));
		
		User springSecurityUser = new User(user.getLoginName(), user.getPassword(), authoritiesList);
		
		// (UserDetails)
		return springSecurityUser;
	}

}
