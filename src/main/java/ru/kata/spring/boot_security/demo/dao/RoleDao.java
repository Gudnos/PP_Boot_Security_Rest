package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    public boolean add(Role user);
    Role findByIdRole(Long id);
    Set<Role> listRoles();
    Role findByName(String name);
    Set<Role> listByName(List<String> name);
}
