package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.BidderEntityDAO;
import com.picho.reverseauctionblockchain.dao.StateEntityDAO;
import com.picho.reverseauctionblockchain.dto.BidderEntityCreateForm;
import com.picho.reverseauctionblockchain.dto.StateEntityCreateForm;
import com.picho.reverseauctionblockchain.model.BidderEntity;
import com.picho.reverseauctionblockchain.model.StateEntity;
import com.picho.reverseauctionblockchain.service.BidderEntityService;
import com.picho.reverseauctionblockchain.service.StateEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidderEntityServiceImpl implements BidderEntityService {
    @Autowired
    BidderEntityDAO bidderEntityDAO;

    @Override
    public BidderEntity saveBidderEntity(BidderEntityCreateForm bidderEntityCreateForm) {
        BidderEntity bidderEntity = new BidderEntity();
        bidderEntity.setCode(bidderEntityCreateForm.getCode());
        bidderEntity.setName(bidderEntityCreateForm.getName());
        bidderEntity.setRuc(bidderEntityCreateForm.getRuc());
        return bidderEntityDAO.save(bidderEntity);
    }
}
