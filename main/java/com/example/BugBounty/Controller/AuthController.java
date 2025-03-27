package com.example.BugBounty.Controller;


import com.example.BugBounty.Jwt.JwtResponse;
import com.example.BugBounty.Jwt.LoginRequest;
import com.example.BugBounty.Jwt.MessageResponse;
import com.example.BugBounty.Jwt.SignupRequest;
import com.example.BugBounty.Repository.UserRepository;
import com.example.BugBounty.Security.JwtUtils;
import com.example.BugBounty.Services.SmtpGmailSenderService;
import com.example.BugBounty.model.User;
import com.example.BugBounty.payload.OtpRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ConcurrentHashMap<String, String> otpMap = new ConcurrentHashMap<>();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SmtpGmailSenderService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        String otp = generateOtp();
        otpMap.put(signupRequest.getEmail(), otp);

        emailService.sendEmail(signupRequest.getEmail(), "TechBoost OTP Verification", "Your OTP is: " + otp);

        return ResponseEntity.ok(new MessageResponse("OTP sent to your email. Please verify"));

        // Create new user's account
//        User user = new User();
//        user.setFullName(signupRequest.getFullName());
//        user.setEmail(signupRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
//
//        Set<String> roles = new HashSet<>();
//        roles.add("USER");
//        user.setRoles(roles);
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody OtpRequest otpRequest) {
        System.out.println("Received OTP request: " + otpRequest);
        String storedOtp = otpMap.get(otpRequest.getEmail());

        if (storedOtp == null || !storedOtp.equals(otpRequest.getOtp())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid OTP!"));
        }

        // Create new user's account
        User user = new User();
        user.setFullName(otpRequest.getFullName());
        user.setEmail(otpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(otpRequest.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        userRepository.save(user);

        otpMap.remove(otpRequest.getEmail());

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));


    }

    private String generateOtp() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}