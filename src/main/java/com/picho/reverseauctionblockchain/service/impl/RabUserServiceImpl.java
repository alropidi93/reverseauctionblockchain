package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.RabUserDAO;
import com.picho.reverseauctionblockchain.dao.RoleDAO;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.model.Role;
import com.picho.reverseauctionblockchain.service.RabUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional
public class RabUserServiceImpl implements RabUserService {

    @Autowired
    RabUserDAO rabUserDAO;

    @Autowired
    RoleDAO roleDAO;

    @Override
    public RabUser saveUser(RabUser user) {

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
}
