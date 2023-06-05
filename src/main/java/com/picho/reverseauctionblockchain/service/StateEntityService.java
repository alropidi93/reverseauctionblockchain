package com.picho.reverseauctionblockchain.service;

import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.dto.StateEntityCreateForm;
import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.StateEntity;
import org.springframework.stereotype.Service;


public interface StateEntityService {
    StateEntity saveStateEntity (StateEntityCreateForm stateEntityCreateForm);
}
