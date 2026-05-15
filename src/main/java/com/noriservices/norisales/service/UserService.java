package com.noriservices.norisales.service;

import com.noriservices.norisales.model.UserModel;
import com.noriservices.norisales.repository.UserRepository;
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
