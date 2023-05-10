package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean add(User user);
    List<User> listUsers();
    void delete(Long id);
    void update(User user);
    User findById(Long id);
    User findByUsername(String userName);
}
