package com.picho.reverseauctionblockchain.dao;


import com.picho.reverseauctionblockchain.model.BidderEntity;
import com.picho.reverseauctionblockchain.model.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BidderEntityDAO extends JpaRepository<BidderEntity,Long> {
    @Query("select be from BidderEntity be where be.code=:code")
    BidderEntity findByCode(@Param("code") String code);
}
