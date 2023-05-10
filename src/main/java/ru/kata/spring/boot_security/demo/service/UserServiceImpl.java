package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    public UserServiceImpl(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }



    @Transactional
    public boolean add(User user) {
        User userPrimary = userDao.findByName(user.getUsername());
        if(userPrimary != null) {return false;}
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        userDao.add(user);
        return true;
    }

    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Transactional
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Transactional
    public void update(User user) {
        User userPrimary = findById(user.getId());
        System.out.println(userPrimary);
        System.out.println(user);
        if(!userPrimary.getPassword().equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        }
        userDao.update(user);
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public User findByUsername(String userName) {
        return userDao.findByName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userPrimary = Optional.ofNullable(userDao.findByName(username));
        if (!userPrimary.isPresent()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        UserDetails user = new org.springframework.security.core.userdetails.User(userPrimary.get().getUsername(),
                userPrimary.get().getPassword(), ath(userPrimary.get().getRoles()));
        return userPrimary.get();
    }

    private Collection<? extends GrantedAuthority> ath(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole()))
                .collect(Collectors.toList());
    }
}
