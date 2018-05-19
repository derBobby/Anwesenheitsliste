package eu.planlos.anwesenheitsliste.model;

import java.util.ArrayList;
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
	private List<Participant> participants = new ArrayList<>();
	
	@OneToMany(mappedBy = "team")
	private List<Meeting> meetings = new ArrayList<>();
			
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
}
