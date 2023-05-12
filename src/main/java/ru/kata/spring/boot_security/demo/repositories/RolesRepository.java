package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

@Repository
public interface RolesRepository {

    Set<Role> listRoles();

    Set<Role> listByRole(List<String> name);

}
