package eu.planlos.anwesenheitsliste.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Meeting {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long idMeeting;

	@Column(nullable = false)
	@NotNull(message = "darf nicht leer sein")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date meetingDate;

	@Column(nullable = false)
	@NotNull
	private String description;

	/*
	 * CONNECTIONS
	 */
	@ManyToOne
	@NotNull(message = "darf nicht leer sein") 
	private Team team;
	
	@ManyToMany
	private List<Participant> participants; 
	
	/*
	 * Standard constructor
	 */
	public Meeting() {
		super();
		participants = new ArrayList<Participant>();
	}

	/**
	 * @param meetingDate
	 * @param description
	 * @param team
	 */
	public Meeting(Date meetingDate, String description, Team team) {
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
	public Date getMeetingDate() {
		return meetingDate;
	}

	/**
	 * @param meetingDate the meetingDate to set
	 */
	public void setMeetingDate(Date meetingDate) {
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
	 * @param participants the participants to set
	 */
	public void addParticipants(List<Participant> participants) {
		this.participants.addAll(participants);
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