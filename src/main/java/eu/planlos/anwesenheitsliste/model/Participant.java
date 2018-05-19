package eu.planlos.anwesenheitsliste.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(
		uniqueConstraints={@UniqueConstraint(columnNames = {"firstName", "lastName"})}
)
public class Participant {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idParticipant;

	@Column(nullable = false)
	@NotBlank
	private String firstName;
	
	@Column
	@NotBlank
	private String lastName;
	
	@Column
	private String phone;
	
	@Column
	private String email;
	
	@Column(nullable = false)
	@NotNull
	private Boolean isActive;
	
	@Column
	@ManyToMany
	private List<Team> teams = new ArrayList<>();
	
	/**
	 * Standard constructor
	 */
	public Participant() {
		super();
	}
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param email
	 * @param isActive
	 */
	public Participant(String firstName, String lastName, String phone, String email, Boolean isActive) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.isActive = isActive;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isactive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the team
	 */
	public List<Team> getTeam() {
		return teams;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(List<Team> teams) {
		this.teams = teams;
	}

	/**
	 * @param team a team to add
	 */
	public void addTeam(Team team) {
		this.teams.add(team);
	}

	/**
	 * @return the idParticipant
	 */
	public Long getIdParticipant() {
		return idParticipant;
	}

	/**
	 * @param idParticipant the idParticipant to set
	 */
	public void setIdParticipant(Long idParticipant) {
		this.idParticipant = idParticipant;
	}
}