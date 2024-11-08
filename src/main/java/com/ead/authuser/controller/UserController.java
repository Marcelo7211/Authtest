package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.User;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static  org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static  org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService service;


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@And({
                                                        @Spec(path = "userType", spec = Equal.class),
                                                        @Spec(path = "userStatus", spec = Equal.class),
                                                        @Spec(path = "email", spec = Like.class)
                                                   })
                                                   Specification<User> spec,
                                                   @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC)
                                                   Pageable pageable) {

        Page<User> userPage = service.findAllPageable(spec, pageable);
        if(!userPage.isEmpty())
            for (User user: userPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUsers(user.getUserId())).withSelfRel());
            }

        return ResponseEntity.status(HttpStatus.OK).body(userPage);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR' , 'STUDENT')")
    @GetMapping("/{uuid}")
    public ResponseEntity<User> getOneUsers(@PathVariable(value = "uuid") UUID uuid){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(uuid));
    }

    @GetMapping("/change-status")
    public ResponseEntity<UserDto> changeStatus (@RequestParam(value = "email") String email,
                                                 @RequestParam(value = "userStatus") UserStatus userStatus) {
        User user = this.service.changeUserStatus(email, userStatus);
        var userDto = new UserDto();

        BeanUtils.copyProperties(user, userDto);

        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/change-type")
    public ResponseEntity<UserDto> changeType (@RequestParam(value = "email") String email,
                                                 @RequestParam(value = "userType") RoleType userType) {
        User user = this.service.changeUserType(email, userType);
        var userDto = new UserDto();

        BeanUtils.copyProperties(user, userDto);

        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable(value = "uuid") UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR' , 'STUDENT')")
    @PutMapping("/{uuid}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "uuid") UUID uuid,
                                              @RequestBody
                                              @Validated({UserDto.UserView.UserPut.class})
                                              @JsonView({UserDto.UserView.UserPut.class})
                                              UserDto user) {
        var userDto = new UserDto();
        BeanUtils.copyProperties(service.putUser(uuid, user), userDto);

        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR' , 'STUDENT')")
    @PutMapping("/password/{uuid}")
    public ResponseEntity<UserDto> updatePassword(@PathVariable(value = "uuid") UUID uuid,
                                                  @RequestBody
                                                  @Validated({UserDto.UserView.PasswordPut.class})
                                                  @JsonView({UserDto.UserView.PasswordPut.class})
                                                  UserDto user) {
        var userDto = new UserDto();
        BeanUtils.copyProperties(service.putPassword(uuid, user), userDto);

        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR' , 'STUDENT')")
    @PutMapping("/image/{uuid}")
    public ResponseEntity<UserDto> updateImage(@PathVariable(value = "uuid") UUID uuid,
                                               @RequestBody
                                               @Validated({UserDto.UserView.ImagePut.class})
                                               @JsonView({UserDto.UserView.ImagePut.class})
                                               UserDto user) {
        var userDto = new UserDto();
        BeanUtils.copyProperties(service.putImage(uuid, user), userDto);

        return ResponseEntity.ok(userDto);
    }

}
