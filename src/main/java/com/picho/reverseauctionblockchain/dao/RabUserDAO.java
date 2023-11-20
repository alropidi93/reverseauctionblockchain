package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.BidderEntity;
import com.picho.reverseauctionblockchain.model.RabUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RabUserDAO extends JpaRepository<RabUser,Long> {
    RabUser findByUsername(String username);

    @Query("select ru from RabUser ru where ru.stateEntity.code = :code")
    RabUser findByStateEntityCode(@Param("code") String code);

    @Query("select ru from RabUser ru where ru.bidderEntity.code = :code")
    RabUser findByBidderEntityCode(@Param("code") String code);
}

