package eu.planlos.anwesenheitsliste.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import eu.planlos.anwesenheitsliste.model.TeamRepository;
import eu.planlos.anwesenheitsliste.model.User;
import eu.planlos.anwesenheitsliste.model.UserRepository;

@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {
 
    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

	    @Bean
        public UserService userService() {
            return new UserService();
        }
    }
 
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TeamRepository teamRepository;
    
    @MockBean
    private SecurityService securityService;
 
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() {
    	User flastname = new User("Firstname", "Lastname", "flastname", "flastname@example.com", bCryptPasswordEncoder.encode("securepw"), true, false);       
     
        Mockito.when(userRepository.findByLoginName(flastname.getLoginName()))
          .thenReturn(flastname);
    }
    
    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        String loginName = "flastname";
        User found = userService.findByLoginName(loginName);
      
         assertThat(found.getLoginName())
          .isEqualTo(loginName);
     }
}