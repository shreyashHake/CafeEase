package com.cafe.serviceIml;

import com.cafe.JWT.CustomerUserDetailsService;
import com.cafe.JWT.JwtUtil;
import com.cafe.constants.CafeConstants;
import com.cafe.dao.UserDao;
import com.cafe.model.User;
import com.cafe.service.UserService;
import com.cafe.util.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    // the sign up code
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside singUp: {}", requestMap);
        try {
            if (validateSingUpRequestMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResoponseEntity("Registered successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResoponseEntity("Email already exits", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResoponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResoponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public boolean validateSingUpRequestMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("password");
    }

    public User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    // the login code
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login method");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>("{\"token\":\"" + jwtUtil.generateToken(
                            customerUserDetailsService.getUserDetail().getEmail(),
                            customerUserDetailsService.getUserDetail().getRole()
                    ) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("{\"Message\" : \"Wait for admin approval\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"Message\" : \"Bad Credentials\"}", HttpStatus.BAD_REQUEST);
    }

}
