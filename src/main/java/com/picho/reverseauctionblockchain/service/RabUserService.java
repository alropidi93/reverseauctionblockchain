package com.picho.reverseauctionblockchain.service;

import com.picho.reverseauctionblockchain.dto.RabUserRegistrationForm;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.model.Role;

import java.util.List;

public interface RabUserService {
    RabUser saveUser (RabUserRegistrationForm userForm);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    RabUser getUser(String username);
    List<RabUser> getUsers();
}
