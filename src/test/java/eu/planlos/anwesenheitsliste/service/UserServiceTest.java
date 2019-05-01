package eu.planlos.anwesenheitsliste.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;
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
		for(int i=0 ; i<User.passwordMinLength; i++) {
			unsafePassword+="*";
		}
		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", unsafePassword, true, false);
		user.setIdUser((long) 1337);
		
		userService.save(user);
	}
	
	@Test
	public final void editedUserProperPassword_savesinDb() throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		String emptyPassword = "";
		User user = new User("Test", "User", TESTLOGINNAME, "user@example.com", emptyPassword, true, false);
		user.setIdUser((long) 1337);
		
		when(userRepo.save(user)).thenReturn(user);
		when(userRepo.findById(user.getIdUser()).get()).thenReturn(user);
		
		User savedUser = userService.save(user);
		
		assertNotNull(savedUser);
	}
	
	
}
