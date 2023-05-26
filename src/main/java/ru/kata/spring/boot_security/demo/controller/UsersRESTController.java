package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UsersRESTController {
    private final UserService userService;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    @Autowired
    public UsersRESTController(UserService userService, RoleService roleService, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public List<UserDTO> findAll() {
        return userService.findAll().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User с ID = " + id + " не найден");
        }
        return convertToUserDTO(user);
    }

    @PostMapping("/users")
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.create(convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    

    @PatchMapping("/users/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User с ID = " + id + " не найден");
        }
        userService.update(id, convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User с ID = " + id + " не найден");
        }
        userService.delete(id);
        return "User с ID = " + id + " удален";
    }

    @GetMapping("/viewUser")
    public UserDTO showUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return convertToUserDTO(user);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.listRoles(), HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    ResponseEntity<Role> getRoleById(@PathVariable("id") long id){
        return new ResponseEntity<>(roleService.getRoleById(id), HttpStatus.OK);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }


}
