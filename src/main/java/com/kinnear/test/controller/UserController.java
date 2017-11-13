package com.kinnear.test.controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kinnear.test.model.User;
import com.kinnear.test.service.IUserUtils;
import com.kinnear.test.validation.ValidUserName;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
	private IUserUtils userUtils;
	
	@Autowired
    public UserController(IUserUtils userUtils) {
		this.userUtils = userUtils;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public User addUser(@ValidUserName @RequestBody String userName) {
        User user = userUtils.addUser(userName);
        return user;
    }
	
	@ExceptionHandler(value = {ConstraintViolationException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleValidationFailure(ConstraintViolationException ex) {

	    StringBuilder messages = new StringBuilder();

	    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
	        messages.append(violation.getMessage() + "\n");
	    }

	    return messages.toString();
	}
}