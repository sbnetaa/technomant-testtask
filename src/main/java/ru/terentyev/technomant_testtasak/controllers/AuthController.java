package ru.terentyev.technomant_testtasak.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.terentyev.technomant_testtasak.models.LoginRequest;
import ru.terentyev.technomant_testtasak.models.Person;
import ru.terentyev.technomant_testtasak.models.RegisterRequest;
import ru.terentyev.technomant_testtasak.repositories.RoleRepository;
import ru.terentyev.technomant_testtasak.services.JwtService;
import ru.terentyev.technomant_testtasak.services.UserDetailsServiceImpl;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(@Lazy AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
                          JwtService jwtService, PasswordEncoder passwordEncoder
            , RoleRepository roleRepository) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new RuntimeException("Passwords do not match");
        }

        Person person = new Person();
        person.setUsername(request.getUsername());
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        person.setRoles(Set.of(roleRepository.findByName("ROLE_USER")));
        userDetailsService.createUser(person);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/token")
    public ResponseEntity<String> token(@RequestBody LoginRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(jwtToken);

        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for user: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (NoSuchElementException e) {
            logger.error("User not found: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            logger.error("An unexpected error occurred for user: {}", authRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}