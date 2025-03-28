package com.ead.authuser.services.impl;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.models.Role;
import com.ead.authuser.repository.RoleRepository;
import com.ead.authuser.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Override
    public Optional<Role> findByRoleName(RoleType name) {
        return repository.findByRoleName(name);
    }
}
