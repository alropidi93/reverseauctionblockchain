package com.picho.reverseauctionblockchain.controller;

import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.dto.AuctionTestDTO;
import com.picho.reverseauctionblockchain.dto.RabUserRegistrationForm;
import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.service.AuctionService;
import com.picho.reverseauctionblockchain.service.RabUserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reverseauction")
public class ReverseAuctionController {

    @Autowired
    AuctionService auctionService;

    @GetMapping("/list")
    public ResponseEntity<List<AuctionTestDTO>> getAuctions() throws IOException, InterruptedException, JSONException {
        String command = "curl -X GET http://localhost:5000/list-auctions";
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        Process pr = processBuilder.start();
        pr.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line2 = "";
        StringBuilder output = new StringBuilder("");
        while ((line2 = reader.readLine()) != null) {
            output.append(line2 + "\n");
        }
        System.out.println("\n\n\noutput====" + output + "===\n\n\n");

        String sbToString = output.toString();
        JSONArray jsonArray = new JSONArray(sbToString);
        List<AuctionTestDTO> myList = new ArrayList<AuctionTestDTO>();
        JSONObject jsonObj;
        for (int i=0; i < jsonArray.length(); i++) {
            jsonObj =  jsonArray.getJSONObject(i);
            AuctionTestDTO auctionTestDTO =  new AuctionTestDTO();
            auctionTestDTO.setOwner((String)jsonObj.get("Owner"));
            auctionTestDTO.setGoodServCode((String)jsonObj.get("CodigoBienServicio"));
            auctionTestDTO.setAuctionCode((String)jsonObj.get("CodigoConvocatoria"));
            myList.add(auctionTestDTO);

        }

        return ResponseEntity.ok().body(myList);
    }

    @CrossOrigin
    @PostMapping(value = "/create")
    public ResponseEntity<Auction>create(@RequestBody AuctionCreateForm auctionCreateform){

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/reverseauction/create").toUriString());
        return ResponseEntity.created(uri).body(auctionService.saveAuction(auctionCreateform));
    }
}
