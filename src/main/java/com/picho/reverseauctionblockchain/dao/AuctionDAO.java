package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.GoodService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionDAO extends JpaRepository<Auction,Long> {
    @Query("select a from Auction a where a.code=:code")
    Auction findByCode(@Param("code") String code);
}
