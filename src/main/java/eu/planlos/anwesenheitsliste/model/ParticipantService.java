package eu.planlos.anwesenheitsliste.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

	@Autowired
	private ParticipantRepository participantRepo;
	
	public Participant save(Participant participant) {
		return participantRepo.save(participant);
	}
	
	public void deleteUser(Participant participant) {
		participantRepo.delete(participant);
	}
	
	public List<Participant> findAll() {
		return (List<Participant>) participantRepo.findAll();
	}
	
	public Participant findById(Long idParticipant) {
		return participantRepo.findById(idParticipant).get();
	}
	
	public List<Participant> findAllByTeamsIdTeam(Long idTeam) {
		return participantRepo.findAllByTeamsIdTeam(idTeam);
	}
	
	public void updateTeamForParticipants(Team team, List<Participant> chosenParticipants) {
		
		List<Participant> participantsForTeam = participantRepo.findAllByTeamsIdTeam(team.getIdTeam());
		
		for(Participant chosenParticipant : chosenParticipants) {
			if(! participantsForTeam.contains(chosenParticipant)) {
				chosenParticipant.addTeam(team);
				participantRepo.save(chosenParticipant);
			}
		}
		
		for(Participant participantForTeam : participantsForTeam) {
			if(! chosenParticipants.contains(participantForTeam)) {
				participantForTeam.removeTeam(team);
				participantRepo.save(participantForTeam);
			}
		}
	}
}
