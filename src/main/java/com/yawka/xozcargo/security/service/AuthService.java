package com.yawka.xozcargo.security.service;

import com.yawka.xozcargo.entity.Client;
import com.yawka.xozcargo.entity.Role;
import com.yawka.xozcargo.entity.User;
import com.yawka.xozcargo.entity.enums.Roles;
import com.yawka.xozcargo.exception.RoleNotFoundException;
import com.yawka.xozcargo.exception.UserNotFoundException;
import com.yawka.xozcargo.payload.*;
import com.yawka.xozcargo.repository.ClientRepository;
import com.yawka.xozcargo.repository.RoleRepository;
import com.yawka.xozcargo.repository.UserRepository;
import com.yawka.xozcargo.security.jwt.JwtUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    AuthenticationManager authenticationManager;

    //private final VerificationTokenRepository verificationTokenRepository;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    public AuthService(UserRepository userRepository, ClientRepository clientRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = user.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());


        return new JwtResponseDTO(jwt,
                user.getId(),
                user.getUsername(),
                user.getClient(),
                roles);
    }


    public String registerUser(SignupRequestDTO signUpRequest) {
        Client client = clientRepository.findByPhoneNumber(signUpRequest.getUsername());
        if (client == null) {
            // throw new RoleNotFoundException("Клиент с таким номером у нас не существует!");
            return "Вы не можете у нас зарегистрироваться. Клиент с таким номером у нас не существует!";
        }
        User user = new User(signUpRequest.getUsername(),
                client,
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "developer" -> {
                        Role modRole = roleRepository.findByName(Roles.ROLE_DEVELOPER)
                                .orElseThrow(() -> new RoleNotFoundException("Роль не найдена"));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                                .orElseThrow(() -> new RoleNotFoundException("Роль не найдена"));
                        roles.add(userRole);
                    }
                }
            });
        }

        user.setRoles(roles);
        user.setEnabled(true);
        userRepository.save(user);

        return "Created";
    }

    public String sendCodeToPhoneNumber(UserPhoneNumberDTO userPhoneNumberDTO) {
        String phoneNumber = userPhoneNumberDTO.getPhoneNumber();
        Client client = clientRepository.findByPhoneNumber(phoneNumber);
        if (client == null) {
            return "Client not found!";
        }
        Optional<User> user = userRepository.findByClient(client);
        if (user.isPresent()) {
            return "user exists";
        }
        String status = "";

        try {
            URL url = new URL("https://smspro.nikita.kg/api/otp/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            conn.setRequestMethod("POST");
            // Set the request content type to application/json
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            // Set the request header "X-API-KEY"
            conn.setRequestProperty("X-API-KEY", "a85afdcb6752de9cc2bd7adfd1fe1ad1");
            // Enable input and output streams
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // JSON payload
            String transactionId = UUID.randomUUID().toString();
            String jsonInputString = String.format("{ \"transaction_id\": \"%s\", \"phone\": \"%s\" }", transactionId, phoneNumber);
            // Write the JSON payload to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                e.printStackTrace();
                status = "error when reading a Json File";
            }

            // Get the response Json
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            System.out.println("Response content: " + content.toString());
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            status = "url connection error";
        }

        return status;
    }

    public String sendCodeToPhoneNumberToResetPassword(UserPhoneNumberDTO userPhoneNumberDTO) {
        String phoneNumber = userPhoneNumberDTO.getPhoneNumber();
        Client client = clientRepository.findByPhoneNumber(phoneNumber);
        if (client == null) {
            return "Client not found!";
        }
        String status = "";
        Optional<User> user = userRepository.findByClient(client);
        if (user.isPresent()) {

            try {
                URL url = new URL("https://smspro.nikita.kg/api/otp/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                conn.setRequestMethod("POST");
                // Set the request content type to application/json
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                // Set the request header "X-API-KEY"
                conn.setRequestProperty("X-API-KEY", "a85afdcb6752de9cc2bd7adfd1fe1ad1");
                // Enable input and output streams
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // JSON payload
                String transactionId = UUID.randomUUID().toString();
                String jsonInputString = String.format("{ \"transaction_id\": \"%s\", \"phone\": \"%s\" }", transactionId, phoneNumber);
                // Write the JSON payload to the output stream
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (Exception e) {
                    e.printStackTrace();
                    status = "error when reading a Json File";
                }

                // Get the response Json
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();
                System.out.println("Response content: " + content.toString());
                return content.toString();
            } catch (Exception e) {
                e.printStackTrace();
                status = "url connection error";
            }
        }

        return status;
    }

    public String verifyPhoneNumber(UserPhoneNumberVerifyDTO userPhoneNumberVerifyDTO) {
        String code = userPhoneNumberVerifyDTO.getCode();
        String token = userPhoneNumberVerifyDTO.getToken();
        String status = "";
        try {
            URL url = new URL("https://smspro.nikita.kg/api/otp/verify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            conn.setRequestMethod("POST");
            // Set the request content type to application/json
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            // Set the request header "X-API-KEY"
            conn.setRequestProperty("X-API-KEY", "a85afdcb6752de9cc2bd7adfd1fe1ad1");
            // Enable input and output streams
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // JSON payload
            String jsonInputString = String.format("{ \"code\": \"%s\", \"token\": \"%s\" }", code, token);
            // Write the JSON payload to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                e.printStackTrace();
                status = "error when reading a Json File";
            }

            // Get the response Json
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            System.out.println("Response content: " + content.toString());
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            status = "url connection error";
        }

        return status;
    }

    public String resetPassword(ResetPasswordRequestDTO resetPasswordRequestTokenDTO) {
        User user = userRepository.findByUsername(resetPasswordRequestTokenDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким номером не найден"));

        user.setPassword(encoder.encode(resetPasswordRequestTokenDTO.getPassword()));
        userRepository.save(user);
        return "updated";
    }
//
//    public boolean verifyResetPasswordToken(VerificationRequest verificationRequest) {
//        VerificationToken verificationToken = verificationTokenRepository.getByCode(verificationRequest.getCode());
//        if (verificationToken == null ) {
//            return false;
//        }
//        if (new Date().after(verificationToken.getExpiryDate())) {
//            return false;
//        }
//        verificationTokenRepository.delete(verificationToken);
//        return true;
//    }

}