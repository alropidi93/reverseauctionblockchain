package com.picho.reverseauctionblockchain.controller;

import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.model.Role;
import com.picho.reverseauctionblockchain.service.RabUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RabUserController {

    @Autowired
    RabUserService rabUserService;


    @GetMapping("/test")
    public ResponseEntity<Object> test(){
        return ResponseEntity.ok().body("prueba".toString());
    }

    @GetMapping("/users")
    public ResponseEntity<List<RabUser>>listUsers(){
        return ResponseEntity.ok().body(rabUserService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<RabUser>saveUser(@RequestBody RabUser user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(rabUserService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(rabUserService.saveRole(role));
    }

    @PostMapping("/user/addRole")
    public ResponseEntity<String>addRole(@RequestBody RoleToUserForm roleToUserForm){
        rabUserService.addRoleToUser(roleToUserForm.getUsername(),roleToUserForm.getRoleName());
        return ResponseEntity.ok().body("Rol agregado a usuario");
    }




}

@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}
