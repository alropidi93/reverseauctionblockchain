package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.GoodServiceDAO;
import com.picho.reverseauctionblockchain.dao.RoleDAO;
import com.picho.reverseauctionblockchain.model.GoodService;
import com.picho.reverseauctionblockchain.service.GoodServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class GoodServiceServiceImpl implements GoodServiceService {

    @Autowired
    GoodServiceDAO goodServiceDAO;

    @Override
    public List<GoodService> getGoodServices() {
        return goodServiceDAO.findAll();
    }
}
