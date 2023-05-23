package com.picho.reverseauctionblockchain.service;

import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.model.Auction;

public interface AuctionService {
    Auction saveAuction (AuctionCreateForm auctionCreateForm);
}
