package com.ead.authuser.services;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<User> findAll();
    User findById(UUID uuid);
    void delete(UUID uuid);
    User save(User userModel);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    User putUser (UUID uuid, UserDto userDto);
    User putPassword (UUID uuid, UserDto userDto);
    User putImage (UUID uuid, UserDto userDto);
    Page<User> findAllPageable (Specification<User> spec, Pageable pageable);
    User changeUserStatus(String email, UserStatus userStatus);
    User changeUserType (String email, RoleType userType);
}
