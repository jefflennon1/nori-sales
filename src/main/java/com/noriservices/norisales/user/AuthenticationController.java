package com.noriservices.norisales.user;

import com.noriservices.norisales.user.dto.AuthenticationDTO;
import com.noriservices.norisales.user.dto.RegisterUserDTO;
import com.noriservices.norisales.user.dto.ResponseUserDTO;
import com.noriservices.norisales.user.dto.TokenResponseDTO;
import com.noriservices.norisales.shared.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import java.util.Objects;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService  jwtService;
    private final UserMapper mapper;


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword) ;

        var token = jwtService.generateToken((User) Objects.requireNonNull(auth.getPrincipal()));

        return ResponseEntity.ok().body(new TokenResponseDTO(token));
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseUserDTO> register(@RequestBody @Valid RegisterUserDTO data){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(userService.save(data)));
    }
}
