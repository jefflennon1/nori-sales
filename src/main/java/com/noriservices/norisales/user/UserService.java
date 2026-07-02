package com.noriservices.norisales.user;

import com.noriservices.norisales.user.dto.RegisterUserDTO;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public static @Nullable User extractLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User save(RegisterUserDTO dto){
     if(userRepository.findByUsername(dto.username()) != null) throw  new RuntimeException("User already exists");
     String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
     User user = new User(dto.username(), dto.name(), dto.email(), encryptedPassword, UserRole.BUYER);
     user.setActive(true);
     user.setCreatedAt(LocalDateTime.now());
     user.setUpdatedAt(LocalDateTime.now());

     return this.userRepository.save(user);
    }
}
