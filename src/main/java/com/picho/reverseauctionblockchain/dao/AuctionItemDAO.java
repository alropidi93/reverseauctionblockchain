package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionItemDAO extends JpaRepository<AuctionItem,Long> {

    @Query("select ai from AuctionItem ai where ai.auction.code = :auctionCode")
    AuctionItem findByAuctionCode(@Param("auctionCode") String auctionCode);

    @Query("select ai from AuctionItem ai where ai.auction.code = :auctionCode")
    List<AuctionItem> listByAuctionCode(@Param("auctionCode") String auctionCode);

}
