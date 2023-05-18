package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    void create(User user);

    List<User> findAll();

    User findOne(long id);

    void update(long id, User updateUser);

    void delete(long id);

    Set<Role> getSetOfRoles(List<String> rolesId);

    User findByUsername(String username);

    UserDetails loadUserByUsername(String username);
}
