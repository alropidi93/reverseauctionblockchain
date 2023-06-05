package com.picho.reverseauctionblockchain.service;

import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.model.Auction;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AuctionService {
    Auction saveAuction (String entityCode,AuctionCreateForm auctionCreateForm) throws IOException, InterruptedException, URISyntaxException, JSONException;
}
