package com.noriservices.norisales.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<UserModel> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
