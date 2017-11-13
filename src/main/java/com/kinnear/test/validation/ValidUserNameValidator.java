package com.kinnear.test.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.kinnear.test.service.IUserUtils;

public class ValidUserNameValidator implements ConstraintValidator<ValidUserName, String> {

    private IUserUtils userUtils;
    
    public ValidUserNameValidator(IUserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @Override
	public void initialize(ValidUserName constraint) {
    }

    @Override
	public boolean isValid(String userName, ConstraintValidatorContext context) {
    	if (userName == null) {
    		//disable existing violation message
    	    context.disableDefaultConstraintViolation();
    	    //build new violation message and add it
    	    context.buildConstraintViolationWithTemplate("The user name cannot be null").addConstraintViolation();    	    
    		return false;
    	}
    	
    	if ("".equals(userName.trim())) {
    		//disable existing violation message
    	    context.disableDefaultConstraintViolation();
    	    //build new violation message and add it
    	    context.buildConstraintViolationWithTemplate("The user name cannot be empty whitepace").addConstraintViolation();    	    
    		return false;
    	}
    	
        return userUtils.isUserNameValid(userName);
    }

}
