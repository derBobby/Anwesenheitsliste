package eu.planlos.anwesenheitsliste.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;
import eu.planlos.anwesenheitsliste.model.exception.EmptyIdException;
import eu.planlos.anwesenheitsliste.model.exception.PasswordLengthException;
import eu.planlos.anwesenheitsliste.model.exception.UnknownUserSaveException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private final String passwordLengthError = "Das Passwort muss zwischen " + User.passwordMinLength + " und " + User.passwordMaxLength + " Zeichen lang sein";
	
	public User save(User user) throws DuplicateKeyException, PasswordLengthException, UnknownUserSaveException {
		
		// Is user null and name etc. taken -> dont save
		checkUniqueConstraintsForNewUser(user); //TODO throw in method necessary?
		
		// Is user is added and password is not set properly dont save
		checkProperPasswordForUserAdd(user);
		
		// If user is edited and password is not set properly dont save
		checkProperPasswordForUserEdit(user);
		
		// If user is edited and password was not set load it back from db and save it
		if(user.getIdUser() != null && ( user.getPassword().equals("")) ) {
			User dbUser = userRepo.findById(user.getIdUser()).get();
			user.setPassword(dbUser.getPassword());
			logger.debug("Benutzer bearbeiten: kein Passwort angegeben, benutze altes.");
			return userRepo.save(user);
		}

		// If user is added or edited and the password is OK then encrypt it and save it
		if(user.isPasswordLengthOK()) { 
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			logger.debug("Benutzer bearbeiten: neues Passwort angegeben.");
			return userRepo.save(user);
		}
		logger.error("Benutzer speichern: Hier hätten wir nicht ankommen dürfen.");
		throw new UnknownUserSaveException("Ein unbekannter Fehler beim Speichern des Benutzers ist aufgetreten");
	}

	private void checkProperPasswordForUserEdit(User user) throws PasswordLengthException {
		if(user.getIdUser() != null && ! user.getPassword().equals("") && user.isPasswordLengthOK() ) {
			logger.debug("Benutzer bearbeiten: das Passwort enspricht nicht den Anforderungen.");
			throw new PasswordLengthException(passwordLengthError);
		}
	}

	private void checkProperPasswordForUserAdd(User user) throws PasswordLengthException {
		if(user.getIdUser() == null && ! user.isPasswordLengthOK() ) {
			logger.debug("Benutzer anlegen: das Passwort enspricht nicht den Anforderungen.");
			throw new PasswordLengthException(passwordLengthError);
		}
	}

	private void checkUniqueConstraintsForNewUser(User user) {
		if(user.getIdUser() == null) {
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String email = user.getEmail();
			String loginName = user.getLoginName();
		
			// Should cover all constraints of the Entity
			if(userRepo.existsByLoginNameOrEmail(loginName, email)
					|| userRepo.existsByFirstNameAndLastName(firstName, lastName)
					) {
				
				logger.debug("Kann Benutzer nicht anlegen.");
				throw new DuplicateKeyException("Ein Benutzer mit dem Namen \"" + firstName + " " + lastName + "\""
						+ "oder dem Benutzernamen \"" + loginName + "\""
								+ "oder der E-Mailadresse \"" + email + "\" existiert bereits");

			}
		}
	}
	
	//TODO tests?
	public List<User> loadAllUsers() {
		logger.debug("Lade alle Benutzer");
		return (List<User>) userRepo.findAll();
	}
	
	//TODO tests?	
	public User loadUser(Long idUser) {
		logger.debug("Lade Benutzer mit id " + idUser);
		return userRepo.findById(idUser).get();
	}

	//TODO probably not working
	//TODO LOGGING
	/**
	 * Updates relation between team and users. Users not active will be ignored.
	 * @param team
	 * @param chosenUsers
	 * @throws EmptyIdException
	 */
	public void updateTeamForUsers(Team team) throws EmptyIdException {
		
		List<User> chosenUsers = team.getUsers();
		
		// Error if false user is passed
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIdUser() == null) {
				throw new EmptyIdException("Aktualisierung fehlgeschlagen. Daten Fehlerhaft.");
			}
		}
		
		// For all active users who were added but are not yet linked to the team
		List<User> usersForTeam = userRepo.findAllByTeamsIdTeam(team.getIdTeam());
		for(User chosenUser : chosenUsers) {
			if(chosenUser.getIsActive() && ! usersForTeam.contains(chosenUser)) {
				chosenUser.addTeam(team);
				userRepo.save(chosenUser);
			}
		}
		
		// For all active users in DB check if are chosen but should not be
		for(User userForTeam : usersForTeam) {
			if(userForTeam.getIsActive() && ! chosenUsers.contains(userForTeam)) {
				userForTeam.removeTeam(team);
				userRepo.save(userForTeam);
			}
		}
	}
}
