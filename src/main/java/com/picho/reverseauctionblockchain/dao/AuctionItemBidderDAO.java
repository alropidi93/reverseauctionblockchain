package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.AuctionItemBidder;
import com.picho.reverseauctionblockchain.model.AuctionItemBidderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemBidderDAO extends JpaRepository<AuctionItemBidder, AuctionItemBidderId> {
}

