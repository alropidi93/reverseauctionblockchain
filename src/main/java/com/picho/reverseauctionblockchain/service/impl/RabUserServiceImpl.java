package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.RabUserDAO;
import com.picho.reverseauctionblockchain.dao.RoleDAO;
import com.picho.reverseauctionblockchain.dto.RabUserRegistrationForm;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.model.Role;
import com.picho.reverseauctionblockchain.service.RabUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service @Transactional @Slf4j
public class RabUserServiceImpl implements RabUserService, UserDetailsService {

    @Autowired
    RabUserDAO rabUserDAO;

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public RabUser saveUser(RabUserRegistrationForm userForm) {
        RabUser user = new RabUser();
        user.setUsername(userForm.getUsername());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setFullname(userForm.getFullname());

        return rabUserDAO.save(user);

    }

    @Override
    public Role saveRole(Role role) {
        return roleDAO.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        RabUser user = rabUserDAO.findByUsername(username);
        Role role = roleDAO.findByName(roleName);
        user.getRoles().add(role);

    }

    @Override
    public RabUser getUser(String username) {
        return rabUserDAO.findByUsername(username);
    }

    @Override
    public List<RabUser> getUsers() {
        return rabUserDAO.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RabUser user = rabUserDAO.findByUsername(username);
        if (user ==  null){
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else{
            log.info("User found in the database: {}", username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }
}
