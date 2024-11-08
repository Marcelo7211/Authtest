package com.ead.authuser.controller;

import com.ead.authuser.models.UserNotification;
import com.ead.authuser.services.UserNotificationService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users-notification")
@CrossOrigin(value = "*", maxAge = 3600)
public class UserNotificationController {

    @Autowired
    private UserNotificationService service;


    @GetMapping("/acorde")
    public String acorde(){
        return "Acordei";
    }

    @GetMapping
    public ResponseEntity<Page<UserNotification>> getAllNotification(@And({
                                                        @Spec(path = "date", spec = Equal.class),
                                                        @Spec(path = "content", spec = Like.class)
                                                   })
                                                   Specification<UserNotification> spec,
                                                              @PageableDefault(page = 0, size = 10, sort = "date", direction = Sort.Direction.DESC)
                                                   Pageable pageable) {

        Page<UserNotification> userPage = service.findAll(spec, pageable);



        return ResponseEntity.status(HttpStatus.OK).body(userPage);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserNotification> getOneNotification(@PathVariable(value = "uuid") UUID uuid){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(uuid));
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable(value = "uuid") UUID uuid) {
        service.deleteById(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("all/{uuid}")
    public ResponseEntity<?> delete( @RequestBody
                                     @Validated
                                     List<UserNotification> userNotification) {
        service.deleteAll(userNotification);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserNotification> updateNotification(@PathVariable(value = "uuid") UUID uuid,
                                              @RequestBody
                                              @Validated
                                              UserNotification userNotification) {

        return ResponseEntity.ok(service.put(uuid, userNotification));
    }

    @GetMapping("/user/{uuid}")
    public ResponseEntity<List<UserNotification>> getUserNotificationByUserId(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.ok(service.findAllByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<UserNotification> post(@RequestBody UserNotification userNotification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userNotification));
    }
}
