package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.AuctionBidder;
import com.picho.reverseauctionblockchain.model.AuctionBidderEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionBidderDAO extends JpaRepository<AuctionBidder, AuctionBidderEntityId> {
}
