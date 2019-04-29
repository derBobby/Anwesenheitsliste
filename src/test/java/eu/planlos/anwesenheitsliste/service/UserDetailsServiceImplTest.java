package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import eu.planlos.anwesenheitsliste.ApplicationRole;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Mock
	private UserRepository userRepo;

	private String testLoginName = "My Loginname";
	private String testPassword = "secret";
	private User testUser = new User("Test", "User", testLoginName, "user@example.com", testPassword, true, false);
	private User testAdminUser = new User("Test", "User", testLoginName, "user@example.com", testPassword, true, true);

	@Test
	public final void userIsLoggedIn_givesUserDetails() {
		when(userRepo.findByLoginName(testLoginName)).thenReturn(testUser);
		
		UserDetails springSecurityUser = userDetailsServiceImpl.loadUserByUsername(testLoginName);

		assertTrue(springSecurityUser.getUsername().equals(testLoginName));
		assertTrue(springSecurityUser.getPassword().equals(testPassword));
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public final void userIsNotLoggedIn_throwsException() {
		when(userRepo.findByLoginName(testLoginName)).thenReturn(null);
		
		userDetailsServiceImpl.loadUserByUsername(testLoginName);
	}

	@Test
	public final void userIsAdmin_hasAdminAuthority() {
		when(userRepo.findByLoginName(testLoginName)).thenReturn(testAdminUser);
		
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(testLoginName);
		
		assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN)));
	}
	@Test
	public final void userIsNotAdmin_hasNoAdminAuthority() {
		when(userRepo.findByLoginName(testLoginName)).thenReturn(testUser);
		
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(testLoginName);
		
		assertTrue(! userDetails.getAuthorities().contains(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN)));
	}

}
