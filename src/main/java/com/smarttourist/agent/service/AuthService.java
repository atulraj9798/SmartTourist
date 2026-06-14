package com.smarttourist.agent.service;

import com.smarttourist.agent.dto.AuthResponse;
import com.smarttourist.agent.dto.LoginRequest;
import com.smarttourist.agent.dto.RegisterRequest;
import com.smarttourist.agent.model.GovernmentIdDetail;
import com.smarttourist.agent.model.User;
import com.smarttourist.agent.repository.GovernmentIdDetailRepository;
import com.smarttourist.agent.repository.UserRepository;
import com.smarttourist.agent.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final GovernmentIdDetailRepository govIdRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    // In-memory cache for OTP codes: key = mobileNumber, value = OTP
    private final Map<String, String> otpCache = new ConcurrentHashMap<>();

    public AuthService(
            UserRepository userRepository,
            GovernmentIdDetailRepository govIdRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.govIdRepository = govIdRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public String generateAndSendOtp(String mobileNumber) {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(1000000));
        otpCache.put(mobileNumber, otp);

        // System output simulates SMS gateway notification
        System.out.println("=================================================");
        System.out.println("SMS SIMULATION GATEWAY to: " + mobileNumber);
        System.out.println("OTP CODE IS: " + otp);
        System.out.println("=================================================");

        return otp; // Return it so frontend/console can display it in mock environments
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        String cachedOtp = otpCache.get(mobileNumber);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            otpCache.remove(mobileNumber); // Consume OTP after successful verification
            return true;
        }
        return false;
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email address is already in use");
        }
        if (userRepository.existsByMobileNumber(request.mobileNumber())) {
            throw new IllegalArgumentException("Mobile number is already registered");
        }

        // Create User
        User user = new User(
                request.name(),
                request.email(),
                request.mobileNumber(),
                passwordEncoder.encode(request.password()),
                "USER"
        );
        User savedUser = userRepository.save(user);

        // Create Government ID detail
        GovernmentIdDetail idDetail = new GovernmentIdDetail(
                savedUser,
                request.idType(),
                request.idNumber(),
                "/assets/gov_id_" + savedUser.getId() + ".pdf" // Mock URL
        );
        idDetail.setIsVerified(true); // Auto-verify for mock purposes
        idDetail.setVerifiedAt(LocalDateTime.now());
        govIdRepository.save(idDetail);

        savedUser.setGovernmentIdDetail(idDetail);
        return savedUser;
    }

    public AuthResponse loginUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }
}
