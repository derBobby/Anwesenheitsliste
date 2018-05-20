package eu.planlos.anwesenheitsliste;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import eu.planlos.anwesenheitsliste.model.Meeting;
import eu.planlos.anwesenheitsliste.model.MeetingRepository;
import eu.planlos.anwesenheitsliste.model.Participant;
import eu.planlos.anwesenheitsliste.model.ParticipantRepository;
import eu.planlos.anwesenheitsliste.model.Team;
import eu.planlos.anwesenheitsliste.model.TeamRepository;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;

@Component
public class SampleDataCreater implements ApplicationRunner {

    private MeetingRepository meetingsRepo;
    private ParticipantRepository participantRepo;
    private TeamRepository teamRepo;
    private UserRepository userRepo;
    
    @Autowired
    public SampleDataCreater(MeetingRepository meetingsRepo, ParticipantRepository participantRepo, TeamRepository teamRepo, UserRepository userRepository) {
        this.meetingsRepo = meetingsRepo;
        this.participantRepo = participantRepo;
        this.teamRepo = teamRepo;
        this.userRepo = userRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) {
		
		meetingsRepo.deleteAll();
		participantRepo.deleteAll();
		teamRepo.deleteAll();
		userRepo.deleteAll();
		
		Participant p1 = participantRepo.save(new Participant("Eva", "Tester", "013371337", "a@sam.de", true));
		Participant p2 = participantRepo.save(new Participant("Ava", "Tester", "013371338", "b@sam.de", true));
		Participant p3 = participantRepo.save(new Participant("Iva", "Tester", "013371339", "c@sam.de", true));
		Participant p4 = participantRepo.save(new Participant("Ova", "Tester", "013371339", "d@sam.de", true));
			
		Team team1 = teamRepo.save(new Team("Cool Team"));
		Team team2 = teamRepo.save(new Team("Chilly Team"));
		Team team3 = teamRepo.save(new Team("Morons"));
		
		Meeting m1 = meetingsRepo.save(new Meeting("01.01.2018", "Just a Test", team1));
		Meeting m2 = meetingsRepo.save(new Meeting("02.01.2018", "Just a Test", team2));
		Meeting m3 = meetingsRepo.save(new Meeting("03.01.2018", "Just a Test", team3));

		User u1 = userRepo.save(new User("Adam", "Sample", "asam", "a@sam.de", "securepw", false, false));
		User u2 = userRepo.save(new User("Bdam", "Sample", "bsam", "b@sam.de", "securepw", false, true));
		User u3 = userRepo.save(new User("Cdam", "Sample", "csam", "c@sam.de", "securepw", true, false));
		User u4 = userRepo.save(new User("Ddam", "Sample", "dsam", "d@sam.de", "securepw", true, false));
		
		//

		u1.addTeam(team1);
		u1.addTeam(team2);
		u1.addTeam(team3);
		
		u2.addTeam(team2);
		u2.addTeam(team3);

		u3.addTeam(team3);
		
		u4.addTeam(team3);
		
		userRepo.save(u1);
		userRepo.save(u2);
		userRepo.save(u3);
		userRepo.save(u4);
		
		//
		
		p1.addTeam(team1);
		p1.addTeam(team2);
		p1.addTeam(team3);

		p2.addTeam(team2);
		p2.addTeam(team3);

		p3.addTeam(team3);
		
		p4.addTeam(team3);

		participantRepo.save(p1);
		participantRepo.save(p2);
		participantRepo.save(p3);
		participantRepo.save(p4);
    }
}