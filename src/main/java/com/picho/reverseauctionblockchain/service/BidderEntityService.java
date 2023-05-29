package com.picho.reverseauctionblockchain.service;

import com.picho.reverseauctionblockchain.dto.BidderEntityCreateForm;
import com.picho.reverseauctionblockchain.dto.StateEntityCreateForm;
import com.picho.reverseauctionblockchain.model.BidderEntity;
import com.picho.reverseauctionblockchain.model.StateEntity;


public interface BidderEntityService {
    BidderEntity saveBidderEntity (BidderEntityCreateForm bidderEntityCreateForm);
}
