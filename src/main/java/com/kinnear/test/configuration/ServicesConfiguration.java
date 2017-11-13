package com.kinnear.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kinnear.test.model.User;
import com.kinnear.test.service.IUserUtils;

@Configuration
public class ServicesConfiguration {
	@Bean
	public IUserUtils userUtils() {
		return new IUserUtils() {
			
			@Override
			public boolean isUserNameValid(String userName) {
				return true;
			}
			
			@Override
			public User addUser(String userName) {
				return new User(userName);
			}
		};
	}
}
