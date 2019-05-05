package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;
import eu.planlos.anwesenheitsliste.model.exception.PasswordLengthException;
import eu.planlos.anwesenheitsliste.model.exception.UnknownUserSaveException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepo;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private static final String TESTLOGINNAME = "My Loginname";
	private static final String TESTPASSWORD = "secret";
	private static final User TESTUSER = new User("Test", "User", TESTLOGINNAME, "user@example.com", TESTPASSWORD, true, false);

	/*
	 *  Tests for save()
	 */
	
	@Test(expected = DuplicateKeyException.class)
	public final void newUserLoginNameTaken_throwsDuplicateKeyException() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		when(userRepo.existsByLoginNameOrEmail(TESTUSER.getLoginName(), TESTUSER.getEmail())).thenReturn(true);
		
		userService.save(TESTUSER);
	}

	@Test(expected = DuplicateKeyException.class)
	public final void newUserNameTaken_throwsDuplicateKeyException() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		when(userRepo.existsByLoginNameOrEmail(TESTUSER.getLoginName(), TESTUSER.getEmail())).thenReturn(false);
		when(userRepo.existsByFirstNameAndLastName(TESTUSER.getFirstName(), TESTUSER.getLastName())).thenReturn(true);
		
		userService.save(TESTUSER);
	}
	
	@Test(expected = PasswordLengthException.class)
	public final void newUserPasswordTooShort_throwsPasswordLengthException() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		String unsafePassword = "";
		for(int i=0 ; i<User.passwordMinLength-1; i++) {
			unsafePassword+="*";
		}
		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", unsafePassword, true, false);

		when(userRepo.existsByLoginNameOrEmail(TESTUSER.getLoginName(), TESTUSER.getEmail())).thenReturn(false);
		when(userRepo.existsByFirstNameAndLastName(TESTUSER.getFirstName(), TESTUSER.getLastName())).thenReturn(false);
		
		userService.save(user);
	}
	
	@Test(expected = PasswordLengthException.class)
	public final void newUserPasswortTooLong_throwsPasswordLengthException() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		String unsafePassword = "";
		for(int i=0 ; i<=User.passwordMaxLength+1; i++) {
			unsafePassword+="*";
		}
		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", unsafePassword, true, false);

		when(userRepo.existsByLoginNameOrEmail(TESTUSER.getLoginName(), TESTUSER.getEmail())).thenReturn(false);
		when(userRepo.existsByFirstNameAndLastName(TESTUSER.getFirstName(), TESTUSER.getLastName())).thenReturn(false);
		
		userService.save(user);
	}
	
	@Test(expected = PasswordLengthException.class)
	public final void editedUserPasswordNotProper_throwsPasswordLengthException() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		String unsafePassword = "";
		for(int i=0 ; i<User.passwordMinLength-1; i++) {
			unsafePassword+="*";
		}
		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", unsafePassword, true, false);
		user.setIdUser((long) 1337);
		
		userService.save(user);
	}
		
	@Test
	public final void editedUserEmptyPassword_savesUserWithLoadedPassword() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		String emptyPassword = "";
		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", emptyPassword, true, false);
		user.setIdUser((long) 1337);
		
		when(userRepo.findById(user.getIdUser())).thenReturn(Optional.of(user));
		
		userService.save(user);

		verify(userRepo, times(1)).findById(user.getIdUser());
		verify(userRepo, times(1)).save(user);
	}
	
	@Test
	public final void editedUserProperPassword_savesUserWithEncodedPassword() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		String safePassword = "";
		for(int i=0 ; i<User.passwordMinLength; i++) {
			safePassword+="*";
		}
		
		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", safePassword, true, false);
		user.setIdUser((long) 1337);
			
		userService.save(user);
		
		verify(bCryptPasswordEncoder, times(1)).encode(safePassword);
		verify(userRepo, times(1)).save(user);
	}
	
	@Test
	public final void addedUserProperPassword_savesUserWithEncodedPassword() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		String safePassword = "";
		for(int i=0 ; i<User.passwordMinLength; i++) {
			safePassword+="*";
		}		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", safePassword, true, false);
		user.setIdUser((long) 1337);
			
		userService.save(user);
		
		verify(bCryptPasswordEncoder, times(1)).encode(safePassword);
		verify(userRepo, times(1)).save(user);
	}
	
	@Test(expected = EmptyIdException.class)
	public final void newUserWithoutIdGiven_throwsException() throws EmptyIdException {
		Team testTeam = new Team("Testteam");
		User newUser = new User();
		List<User> userList = new ArrayList<>();
		userList.add(newUser);
		testTeam.setUsers(userList);
		
		userService.updateTeamForUsers(testTeam);		
	}
	
	@Test
	public final void inactiveUserAddedToTeam_UserIsIgnored() {
		fail();
	}
	
	@Test
	public final void inactiveUserRemovedFromTeam_UserIsIgnored() {
		fail();
	}
}
