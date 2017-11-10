package com.kinnear.test.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.kinnear.test.ValidationConfiguration;
import com.kinnear.test.user.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {ValidationConfiguration.class})
public class UserTest {
    @Autowired
    private Validator validator;
    
    @MockBean
    private IUserRepository userRepo;
    
    @Test
    public void validUser() throws Exception {
    	// mocking
    	when(userRepo.isUserValid("Adam")).thenReturn(true);
    	
        // given
    	User user = new User("Adam");
        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        // then
        assertThat(violations, hasSize(0));
        
        // mocking
        verify(userRepo).isUserValid("Adam");
        verifyNoMoreInteractions(userRepo);
    }

    @Test
    public void invalidUser() throws Exception {
    	// mocking
    	when(userRepo.isUserValid("Adam")).thenReturn(false);
    	
        // given
    	User user = new User("Adam");
        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        // then
        assertThat(violations, hasSize(1));
        assertThat(violations.iterator().next().getMessage(), equalTo("The user name 'Adam' is not valid"));
        
        // mocking
        verify(userRepo).isUserValid("Adam");
        verifyNoMoreInteractions(userRepo);
    }

}