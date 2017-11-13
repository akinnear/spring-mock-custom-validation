package com.kinnear.test.service;

import com.kinnear.test.model.User;

public interface IUserUtils {
	boolean isUserNameValid(String userName);
	
	User addUser(String userName);
}
