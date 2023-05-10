package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean add(Role user) {
        entityManager.persist(user);
        return true;
    }

    public Role findByIdRole(Long id) {
        return entityManager.find(Role.class, id);
    }

    public Set<Role> listRoles() {
        List<Role> query = entityManager.createQuery("select s from Role s", Role.class).getResultList();
        return new HashSet<>(query);
    }

    public Role findByName(String name) {
        return entityManager.createQuery("select u from Role u where u.role = :id", Role.class)
                .setParameter("id", name)
                .getResultList().stream().findAny().orElse(null);
    }

    public Set<Role> listByName(List<String> name) {
        List<Role> query = entityManager.createQuery("select u from Role u where u.role in (:id)", Role.class)
                .setParameter("id", name)
                .getResultList();
        return new HashSet<>(query);
    }
}
