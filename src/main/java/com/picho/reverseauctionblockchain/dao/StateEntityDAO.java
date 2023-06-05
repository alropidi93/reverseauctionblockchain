package com.picho.reverseauctionblockchain.dao;


import com.picho.reverseauctionblockchain.model.GoodService;
import com.picho.reverseauctionblockchain.model.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.nimbus.State;

@Repository
public interface StateEntityDAO extends JpaRepository<StateEntity,Long> {
    @Query("select se from StateEntity se where se.code=:code")
    StateEntity findByCode(@Param("code") String code);
}
