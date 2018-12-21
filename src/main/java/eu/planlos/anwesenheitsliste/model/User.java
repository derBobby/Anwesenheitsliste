package eu.planlos.anwesenheitsliste.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(
	uniqueConstraints={@UniqueConstraint(columnNames = {"firstName", "lastName"})}
)
//public class User extends org.springframework.security.core.userdetails.User {
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idUser;
	
	@Column(nullable = false)
	@Size(min = 3, max = 20)
	private String firstName;
	
	@Column(nullable = false)
	@Size(min = 3, max = 20)
	private String lastName;
	
	@Column(nullable = false, unique = true)
	@Size(min = 3, max = 20)
	private String loginName;
	
	@Column(nullable = false, unique = true)
	@NotEmpty
	@Email(message="muss eine gültige E-Mailadresse sein")
	private String email;
	
	//TODO validation length unsecure
	@Column(nullable = false)
	@NotNull
	@NotEmpty
	private String password;
	
	@Column
	private String resetHash;
	
	@Column(nullable = false)
	@NotNull
	private Boolean isActive;
	
	@Column(nullable = false)
	@NotNull
	private Boolean isAdmin;

	@ManyToMany
	private List<Team> teams;
	
	/**
	 * Standard constructor
	 */
	public User() {
		super();
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param loginName
	 * @param email
	 * @param password
	 * @param isActive
	 * @param isAdmin
	 */
	public User(String firstName, String lastName, String loginName, String email, String password, Boolean isActive, Boolean isAdmin) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.loginName = loginName;
		this.email = email;
		this.password = password;
		this.isActive = isActive;
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the idUser
	 */
	public Long getIdUser() {
		return idUser;
	}
	
	/**
	 * @param idUser the idUser to set
	 */
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the resetHash
	 */
	public String getResetHash() {
		return resetHash;
	}
	
	/**
	 * @param resetHash the resetHash to set
	 */
	public void setResetHash(String resetHash) {
		this.resetHash = resetHash;
	}
	
	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}
	
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * @return the isAdmin
	 */
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	
	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the teams
	 */
	public List<Team> getTeams() {
		return teams;
	}

	/**
	 * @param teams the teams to set
	 */
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	/**
	 * @param team the team to add
	 */
	public void addTeam(Team team) {
		if(this.teams == null) {
			this.teams = new ArrayList<Team>();
		}
		
		this.teams.add(team);
	}

	/**
	 * @param team the team to remove
	 */
	public void removeTeam(Team team) {
		if(this.teams != null && this.teams.contains(team)) {
			this.teams.remove(team);
		}
	}
	
    //TODO correct?
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return this.idUser == user.getIdUser();
    }

    //TODO correct?
    @Override
    public int hashCode() {
        return Objects.hash(this.idUser);
    }
	
	
}
