package com.kinnear.test.user;

import java.util.Objects;

public class User {
	 
    @ValidLogin
    private String login;
 
    public User(String login) {
        Objects.requireNonNull(login);
        this.login = login;
    }
}