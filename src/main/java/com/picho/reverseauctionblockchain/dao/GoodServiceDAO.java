package com.picho.reverseauctionblockchain.dao;

import com.picho.reverseauctionblockchain.model.GoodService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodServiceDAO extends JpaRepository<GoodService,Long> {


   @Query("select gs from GoodService gs where gs.code=:code")
   GoodService findByCode(@Param("code") String code);

}
