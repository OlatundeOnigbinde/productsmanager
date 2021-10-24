package com.lpg.pmapp.controller;

import com.lpg.pmapp.service.UsersService;
import com.lpg.pmapp.vo.LoginRequest;
import com.lpg.pmapp.vo.RegistrationRequest;
import com.lpg.pmapp.vo.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    @Autowired
    UsersService us;

    @PostMapping(value = "/login", consumes = {"application/json", "text/plain;charset=UTF-8"}, produces = {"application/json"})
    public ResponseEntity<ServiceResponse> login(@RequestBody LoginRequest req) {
        try {
            ServiceResponse response = us.login(req.getEmail(), req.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Bad request was sent. Reason: " + ex.getMessage()));
        }
    }

    @PostMapping(value = "/register", consumes = {"application/json", "text/plain;charset=UTF-8"}, produces = {"application/json"})
    public ResponseEntity<ServiceResponse> register(@RequestBody RegistrationRequest req) {
        try {
            ServiceResponse response = us.userRegistration(req.getFirstname(), req.getLastname(), req.getPhoneNumber(), req.getEmail(), req.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return ResponseEntity.badRequest().body(new ServiceResponse(ServiceResponse.FAILED_CODE, "Bad request was sent. Reason: " + ex.getMessage()));
        }
    }
}
