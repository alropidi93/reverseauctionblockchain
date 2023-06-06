package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemDAO extends JpaRepository<AuctionItem,Long> {
}
