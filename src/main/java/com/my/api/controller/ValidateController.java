package com.my.api.controller;

import com.my.jwt.JwtTokenUtil;
import com.my.jwt.model.JwtRequest;
import com.my.jwt.model.JwtResponse;
import com.my.jwt.service.JwtUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Getter
@Setter
@RestController
@Api(tags = "驗證授權 操作", value = "驗證授權 操作 API 說明")
public class ValidateController {

    private static final Logger logger = LoggerFactory.getLogger(ValidateController.class);

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @ApiOperation(value = "驗證")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 400, message = "失敗")
    })
    @PostMapping("/validate")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtRequest jwtRequest) throws Exception {
        authenticate(jwtRequest.getAccount(), jwtRequest.getPassword());
        final UserDetails userDetails = getUserDetailsService().loadUserByUsername(jwtRequest.getAccount());
        final String token = getJwtTokenUtil().generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String account, String password) throws Exception {
        try {
            getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(account, password)
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }


    }
}