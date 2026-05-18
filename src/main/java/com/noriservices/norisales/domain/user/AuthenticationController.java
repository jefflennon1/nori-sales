package com.noriservices.norisales.domain.user;

import com.noriservices.norisales.domain.user.dto.AuthenticationDTO;
import com.noriservices.norisales.domain.user.dto.RegisterUserDTO;
import com.noriservices.norisales.domain.user.dto.ResponseUserDTO;
import com.noriservices.norisales.domain.user.dto.TokenResponseDTO;
import com.noriservices.norisales.infra.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService  jwtService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword) ;

        var token = jwtService.generateToken((UserModel) Objects.requireNonNull(auth.getPrincipal()));

        return ResponseEntity.ok().body(new TokenResponseDTO(token));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterUserDTO data){
        if(this.userRepository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        UserModel user = new UserModel(data.username(), data.name(), data.email(), encryptedPassword, data.role());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        this.userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseUserDTO(user.getUsername(), user.getEmail(), user.getRole(), user.isEnabled()));
    }
}
