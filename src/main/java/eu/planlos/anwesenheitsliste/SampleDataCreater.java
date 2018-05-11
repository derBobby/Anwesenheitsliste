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
		
		meetingsRepo.save(new Meeting("01.01.2018", "Just a Test"));
		meetingsRepo.save(new Meeting("02.01.2018", "Just a Test"));
		meetingsRepo.save(new Meeting("03.01.2018", "Just a Test"));
		
		participantRepo.save(new Participant("Eva", "Tester", "013371337", "a@sam.de", true));
		participantRepo.save(new Participant("Ava", "Tester", "013371338", "b@sam.de", true));
		participantRepo.save(new Participant("Iva", "Tester", "013371339", "c@sam.de", true));
		
		teamRepo.save(new Team("Cool Team"));
		teamRepo.save(new Team("Chilly Team"));
		teamRepo.save(new Team("Morons"));

		userRepo.save(new User("Adam", "Sample", "asam", "a@sam.de", "securepw", false, false));
		userRepo.save(new User("Bdam", "Sample", "bsam", "b@sam.de", "securepw", false, true));
		userRepo.save(new User("Cdam", "Sample", "csam", "c@sam.de", "securepw", true, false));
		userRepo.save(new User("Ddam", "Sample", "dsam", "d@sam.de", "securepw", true, true));
    }
}