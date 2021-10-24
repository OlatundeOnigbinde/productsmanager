package com.lpg.pmapp.service;

import com.lpg.pmapp.model.User;
import com.lpg.pmapp.model.repo.UserRepository;
import com.lpg.pmapp.util.Util;
import com.lpg.pmapp.vo.LoginResponse;
import com.lpg.pmapp.vo.ServiceResponse;
import jodd.util.Base64;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsersService {
    private static final Logger LOGGER = Logger.getLogger(UsersService.class.getName());

    @Autowired
    UserRepository userRepo;

    public ServiceResponse userRegistration(String firstname, String lastname, String phoneNumber,
                                            String email, String password){
        if (StringUtil.isBlank(firstname)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "First name is required");
        }
        if (StringUtil.isBlank(lastname)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Last name is required");
        }
        if (StringUtil.isBlank(email)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Email is required");
        }
        if (!email.contains("@")) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Email is not valid");
        }
        if (StringUtil.isBlank(password)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Password is required");
        }

        // check if this customer data exists
        Optional<User> customerOptional = userRepo.findByEmail(email);
        if (customerOptional.isPresent()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Profile with the email address " + customerOptional.get().getEmail() + " already exists.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        User user = new User();
        user.setActiveFlag(true);
        user.setCreateDate(new Date());
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(Util.hash(password));

        try {
            user = userRepo.save(user);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            return new ServiceResponse(ServiceResponse.FAILED_CODE, ServiceResponse.FAILED_MESSAGE);
        }
        return new ServiceResponse(ServiceResponse.SUCCESS_CODE, ServiceResponse.SUCCESS_MESSAGE + ". User registration has been completed successfully");
    }

    public ServiceResponse login(String email, String password){
        if (StringUtil.isBlank(email)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Email is required");
        }
        if (!email.contains("@")) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Email is not valid");
        }
        if (StringUtil.isBlank(password)) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Password is required");
        }

        // check if this user data exists
        Optional<User> userOp = userRepo.findByEmail(email);
        if (userOp.isEmpty()) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "No profile found with " + email);
        }
        User user = userOp.get();
        if (!Objects.equals(Util.hash(password), user.getPassword())) {
            return new ServiceResponse(ServiceResponse.FAILED_CODE, "Authentication failed");
        }

        LoginResponse response = new LoginResponse(ServiceResponse.SUCCESS_CODE, ServiceResponse.SUCCESS_MESSAGE);
        response.setEmail(user.getEmail());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setPhoneNumber(user.getPhoneNumber());
        user.setLastLoginDate(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(user.getId()).append("-").append(System.currentTimeMillis()).append("-").append(user.getEmail()).append("-").append(user.getPhoneNumber());
        String sessionToken = Base64.encodeToString(sb.toString());
        //redis.setSync(String.valueOf(user.getId()), session_token, sessionIdleDuration * 60);
        response.setSessionToken(sessionToken);
        user.setSessionToken(sessionToken);
        try {
            userRepo.save(user);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "", ex);
            return new ServiceResponse(ServiceResponse.FAILED_CODE, ServiceResponse.FAILED_MESSAGE);
        }

        return response;
    }
}
