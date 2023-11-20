package com.picho.reverseauctionblockchain.controller;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.picho.reverseauctionblockchain.dto.*;
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

    @GetMapping("/listBeta")
    public ResponseEntity<List<AuctionTestDTO>> getAuctionsBeta() throws IOException, InterruptedException, JSONException {
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

    @GetMapping("/list")
    public ResponseEntity<List<AuctionTestDTO>> getAuctions() throws IOException, InterruptedException, JSONException, URISyntaxException {
        List<AuctionTestDTO> auctions = auctionService.listAuctions();
        return ResponseEntity.ok().body(auctions);
    }

    @GetMapping("/selection/detail/{auctionCode}")
    public ResponseEntity<AuctionSelectionDetail> getSelectionDetail(@PathVariable String auctionCode) throws JSONException, URISyntaxException, IOException, InterruptedException {
        AuctionSelectionDetail auctionSelectionDetail  =  auctionService.getAuction(auctionCode);
        return ResponseEntity.ok().body(auctionSelectionDetail);
    }

    @GetMapping("/activities/detail/{auctionCode}/{bidderEntityCode}")
    public ResponseEntity<AuctionActivitiesDetail> getActivitiesDetail(@PathVariable String auctionCode,@PathVariable String bidderEntityCode) throws JSONException, URISyntaxException, IOException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RabUser rabUser =  rabUserService.getRabUserByBidderEntityCode(bidderEntityCode);
        if (rabUser.getUsername().equals(username)){
            AuctionActivitiesDetail auctionActivitiesDetail  =  auctionService.getAuctionActivities(auctionCode,bidderEntityCode);
            return ResponseEntity.ok().body(auctionActivitiesDetail);
        }

        return ResponseEntity.ok().body(null);

    }

    @GetMapping("/firstBid/detail/{auctionCode}/{bidderEntityCode}")
    public ResponseEntity<AuctionFistBidDetail> getfirstBidDetail(@PathVariable String auctionCode,@PathVariable String bidderEntityCode) throws JSONException, URISyntaxException, IOException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RabUser rabUser =  rabUserService.getRabUserByBidderEntityCode(bidderEntityCode);
        if (rabUser.getUsername().equals(username)){
            AuctionFistBidDetail auctionFistBidDetail  =  auctionService.getAuctionFirstBid(auctionCode,bidderEntityCode);
            return ResponseEntity.ok().body(auctionFistBidDetail);
        }

        return ResponseEntity.ok().body(null);

    }

    @GetMapping("/priceBid/detail/{auctionCode}/{bidderEntityCode}")
    public ResponseEntity<AuctionPriceBidDetail> getpriceBidDetail(@PathVariable String auctionCode,@PathVariable String bidderEntityCode) throws JSONException, URISyntaxException, IOException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RabUser rabUser =  rabUserService.getRabUserByBidderEntityCode(bidderEntityCode);
        if (rabUser.getUsername().equals(username)){
            AuctionPriceBidDetail auctionPriceBidDetail  =  auctionService.getAuctionPriceBid(auctionCode,bidderEntityCode);
            return ResponseEntity.ok().body(auctionPriceBidDetail);
        }

        return ResponseEntity.ok().body(null);

    }

    @GetMapping("/buenapro/detail/{auctionCode}")
    public ResponseEntity<List<AuctionBuenaProDetail>> getBuenaproDetail(@PathVariable String auctionCode) throws JSONException, URISyntaxException, IOException, InterruptedException {


        List<AuctionBuenaProDetail> auctionBuenaProDetailList  =  auctionService.getAuctionBuenaProInfo(auctionCode);
        return ResponseEntity.ok().body(auctionBuenaProDetailList);


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

    @CrossOrigin
    @PostMapping(value = "/registerParticipation/{auctionCode}/{bidderEntityCode}")
    public ResponseEntity<Auction>registerParticipation(@PathVariable String auctionCode ,@PathVariable String bidderEntityCode, @RequestBody AuctionRegisterParticipationForm auctionRegisterParticipationForm) throws IOException, InterruptedException, JSONException, URISyntaxException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RabUser rabUser =  rabUserService.getRabUserByBidderEntityCode(bidderEntityCode);
        URI uri = null;
        if (rabUser.getUsername().equals(username)){
            uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/reverseauction/registerParticipation").toUriString());
            Boolean participationFlag =  auctionService.registerBidderParticipation(auctionCode, bidderEntityCode,auctionRegisterParticipationForm.getParticipationRegisterDate());
            if (participationFlag == null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.ok().body(null);
    }

    @CrossOrigin
    @PostMapping(value = "/registerFirstBid/{auctionCode}/{bidderEntityCode}/{idAuctionItem}")
    public ResponseEntity<Auction>registerFirstBid(@PathVariable String auctionCode ,@PathVariable String bidderEntityCode,@PathVariable Long idAuctionItem, @RequestBody AuctionRegisterFirstBidForm auctionRegisterFirstBidForm) throws IOException, InterruptedException, JSONException, URISyntaxException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RabUser rabUser =  rabUserService.getRabUserByBidderEntityCode(bidderEntityCode);
        URI uri = null;
        if (rabUser.getUsername().equals(username)){
            uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/reverseauction/registerFirstBid").toUriString());
            Boolean firstBidFlag =  auctionService.registerFirstBid(auctionCode, bidderEntityCode,idAuctionItem,auctionRegisterFirstBidForm.getFirstBidRegisterDate(),auctionRegisterFirstBidForm.getFirstBid());
            if (firstBidFlag == null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.ok().body(null);
    }


    @CrossOrigin
    @PostMapping(value = "/registerPriceBid/{auctionCode}/{bidderEntityCode}/{idAuctionItem}")
    public ResponseEntity<Auction>registerPriceBid(@PathVariable String auctionCode ,@PathVariable String bidderEntityCode,@PathVariable Long idAuctionItem, @RequestBody AuctionRegisterPriceBidForm auctionRegisterPriceBidForm) throws IOException, InterruptedException, JSONException, URISyntaxException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        //String username ="fake";
        RabUser rabUser =  rabUserService.getRabUserByBidderEntityCode(bidderEntityCode);
        URI uri = null;
        if (rabUser.getUsername().equals(username)){
            uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/reverseauction/registerPriceBid").toUriString());
            Boolean firstBidFlag =  auctionService.registerPriceBid(auctionCode, bidderEntityCode,idAuctionItem,auctionRegisterPriceBidForm.getRegDatetime(),auctionRegisterPriceBidForm.getNewBid());
            if (firstBidFlag == null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.ok().body(null);
    }

    @CrossOrigin
    @PostMapping(value = "/registerBuenaPro/{auctionCode}/{idAuctionItem}")
    public ResponseEntity<Auction>registerBuenaPro(@PathVariable String auctionCode,@PathVariable  Long idAuctionItem,@RequestBody AuctionRegisterBuenaProForm auctionRegisterBuenaProForm) throws IOException, InterruptedException, JSONException, URISyntaxException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String usernameDB = auctionService.getStateEntityUsernameByAuctionCode(auctionCode);

        URI uri = null;
        if (usernameDB.equals(username)){
            uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/reverseauction/registerBuenaPro").toUriString());
            Boolean buenaProFlag =  auctionService.registerBuenaPro(auctionCode,idAuctionItem,
                    auctionRegisterBuenaProForm.getFirstBidderEntityCode(),
                    auctionRegisterBuenaProForm.getSecondBidderEntityCode(),
                    auctionRegisterBuenaProForm.getDatetimeReg());
            //if (firstBidFlag == null)
            //    return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.ok().body(null);
    }
    @GetMapping("/getGoodServices")
    public ResponseEntity<List<GoodService>>getGoodServices(){

        return ResponseEntity.ok().body(goodServiceService.getGoodServices());
    }
}
