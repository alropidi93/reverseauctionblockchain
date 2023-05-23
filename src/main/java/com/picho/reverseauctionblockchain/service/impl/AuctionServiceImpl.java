package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.AuctionDAO;
import com.picho.reverseauctionblockchain.dao.RabUserDAO;
import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.service.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    AuctionDAO auctionDAO;

    @Override
    public Auction saveAuction(AuctionCreateForm auctionCreateForm) {
        Auction auction = new Auction();
        auction.setCode(auctionCreateForm.getAuctionCode());
        auction.setName(auctionCreateForm.getAuctionName());
        auction.setProcurementType(auctionCreateForm.getProcurementType() == "bien" ? 1 : 0);
        return auctionDAO.save(auction);

    }
}
