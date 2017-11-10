package com.kinnear.test.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class ValidLoginValidator implements ConstraintValidator<ValidLogin, String> {

    private IUserRepository userRepository;

    public ValidLoginValidator(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void initialize(ValidLogin constraint) {
    }

    public boolean isValid(String user, ConstraintValidatorContext context) {
        return user != null && userRepository.isUserValid(user);
    }

}
