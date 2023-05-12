package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.MyUserDetailsService;

@Component
public class UserValidator implements Validator {

    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public UserValidator(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            myUserDetailsService.loadUserByUsername(user.getUsername());
        }catch (UsernameNotFoundException ignored) {
            return; // всё OK, пользователь не найден
        }
        errors.rejectValue("username", "", "Username уже существует");

    }
}
