package com.picho.reverseauctionblockchain.controller;

import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.dto.AuctionTestDTO;
import com.picho.reverseauctionblockchain.dto.RabUserRegistrationForm;
import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.GoodService;
import com.picho.reverseauctionblockchain.model.RabUser;
import com.picho.reverseauctionblockchain.service.AuctionService;
import com.picho.reverseauctionblockchain.service.GoodServiceService;
import com.picho.reverseauctionblockchain.service.RabUserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reverseauction")
public class ReverseAuctionController {

    @Autowired
    AuctionService auctionService;

    @Autowired
    GoodServiceService goodServiceService;

    @Autowired
    RabUserService rabUserService;

    @GetMapping("/list")
    public ResponseEntity<List<AuctionTestDTO>> getAuctions() throws IOException, InterruptedException, JSONException {
        String command = "curl -X GET http://localhost:5000/list-auctions -H \"Content-Type: application/json\"";
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
            auctionTestDTO.setAuctionCode((String)jsonObj.get("CodigoConvocatoria"));
            JSONArray items = ((JSONArray)jsonObj.get("Items"));
            JSONObject item= (JSONObject)(items.get(0));
            auctionTestDTO.setGoodServCode((String)item.get("CodigoBienServicio"));



            myList.add(auctionTestDTO);

        }

        return ResponseEntity.ok().body(myList);
    }

    @CrossOrigin
    @PostMapping(value = "/create/{entityCode}")
    public ResponseEntity<Auction>create(@PathVariable String entityCode ,@RequestBody AuctionCreateForm auctionCreateform) throws IOException, InterruptedException, JSONException, URISyntaxException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RabUser rabUser =  rabUserService.getRabUserByStateEntityCode(entityCode);
        URI uri = null;
        Auction auctionCreated = null;
        if (rabUser.getUsername().equals(username)){
            uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/reverseauction/create").toUriString());
            auctionCreated =  auctionService.saveAuction(entityCode, auctionCreateform);
            if (auctionCreated == null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.created(uri).body(auctionCreated);
    }

    @GetMapping("/getGoodServices")
    public ResponseEntity<List<GoodService>>getGoodServices(){

        return ResponseEntity.ok().body(goodServiceService.getGoodServices());
    }
}
