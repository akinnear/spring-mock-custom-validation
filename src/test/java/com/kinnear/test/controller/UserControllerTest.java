package com.kinnear.test.controller;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import com.kinnear.test.model.User;
import com.kinnear.test.service.IUserUtils;
import com.kinnear.test.validation.OverwriteValidatorConstraintsValidatorFactory;
import com.kinnear.test.validation.ValidUserNameValidator;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
    @MockBean
    private IUserUtils userUtils;
    
    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;
    
    @Autowired
    private WebApplicationContext applicationContext;
        
	@DirtiesContext // dirties it by adding item to LocalValidatorFactoryBean
	@Test
	public void validUserNameMockingValidator() throws Exception {
		//given
		String userName = "valid";
		User user = new User(userName);
		when(userUtils.isUserNameValid(eq(userName))).thenReturn(true);
		when(userUtils.isUserNameValid(argThat(not(userName)))).thenReturn(false);
		when(userUtils.addUser(userName)).thenReturn(user);
		
		//when
		// need to use the injected mockmvc because it needs to use the mocked userUtils
		ResultActions result = mockMvc.perform(post("/user/add").content(userName));
				
		// then
		result.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", equalTo(userName)));
		
		verify(userUtils, times(1)).isUserNameValid(userName);
		verify(userUtils, times(1)).addUser(userName);
        verifyNoMoreInteractions(userUtils);
	}
	
	@DirtiesContext // dirties it by adding item to LocalValidatorFactoryBean
	@Test
	public void invalidUserNameMockingValidator() throws Exception {
		//given
		String userName = "invalid";
		when(userUtils.isUserNameValid(anyString())).thenReturn(false);
		
		//when
		// need to use the injected mockmvc because it needs to use the mocked userUtils
		ResultActions result = mockMvc.perform(post("/user/add").content(userName));
				
		// then
		result.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$", equalTo("The user name '"+userName+"' is not valid")));
		
		verify(userUtils, times(1)).isUserNameValid(userName);
        verifyNoMoreInteractions(userUtils);
	}
	
	@DirtiesContext
	@Test
	public void validUserNameMockValidatorAnnotation() throws Exception {
		//given
		String userName = "valid";
		User user = new User(userName);
		
		// fake out the validator
		ValidUserNameValidator userNameValidator = mock(ValidUserNameValidator.class);
		when(userNameValidator.isValid(eq(userName), anyObject())).thenReturn(true);
		when(userNameValidator.isValid(argThat(not(userName)), anyObject())).thenReturn(false);
		when(userUtils.addUser(userName)).thenReturn(user);
		
		// set a constraint factory which will grab the mock from it
		// this dirties the context so we need @DirtiesContext to reset it
		localValidatorFactoryBean.setConstraintValidatorFactory(
			new OverwriteValidatorConstraintsValidatorFactory(applicationContext, userNameValidator));
		localValidatorFactoryBean.afterPropertiesSet();
				
		//when
		ResultActions result = mockMvc.perform(post("/user/add").content(userName));
				
		// then
		result.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", equalTo(userName)));
		
		verify(userUtils, times(1)).addUser(userName);
        verifyNoMoreInteractions(userUtils);
	}
	
	@DirtiesContext
	@Test
	public void invalidUserNameMockValidatorAnnotation() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		//given
		String userName = "invalid";
		
		// fake out the validator
		ValidUserNameValidator userNameValidator = mock(ValidUserNameValidator.class);
		when(userNameValidator.isValid(anyString(), anyObject())).thenReturn(false);
		
		// set a constraint factory which will grab the mock from it
		// this dirties the context so we need @DirtiesContext to reset it
		localValidatorFactoryBean.setConstraintValidatorFactory(
			new OverwriteValidatorConstraintsValidatorFactory(applicationContext, userNameValidator));
		localValidatorFactoryBean.afterPropertiesSet();
		
		//when
		ResultActions result = mockMvc.perform(post("/user/add").content(userName));
				
		// then
		result.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$", equalTo("The user name '"+userName+"' is not valid")));
		
        verifyZeroInteractions(userUtils);
	}
}
