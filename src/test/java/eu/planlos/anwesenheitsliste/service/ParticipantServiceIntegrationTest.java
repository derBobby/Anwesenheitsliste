package eu.planlos.anwesenheitsliste.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import eu.planlos.anwesenheitsliste.model.ParticipantRepository;

@RunWith(SpringRunner.class)
public class ParticipantServiceIntegrationTest {
 
    @TestConfiguration
    static class ParticipantServiceIntegrationTestContextConfiguration {

	    @Bean
        public ParticipantService participantService() {
            return new ParticipantService();
        }
    }
 
    @MockBean
    private ParticipantRepository participantRepo;
    
    @MockBean
    private SecurityService securityService;
 
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() {
//    	User flastname = new User("Firstname", "Lastname", "flastname", "flastname@example.com", bCryptPasswordEncoder.encode("securepw"), true, false);       
//     
//        Mockito.when(userRepository.findByLoginName(flastname.getLoginName()))
//          .thenReturn(flastname);
    }
    
    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
//        String loginName = "flastname";
//        User found = userService.loadUser(loginName);
      
//         assertThat(found.getLoginName())
//          .isEqualTo(loginName);
     }
}