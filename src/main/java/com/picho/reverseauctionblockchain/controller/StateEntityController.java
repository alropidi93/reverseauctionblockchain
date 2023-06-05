package com.picho.reverseauctionblockchain.controller;

import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.dto.StateEntityCreateForm;
import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.StateEntity;
import com.picho.reverseauctionblockchain.service.GoodServiceService;
import com.picho.reverseauctionblockchain.service.StateEntityService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/state_entity")
public class StateEntityController {

    @Autowired
    StateEntityService stateEntityService;

    @CrossOrigin
    @PostMapping(value = "/create")
    public ResponseEntity<StateEntity> create(@RequestBody StateEntityCreateForm stateEntityCreateForm) throws IOException, InterruptedException, JSONException, URISyntaxException {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/state_entity/create").toUriString());
        return ResponseEntity.created(uri).body(stateEntityService.saveStateEntity(stateEntityCreateForm));
    }
}
