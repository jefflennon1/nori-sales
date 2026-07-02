package com.noriservices.norisales.user;

import com.noriservices.norisales.shared.exception.UserAlreadyExistsException;
import com.noriservices.norisales.user.dto.RegisterUserDTO;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static @Nullable User extractLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User save(RegisterUserDTO dto){
     if(userRepository.findByUsername(dto.username()) != null) throw  new UserAlreadyExistsException("Username already in use");
     if(userRepository.findByEmail(dto.email()).isPresent()) throw  new UserAlreadyExistsException("Email already in use");
     String encryptedPassword = passwordEncoder.encode(dto.password());
     User user = new User(dto.username(), dto.name(), dto.email(), encryptedPassword, UserRole.BUYER);
     user.setActive(true);

     return this.userRepository.save(user);
    }
}
