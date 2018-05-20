package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Team {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idTeam;
	
	@Column(nullable = false, unique = true)
	@NotBlank
	private String teamName;

	@ManyToMany(mappedBy = "teams")
	private List<Participant> participants;

	@OneToMany(mappedBy = "team")
	private List<Meeting> meetings;
	
	@OneToMany(mappedBy = "teams")
	private List<User> users;
			
	/**
	 * Standard constructor
	 */
	public Team() {
		super();
	}
	
	/**
	 * @param teamName
	 * @param participants
	 */
	public Team(String teamName) {
		super();
		this.teamName = teamName;
	}

	/**
	 * @return the idTeam
	 */
	public Long getIdTeam() {
		return idTeam;
	}

	/**
	 * @param idTeam the idTeam to set
	 */
	public void setIdTeam(Long idTeam) {
		this.idTeam = idTeam;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * @return the participants
	 */
	public List<Participant> getParticipants() {
		return participants;
	}

	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	/**
	 * @return the meetings
	 */
	public List<Meeting> getMeetings() {
		return meetings;
	}

	/**
	 * @param meetings the meetings to set
	 */
	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
