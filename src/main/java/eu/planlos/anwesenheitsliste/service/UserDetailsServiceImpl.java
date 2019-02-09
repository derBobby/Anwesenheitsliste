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
			logger.error("Benutzer laden nicht möglich: Der Benutzer " + loginName + " existiert nicht");
			throw new UsernameNotFoundException("Kein Account mit Loginname " + loginName + " gefunden.");
		}
		
		if(user.getIsAdmin()) {
			logger.debug("Füge Administrator-Berechtigung in den Kontext hinzu");
			authoritiesList.add(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN));
		}
		authoritiesList.add(new SimpleGrantedAuthority(ApplicationRole.ROLE_USER));
		
		User springSecurityUser = new User(user.getLoginName(), user.getPassword(), authoritiesList);
		logger.debug("Benutzer erfolgreich geladen");
		// (UserDetails)
		return springSecurityUser;
	}

}
