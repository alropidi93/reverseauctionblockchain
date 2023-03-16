package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.RabUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RabUserDAO extends JpaRepository<RabUser,Long> {
    RabUser findByUsername(String username);
}

