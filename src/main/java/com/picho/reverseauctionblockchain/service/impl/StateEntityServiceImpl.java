package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.RabUserDAO;
import com.picho.reverseauctionblockchain.dao.StateEntityDAO;
import com.picho.reverseauctionblockchain.dto.StateEntityCreateForm;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.model.StateEntity;
import com.picho.reverseauctionblockchain.service.StateEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateEntityServiceImpl implements StateEntityService {
    @Autowired
    StateEntityDAO stateEntityDAO;

    @Override
    public StateEntity saveStateEntity(StateEntityCreateForm stateEntityCreateForm) {

        StateEntity stateEntity = new StateEntity();
        stateEntity.setCode(stateEntityCreateForm.getCode());
        stateEntity.setName(stateEntityCreateForm.getName());
        stateEntity.setDescription(stateEntityCreateForm.getDescription());
        return stateEntityDAO.save(stateEntity);

    }


}
