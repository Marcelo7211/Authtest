package com.ead.authuser.configs.security;

import com.ead.authuser.models.User;
import com.ead.authuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userModel = repository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username" + username));

        return UserDetailsImpl.build(userModel);
    }
}
