package com.picho.reverseauctionblockchain.service;

import com.picho.reverseauctionblockchain.model.GoodService;
import com.picho.reverseauctionblockchain.model.RabUser;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GoodServiceService {
    List<GoodService> getGoodServices();
}
