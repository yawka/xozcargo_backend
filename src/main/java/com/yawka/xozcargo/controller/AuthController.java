package com.yawka.xozcargo.controller;

import com.yawka.xozcargo.payload.*;
import com.yawka.xozcargo.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequestDTO signupRequestDTO) {
        String response = authService.registerUser(signupRequestDTO);
        if(response.equals("Created")) {
        return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        JwtResponseDTO jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/send-code-to-phone-number")
    public ResponseEntity<String> sendCodeToPhoneNumber(@RequestBody UserPhoneNumberDTO userPhoneNumberDTO) {
        String response = authService.sendCodeToPhoneNumber(userPhoneNumberDTO);
        if (response.equals("Client not found!")) {
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else if(response.equals("user exists")) {
            return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/send-code-to-phone-number-to-reset-password")
    public ResponseEntity<String> sendCodeToPhoneNumberToResetPassword(@RequestBody UserPhoneNumberDTO userPhoneNumberDTO) {
        String response = authService.sendCodeToPhoneNumberToResetPassword(userPhoneNumberDTO);
        if (response.equals("Client not found!")) {
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/verify-phone-number")
    public ResponseEntity<String> verifyPhoneNumber(@RequestBody UserPhoneNumberVerifyDTO userPhoneNumberVerifyDTO) {
        String response = authService.verifyPhoneNumber(userPhoneNumberVerifyDTO);
        if(response == "url connection error") {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
        String response = authService.resetPassword(resetPasswordRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //verify
    //resend-verification-code
    //forgot-password
    //reset-password

}


