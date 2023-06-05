package com.picho.reverseauctionblockchain.controller;

import com.picho.reverseauctionblockchain.dto.BidderEntityCreateForm;
import com.picho.reverseauctionblockchain.dto.StateEntityCreateForm;
import com.picho.reverseauctionblockchain.model.BidderEntity;
import com.picho.reverseauctionblockchain.model.StateEntity;
import com.picho.reverseauctionblockchain.service.BidderEntityService;
import com.picho.reverseauctionblockchain.service.StateEntityService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/bidder_entity")
public class BidderEntityController {

    @Autowired
    BidderEntityService bidderEntityService;

    @CrossOrigin
    @PostMapping(value = "/create")
    public ResponseEntity<BidderEntity> create(@RequestBody BidderEntityCreateForm bidderEntityCreateForm) throws IOException, InterruptedException, JSONException, URISyntaxException {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/bidder_entity/create").toUriString());
        return ResponseEntity.created(uri).body(bidderEntityService.saveBidderEntity(bidderEntityCreateForm));
    }
}
