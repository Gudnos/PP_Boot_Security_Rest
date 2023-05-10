package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    boolean addRole(Role role);

    Role findByNameRole(String name);

    Set<Role> listRoles();

    Role findByIdRole(Long id);

    Set<Role> listByRole(List<String> name);
}
