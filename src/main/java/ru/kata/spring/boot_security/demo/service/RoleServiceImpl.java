package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    public boolean addRole(Role role) {
        Role userPrimary = roleDao.findByName(role.getRole());
        if(userPrimary != null) {return false;}
        roleDao.add(role);
        return true;
    }

    public Role findByNameRole(String name) { return roleDao.findByName(name); }

    public Set<Role> listRoles() { return roleDao.listRoles(); }

    public Role findByIdRole(Long id) {
        return roleDao.findByIdRole(id);
    }

    public Set<Role> listByRole(List<String> name) {
        return roleDao.listByName(name);
    }}
