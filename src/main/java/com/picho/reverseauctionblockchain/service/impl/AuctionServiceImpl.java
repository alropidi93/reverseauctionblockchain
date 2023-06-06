package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.*;
import com.picho.reverseauctionblockchain.dto.AuctionCreateForm;
import com.picho.reverseauctionblockchain.model.Auction;
import com.picho.reverseauctionblockchain.model.AuctionItem;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@Transactional
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    AuctionDAO auctionDAO;

    @Autowired
    AuctionItemDAO auctionItemDAO;

    @Autowired
    GoodServiceDAO goodServiceDAO;

    @Autowired
    StateEntityDAO stateEntityDAO;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Auction saveAuction(String entityCode, AuctionCreateForm auctionCreateForm) throws IOException, InterruptedException, URISyntaxException, JSONException {
        Auction auction = new Auction();
        auction.setCode(auctionCreateForm.getCode());
        auction.setName(auctionCreateForm.getName());
        auction.setReferenceValue(auctionCreateForm.getReferenceValue());
        auction.setDescription(auctionCreateForm.getDescription());


        StateEntity stateEntity =  stateEntityDAO.findByCode(entityCode);
        auction.setStateEntity(stateEntity);
        GoodService goodService = goodServiceDAO.findByCode(auctionCreateForm.getGoodServices().get(0).getCode());
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setGoodServiceType(goodService);
        Float quantity = auctionCreateForm.getGoodServices().get(0).getQuantity();
        quantity = Math.round(quantity*100.0f)/100.0f;
        auctionItem.setQuantity(quantity);
        if (goodService.getProcurementType() == 1)
        {
            auctionItem.setName(goodService.getName() + " X "+String.format("%.02f", quantity)+" " +goodService.getDimension());
        }
        else
        {
            auctionItem.setName(goodService.getName() + " X "+ goodService.getDimension()+": "+String.format("%.00f", quantity));
        }
        String dateTimeCreation = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));

        JSONObject itemData = new JSONObject();
        itemData.put("auctionCode",auction.getCode());
        itemData.put("entityCode",stateEntity.getCode());
        itemData.put("datetimeCreation",dateTimeCreation);
        itemData.put("owner","SEACE");
        JSONObject itemAuctionData = new JSONObject();
        itemAuctionData.put("CodigoBienServicio",goodService.getCode());
        itemAuctionData.put("Cantidad",quantity);
        itemAuctionData.put("Unidad",goodService.getDimension());

        itemData.put("item",itemAuctionData);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/create-auction"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(itemData.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Auction auctionCreated = null;
        if(response.statusCode() == HttpStatus.OK.value()){
            log.info("Se guardo en la blockchain");
            JSONObject responseJson = new JSONObject(response.body());
            System.out.println(responseJson.get("message"));
            auctionCreated =  auctionDAO.save(auction);
            auctionItem.setAuction(auctionCreated);
            AuctionItem auctionItemCreated = auctionItemDAO.save(auctionItem);
            log.info("Item creado satisfactoriamente");
            //auctionCreated = auctionDAO.getReferenceById(auctionCreated.getIdAuction());
        }
        else{
            System.out.println("no se guardo en la blockchain");
            auctionCreated = null;
        }

        return auctionCreated;

    }
}
