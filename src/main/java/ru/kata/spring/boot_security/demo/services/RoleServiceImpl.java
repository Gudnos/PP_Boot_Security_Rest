package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RolesRepository;


import java.util.List;
import java.util.Set;
@Service
public class RoleServiceImpl implements RoleService{

    private final RolesRepository rolesRepository;

    public RoleServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Set<Role> listRoles() {
        return rolesRepository.listRoles();
    }

    public Set<Role> listByRole(List<String> name) {
        return rolesRepository.listByRole(name);
    }
}
