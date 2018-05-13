package eu.planlos.anwesenheitsliste.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Meeting {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idMeeting;

	@Column(nullable = false)
	@NotNull(message = "darf nicht leer sein")
	@NotBlank
	private String meetingDate;

	@Column(nullable = false)
	@NotNull
	private String description;
	
	@ManyToOne
	@NotNull(message = "darf nicht leer sein") 
	private Team team;
	
	@ManyToMany
	private List<Participant> participants = new ArrayList<Participant>(); 
	
	/**
	 * Standard constructor
	 */
	public Meeting() {
		super();
	}

	/**
	 * @param meetingDate
	 * @param description
	 */
	public Meeting(String meetingDate, String description, Team team) {
		this.meetingDate = meetingDate;
		this.description = description;
		this.team = team;
	}

	public Meeting(Team team) {
		this.team = team;
	}

	/**
	 * @return the meetingDate
	 */
	public String getMeetingDate() {
		return meetingDate;
	}

	/**
	 * @param meetingDate the meetingDate to set
	 */
	public void setMeetingDate(String meetingDate) {
		this.meetingDate = meetingDate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
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
	 * @return the idMeeting
	 */
	public Long getIdMeeting() {
		return idMeeting;
	}

	/**
	 * @param idMeeting the idMeeting to set
	 */
	public void setIdMeeting(Long idMeeting) {
		this.idMeeting = idMeeting;
	}
}