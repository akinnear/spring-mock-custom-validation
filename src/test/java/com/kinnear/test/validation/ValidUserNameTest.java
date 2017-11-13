package com.kinnear.test.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.annotation.Validated;

import com.kinnear.test.configuration.ValidationConfiguration;
import com.kinnear.test.service.IUserUtils;
import com.kinnear.test.validation.ValidUserNameTest.MethodUserNames;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {ValidationConfiguration.class, MethodUserNames.class})
public class ValidUserNameTest {
    @Autowired
    private Validator validator;
    
    @MockBean
    private IUserUtils userUtils;
    
    @Test
    public void validateFields() throws Exception {
        // given
    	FieldUserNames userNames = new FieldUserNames();
    	
        // when
    	when(userUtils.isUserNameValid("invalid")).thenReturn(false);
    	when(userUtils.isUserNameValid("valid")).thenReturn(true);
        Set<ConstraintViolation<FieldUserNames>> violations = validator.validate(userNames);
        
        // then
        assertThat(violations, hasSize(4));
        List<String> errorMsgs = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(errorMsgs, containsInAnyOrder(
        		"The user name cannot be null", 
        		"The user name 'invalid' is not valid",
        		"The user name cannot be empty whitespace",
        		"The user name cannot be empty whitespace"));
    }
    
    /*
     * Not working right now
    @Test
    public void validateMethods() throws Exception {
        // given
    	MethodUserNames userNames = new MethodUserNames();
    	
        // when
    	when(userRepo.isUserNameValid("invalid")).thenReturn(false);
    	when(userRepo.isUserNameValid("valid")).thenReturn(true);
        Set<ConstraintViolation<MethodUserNames>> violations = validator.validate(userNames);
        
        // then
        assertThat(violations, hasSize(4));
        List<String> errorMsgs = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(errorMsgs, containsInAnyOrder(
        		"The user name cannot be null", 
        		"The user name 'invalid' is not valid",
        		"The user name cannot be empty whitepace",
        		"The user name cannot be empty whitepace"));
    }
    */
    
    @Test
    public void validateTypes() throws Exception {
        // given
    	TypeUserNames userNames = new TypeUserNames();
    	
        // when
    	when(userUtils.isUserNameValid("invalid")).thenReturn(false);
    	when(userUtils.isUserNameValid("valid")).thenReturn(true);
        Set<ConstraintViolation<TypeUserNames>> violations = validator.validate(userNames);
        
        // then
        assertThat(violations, hasSize(4));
        List<String> errorMsgs = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(errorMsgs, containsInAnyOrder(
        		"The user name cannot be null", 
        		"The user name 'invalid' is not valid",
        		"The user name cannot be empty whitespace",
        		"The user name cannot be empty whitespace"));
    }

    static class FieldUserNames {
    	@ValidUserName
    	String nullUserName = null;
    	
    	@ValidUserName
    	String invalidUserName = "invalid";
    	
    	@ValidUserName
    	String emptyUserName = "";
    	
    	@ValidUserName
    	String whiteSpaceUserName = " ";
    	
    	@ValidUserName
    	String validUserName = "valid";
    }
    
    @Service
    @Validated
    static class MethodUserNames {
    	@ValidUserName
	    String nullUserName() {
	        return null;
	    }

    	@ValidUserName
	    String invalidUserName() {
	        return "invalid";
	    }

    	@ValidUserName
	    String emptyUserName() {
	        return "";
	    }

    	@ValidUserName
	    String whiteSpaceUserName() {
	        return " ";
	    }
    	
    	@ValidUserName
	    String validUserName() {
	        return "valid";
	    }
    }
    
    static class TypeUserNames {
    	@Valid
        private List<@ValidUserName String> list = Arrays.asList(null, "invalid", "", " ", "valid");

    }
}