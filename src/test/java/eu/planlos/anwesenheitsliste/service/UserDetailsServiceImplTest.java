package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.assertFalse;
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

	private static final String TESTLOGINNAME = "My Loginname";
	private static final String TESTPASSWORD = "secret";
	private static final User TESTUSER = new User("Test", "User", TESTLOGINNAME, "user@example.com", TESTPASSWORD, true, false);
	private static final User TESTADMINUSER = new User("Test", "User", TESTLOGINNAME, "user@example.com", TESTPASSWORD, true, true);

	@Test
	public final void userIsLoggedIn_givesUserDetails() {
		when(userRepo.findByLoginName(TESTLOGINNAME)).thenReturn(TESTUSER);
		
		UserDetails springSecurityUser = userDetailsServiceImpl.loadUserByUsername(TESTLOGINNAME);

		assertTrue(springSecurityUser.getUsername().equals(TESTLOGINNAME));
		assertTrue(springSecurityUser.getPassword().equals(TESTPASSWORD));
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public final void userIsNotLoggedIn_throwsException() {
		when(userRepo.findByLoginName(TESTLOGINNAME)).thenReturn(null);
		
		userDetailsServiceImpl.loadUserByUsername(TESTLOGINNAME);
	}

	@Test
	public final void userIsAdmin_hasAdminAuthority() {
		when(userRepo.findByLoginName(TESTLOGINNAME)).thenReturn(TESTADMINUSER);
		
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(TESTLOGINNAME);
		
		assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN)));
	}
	
	@Test
	public final void userIsNotAdmin_hasNoAdminAuthority() {
		when(userRepo.findByLoginName(TESTLOGINNAME)).thenReturn(TESTUSER);
		
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(TESTLOGINNAME);
		
		assertFalse(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(ApplicationRole.ROLE_ADMIN)));
	}

}
