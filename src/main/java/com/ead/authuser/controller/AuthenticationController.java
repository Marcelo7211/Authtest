package com.ead.authuser.controller;


import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.dto.JwtDto;
import com.ead.authuser.dto.LoginDto;
import com.ead.authuser.dto.UserDto;
import com.ead.authuser.dto.UserResponseDto;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.models.Role;
import com.ead.authuser.models.User;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;


@RestController
@RequestMapping("/auth")
@CrossOrigin(value = "*", maxAge = 3600)
public class AuthenticationController {

    @Autowired
    private UserService service;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtProvider jwtAuthenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> registerUser(@RequestBody
                                                @Validated({UserDto.UserView.RegistrationPost.class})
                                                @JsonView({UserDto.UserView.RegistrationPost.class})
                                                UserDto userDto){

        if(service.findByUserName(userDto.getUserName()).isPresent()){
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "Username ja existente!");
        }

        if(service.findByEmail(userDto.getEmail()).isPresent()){
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe um usuário com este email");
        }



        Role roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role Studant not found."));

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var userModel = new User();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.DEBIT);
        userModel.getRoles().add(roleModel);
        userModel.setCreattionDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        var userModelResp = service.save(userModel);
        BeanUtils.copyProperties(userModelResp, userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = jwtProvider.authenticateUser(loginDto.getUsername(), loginDto.getPassword());
        String token = jwtProvider.generateJwt(authentication);

        return ResponseEntity.ok(new JwtDto(token));
    }

    @GetMapping("user-by-email/{email}")
    public ResponseEntity<UserResponseDto> getUuidByEmail(@PathVariable(value = "email") String email) {
        
        User user = service.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with this email not found"));
        
        var userResp = new UserResponseDto(user.getUserId());

        return ResponseEntity.ok(userResp);
    }
}


