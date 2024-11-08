package com.ead.authuser.services.impl;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.Role;
import com.ead.authuser.models.User;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleService roleService;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(UUID uuid) {
        return userExists(uuid);
    }

    @Override
    public void delete(UUID uuid) {
        userExists(uuid);
        repository.deleteById(uuid);
    }

    @Override
    public User save(User userModel) {
        return repository.save(userModel);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    @Override
    public Optional<User> findByEmail(String userName) {
        return repository.findByEmail(userName);
    }

    @Override
    public User putUser(UUID uuid, UserDto userDto) {

        var userModel = this.userExists(uuid);
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return repository.save(userModel);
    }

    @Override
    public User putPassword(UUID uuid, UserDto userDto) {

        var userModel = this.userExists(uuid);

        if(!userDto.getOldPassword().equals(userModel.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OldPassword is not correct");

        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return repository.save(userModel);
    }

    @Override
    public User putImage(UUID uuid, UserDto userDto) {

        var userModel = this.userExists(uuid);
        userModel.setImageUrl(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return repository.save(userModel);
    }

    @Override
    public Page<User> findAllPageable(Specification<User> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    @Override
    public User changeUserStatus(String email, UserStatus userStatus) {
        Optional<User> user = this.findByEmail(email);

        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found try outher");

        user.get().setUserStatus(userStatus);
        user.get().setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return repository.save(user.get());
    }

    @Override
    public User changeUserType(String email, RoleType userType) {
        Optional<User> user = this.findByEmail(email);

        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found try outher");

        Role role = roleService.findByRoleName(userType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));

        user.get().getRoles().add(role);
        user.get().setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return repository.save(user.get());
    }

    private User userExists(UUID uuid) {

        if(uuid == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uuid is not be null");

        Optional<User> user = repository.findById(uuid);

        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found");

        return user.get();
    }
}
