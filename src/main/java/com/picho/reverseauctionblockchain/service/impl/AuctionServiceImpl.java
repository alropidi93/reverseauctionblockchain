package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.AuctionDAO;
import com.picho.reverseauctionblockchain.dao.GoodServiceDAO;
import com.picho.reverseauctionblockchain.dao.RabUserDAO;
import com.picho.reverseauctionblockchain.dao.StateEntityDAO;
import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.GoodService;
import com.picho.reverseauctionblockchain.model.StateEntity;
import com.picho.reverseauctionblockchain.service.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Transactional
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    AuctionDAO auctionDAO;

    @Autowired
    GoodServiceDAO goodServiceDAO;

    @Autowired
    StateEntityDAO stateEntityDAO;

    @Override
    public Auction saveAuction(String entityCode, AuctionCreateForm auctionCreateForm) throws IOException, InterruptedException, URISyntaxException, JSONException {
        Auction auction = new Auction();
        auction.setCode(auctionCreateForm.getCode());
        auction.setName(auctionCreateForm.getName());
        auction.setReferenceValue(auctionCreateForm.getReferenceValue());
        auction.setDescription(auctionCreateForm.getDescription());

        StateEntity stateEntity =  stateEntityDAO.findByCode(entityCode);
        auction.setStateEntity(stateEntity);
        GoodService goodService = goodServiceDAO.findByCode(auctionCreateForm.getGoodServiceCode());
        auction.setGoodService(goodService);
        JSONObject data = new JSONObject();
        data.put("auctionCode",auction.getCode());
        data.put("goodServCode",auction.getGoodService().getCode());
        data.put("owner","SEACE");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/create-auction"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Auction auctionCreated = null;
        if(response.statusCode() == HttpStatus.OK.value()){
            System.out.println("Se guardo en la blockchain");
            JSONObject responseJson = new JSONObject(response.body());
            System.out.println(responseJson.get("message"));
            auctionCreated =  auctionDAO.save(auction);
        }
        else{
            System.out.println("no se guardo en la blockchain");
        }


        return auctionCreated;

    }
}
