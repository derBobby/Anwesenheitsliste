package eu.planlos.anwesenheitsliste.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<GrantedAuthority> authoritiesList = new ArrayList<>();
		eu.planlos.anwesenheitsliste.model.User user = userRepo.findByLoginName(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("Kein Account mit Loginname " + username + " gefunden.");
		}
		
		if(user.getIsAdmin()) {
			authoritiesList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		authoritiesList.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		User springSecurityUser = new User(user.getLoginName(), user.getPassword(), authoritiesList);
		
		return (UserDetails) springSecurityUser;
	}

}
