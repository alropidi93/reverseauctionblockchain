package com.picho.reverseauctionblockchain.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.picho.reverseauctionblockchain.dto.RabUserRegistrationForm;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.model.Role;
import com.picho.reverseauctionblockchain.service.RabUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class RabUserController {

    @Autowired
    RabUserService rabUserService;


    @GetMapping("/test")
    public ResponseEntity<Object> test(){
        return ResponseEntity.ok().body("prueba 100 jeje".toString());
    }

    @GetMapping("/users")
    public ResponseEntity<List<RabUser>>listUsers(){
        return ResponseEntity.ok().body(rabUserService.getUsers());
    }

    @CrossOrigin
    @PostMapping(value = "/user/save")
    public ResponseEntity<RabUser>saveUser(@RequestBody RabUserRegistrationForm userForm){
        System.out.println("En save user");
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(rabUserService.saveUser(userForm));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(rabUserService.saveRole(role));
    }

    @PostMapping("/user/test")
    public ResponseEntity<String>addRole(){
        System.out.println("testinggg");
        return ResponseEntity.ok().body("Test");
    }

    @PostMapping("/user/addRole")
    public ResponseEntity<String>addRole(@RequestBody RoleToUserForm roleToUserForm){
        rabUserService.addRoleToUser(roleToUserForm.getUsername(),roleToUserForm.getRoleName());
        return ResponseEntity.ok().body("Rol agregado a usuario");
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader =  request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                RabUser user = rabUserService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            }
            catch (Exception exception){

                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }

    }




}

@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}
