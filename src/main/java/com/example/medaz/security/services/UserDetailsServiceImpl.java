package com.example.medaz.security.services;

import com.example.medaz.entity.User;
import com.example.medaz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional // Important for loading related entities if needed (like roles)
    public UserDetails loadUserByUsername(String identificationNumber) throws UsernameNotFoundException {
        User user = userRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with identification number: " + identificationNumber));

        return UserDetailsImpl.build(user);
    }
}

