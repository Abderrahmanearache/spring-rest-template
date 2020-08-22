package net.armane.ar9am.controller;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.armane.ar9am.Security.Resources;
import net.armane.ar9am.Security.RequestForm.LoginForm;
import net.armane.ar9am.Security.RequestForm.RegisterForm;
import net.armane.ar9am.Security.ResponseForm.UserResponse;
import net.armane.ar9am.Security.account.RoleValue;
import net.armane.ar9am.Security.account.User;
import net.armane.ar9am.service.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    AccountManager accountManager;
    @Autowired
    AuthenticationManager authManager;


    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterForm form) {

//        to be chaged
//        Validation Exising

        if (!form.getPassword().equals(form.getRepassword()))
            throw new RuntimeException("passwords not match");

        if (accountManager.findUserByUsername(form.getUsername()) != null)
            throw new RuntimeException("username alreay exist !!");
/*
        if (accountManager.findUserByEmail(form.getEmail()) != null)
            throw new RuntimeException("email alreay exist !!");
*/

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setEmail(form.getEmail());

        accountManager.saveUser(user);

        // if is important
        accountManager.addRoleToUser(user, RoleValue.STUDENT);
        return login(new LoginForm(form.getUsername(), form.getPassword()));
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginForm loginRequest) {


        Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                auth.getPrincipal();

        String jwttoken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Resources.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Resources.JWT_SECRET)
                .claim("roles", user.getAuthorities())
                .compact();

        Collection<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User userdata = accountManager.findUserByUsername(loginRequest.getUsername());

        UserResponse userResponse = new UserResponse(userdata.getId(),
                userdata.getUsername(), userdata.getEmail(), roles, Resources.TOKEN_PREFIX + jwttoken);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


}
