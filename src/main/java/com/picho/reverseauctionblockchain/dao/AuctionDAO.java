package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionDAO extends JpaRepository<Auction,Long> {
}
