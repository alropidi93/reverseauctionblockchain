package com.picho.reverseauctionblockchain.service;

import com.picho.reverseauctionblockchain.dto.*;
import com.picho.reverseauctionblockchain.model.Auction;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.StringJoiner;

public interface AuctionService {
    Auction saveAuction (String entityCode,AuctionCreateForm auctionCreateForm) throws IOException, InterruptedException, URISyntaxException, JSONException;
    AuctionSelectionDetail getAuction (String auctionCode) throws URISyntaxException, IOException, InterruptedException, JSONException;
    List <AuctionTestDTO> listAuctions() throws URISyntaxException, IOException, InterruptedException, JSONException;
    AuctionActivitiesDetail getAuctionActivities (String auctionCode, String bidderEntityCode) throws URISyntaxException, IOException, InterruptedException, JSONException;
    AuctionFistBidDetail getAuctionFirstBid (String auctionCode, String bidderEntityCode) throws URISyntaxException, IOException, InterruptedException, JSONException;
    AuctionPriceBidDetail getAuctionPriceBid (String auctionCode, String bidderEntityCode) throws URISyntaxException, IOException, InterruptedException, JSONException;
    Boolean registerBidderParticipation (String auctionCode,String bidderEntityCode, String participationRegisterDate) throws URISyntaxException, IOException, InterruptedException, JSONException;
    Boolean registerFirstBid (String auctionCode,String bidderEntityCode, Long idAuctionItem, String firstBidRegisterDate, Float firstBid) throws URISyntaxException, IOException, InterruptedException, JSONException;
    Boolean registerPriceBid (String auctionCode,String bidderEntityCode, Long idAuctionItem, String newBidRegisterDatetime, Float newBid) throws URISyntaxException, IOException, InterruptedException, JSONException;
    Boolean registerBuenaPro (String auctionCode,Long idAuctionItem, String firstBidderEntityCode, String secondBidderEntityCode, String buenaProRegisterDatetime) throws URISyntaxException, IOException, InterruptedException, JSONException;
    List<AuctionBuenaProDetail> getAuctionBuenaProInfo(String auctionCode) throws URISyntaxException, IOException, InterruptedException, JSONException;
    String getStateEntityUsernameByAuctionCode(String auctionCode);
}
