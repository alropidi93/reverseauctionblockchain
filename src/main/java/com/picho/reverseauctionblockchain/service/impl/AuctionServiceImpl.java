package com.picho.reverseauctionblockchain.service.impl;

import com.picho.reverseauctionblockchain.dao.*;
import com.picho.reverseauctionblockchain.dto.*;
import com.picho.reverseauctionblockchain.model.*;
import com.picho.reverseauctionblockchain.service.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    AuctionDAO auctionDAO;

    @Autowired
    RabUserDAO rabuserDAO;
    @Autowired
    AuctionItemDAO auctionItemDAO;

    @Autowired
    GoodServiceDAO goodServiceDAO;

    @Autowired
    StateEntityDAO stateEntityDAO;

    @Autowired
    AuctionBidderDAO auctionBidderDAO;

    @Autowired
    AuctionItemBidderDAO auctionItemBidderDAO;

    @Autowired
    BidderEntityDAO bidderEntityDAO;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Auction saveAuction(String entityCode, AuctionCreateForm auctionCreateForm) throws IOException, InterruptedException, URISyntaxException, JSONException {
        //fecha hora de inicio de etapa de registro y primera oferta debe ser como menor
        // a las 00 horas del día siguiente de registrada la oferta

        //fecha hora fin de etapa de registro y primera oferta debe ser menor o igual a 5|8 días
        //despues de iniciado la etapa de convocatoria


        //fecha hora inicio de etapa de pujas debe ser mayor  a 5|8 días
        // despues de iniciada la etapa de convocatoria

        //fecha hora inicio de buena pro

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

        String dateTimeCreationStr = auctionCreateForm.getDatetimeReg();

        JSONObject itemData = new JSONObject();
        itemData.put("auctionCode",auction.getCode());
        itemData.put("entityCode",stateEntity.getCode());
        itemData.put("datetimeCreation",dateTimeCreationStr);
        itemData.put("referenceValue",auctionCreateForm.getReferenceValue());
        itemData.put("owner","SEACE");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        LocalDateTime datetimeCreation = LocalDateTime.parse(dateTimeCreationStr, formatter);
        /*
        if ( datetimeCreation.getSecond() > 0){
            datetimeCreation = datetimeCreation.plus(1,ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES).truncatedTo(ChronoUnit.SECONDS);
            dateTimeCreationStr = datetimeCreation.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        }*/
        String datetimeReferenceStr = auctionCreateForm.getDatetimeInitFirstBid();
        LocalDateTime datetimeReference = LocalDateTime.parse(datetimeReferenceStr, formatter);
        datetimeReference = datetimeReference.minus(1, ChronoUnit.MINUTES);
        String datetimeEndConvStr = datetimeReference.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        itemData.put("datetimeInitConv",dateTimeCreationStr);
        itemData.put("datetimeEndConv",datetimeEndConvStr);
        itemData.put("datetimeInitFirstBid",auctionCreateForm.getDatetimeInitFirstBid());
        itemData.put("datetimeEndFirstBid",auctionCreateForm.getDatetimeEndFirstBid());
        itemData.put("datetimeInitPriceBid",auctionCreateForm.getDatetimeInitPriceBid());
        itemData.put("datetimeEndPriceBid",auctionCreateForm.getDatetimeEndPriceBid());
        itemData.put("datetimeInitBuenaPro",auctionCreateForm.getDatetimeInitBuenaPro());
        itemData.put("datetimeEndBuenaPro",auctionCreateForm.getDatetimeEndBuenaPro());

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

    @Override
    public AuctionSelectionDetail getAuction(String auctionCode) throws URISyntaxException, IOException, InterruptedException, JSONException {
        AuctionSelectionDetail auctionSelectionDetail = new AuctionSelectionDetail();
        Auction auctionDB = auctionDAO.findByCode(auctionCode);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/get-auction/"+auctionCode))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == HttpStatus.OK.value()){
            log.info("Se obtuvo detalle de la blockchain");
            JSONObject responseJson = new JSONObject(response.body());
            log.info("Se obtuvo detalle de la blockchain");
            auctionSelectionDetail.setCode(auctionCode);
            auctionSelectionDetail.setName(auctionDB.getName());
            auctionSelectionDetail.setEntityName(auctionDB.getStateEntity().getName());
            AuctionItem auctionItem =  auctionItemDAO.findByAuctionCode(auctionCode);
            auctionSelectionDetail.setGoodServiceType(auctionItem.getGoodServiceType().getProcurementType()==1?"Bien":"Servicio");
            auctionSelectionDetail.setDatetimeInitConv((String)((JSONObject)responseJson.get("ConvocatoriaPhase")).get("DatetimeInit"));
            auctionSelectionDetail.setDatetimeEndConv((String)((JSONObject)responseJson.get("ConvocatoriaPhase")).get("DatetimeEnd"));
            auctionSelectionDetail.setDatetimeInitFirstBid((String)((JSONObject)responseJson.get("PrimeraOfertaPhase")).get("DatetimeInit"));
            auctionSelectionDetail.setDatetimeEndFirstBid((String)((JSONObject)responseJson.get("PrimeraOfertaPhase")).get("DatetimeEnd"));
            auctionSelectionDetail.setDatetimeInitPriceBid((String)((JSONObject)responseJson.get("PujaPhase")).get("DatetimeInit"));
            auctionSelectionDetail.setDatetimeEndPriceBid((String)((JSONObject)responseJson.get("PujaPhase")).get("DatetimeEnd"));
            auctionSelectionDetail.setDatetimeInitBuenaPro((String)((JSONObject)responseJson.get("BuenaProPhase")).get("DatetimeInit"));
            auctionSelectionDetail.setDatetimeEndBuenaPro((String)((JSONObject)responseJson.get("BuenaProPhase")).get("DatetimeEnd"));
            Integer currentPhase;
            if (((String)((JSONObject)responseJson.get("ConvocatoriaPhase")).get("DatetimeEndStamp")).equals("")) currentPhase = 1;
            else if (((String)((JSONObject)responseJson.get("PrimeraOfertaPhase")).get("DatetimeEndStamp")).equals("")) currentPhase = 2;
            else if (((String)((JSONObject)responseJson.get("PujaPhase")).get("DatetimeEndStamp")).equals("")) currentPhase = 3;
            else if (((String)((JSONObject)responseJson.get("BuenaProPhase")).get("DatetimeEndStamp")).equals("")) currentPhase = 4;
            else currentPhase = 5;
            auctionSelectionDetail.setCurrentPhase(currentPhase);
            auctionSelectionDetail.setReferenceValue(auctionDB.getReferenceValue());
            auctionSelectionDetail.setDescription(auctionDB.getDescription());
            return auctionSelectionDetail;
        }
        else return null;

    }

    @Override
    public List<AuctionTestDTO> listAuctions() throws URISyntaxException, IOException, InterruptedException, JSONException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/list-auctions"))
                .headers("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == HttpStatus.OK.value()){
            JSONArray responseJson = new JSONArray(response.body());
            List<AuctionTestDTO> auctions = new ArrayList<AuctionTestDTO>();
            JSONObject jsonObj;
            for (int i=0; i < responseJson.length(); i++) {
                jsonObj =  responseJson.getJSONObject(i);
                AuctionTestDTO auctionTestDTO =  new AuctionTestDTO();
                auctionTestDTO.setOwner((String)jsonObj.get("Owner"));
                auctionTestDTO.setAuctionCode((String)jsonObj.get("CodigoConvocatoria"));
                JSONArray items = ((JSONArray)jsonObj.get("Items"));
                JSONObject item= (JSONObject)(items.get(0));
                auctionTestDTO.setGoodServCode((String)item.get("CodigoBienServicio"));
                auctions.add(auctionTestDTO);

            }
            return auctions;
        }
        else return null;
    }

    @Override
    public AuctionActivitiesDetail getAuctionActivities(String auctionCode, String bidderEntityCode) throws URISyntaxException, IOException, InterruptedException, JSONException {

        AuctionActivitiesDetail auctionActivitiesDetail = new AuctionActivitiesDetail();
        Auction auctionDB = auctionDAO.findByCode(auctionCode);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/get-auction/"+auctionCode))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        if(response.statusCode() == HttpStatus.OK.value()){
            log.info("Se obtuvo detalle de la blockchain");
            JSONObject responseJson = new JSONObject(response.body());
            log.info("Se obtuvo detalle de la blockchain");
            auctionActivitiesDetail.setCode(auctionCode);
            auctionActivitiesDetail.setName(auctionDB.getName());
            AuctionItem auctionItem =  auctionItemDAO.findByAuctionCode(auctionCode);
            auctionActivitiesDetail.setGoodServiceType(auctionItem.getGoodServiceType().getProcurementType()==1?"Bien":"Servicio");
            List<AuctionActivitiesDetail.CronogramRow> cronogramRowList = new ArrayList<AuctionActivitiesDetail.CronogramRow>();
            AuctionActivitiesDetail.CronogramRow cronogramRowConv,cronogramRowPrimera, cronogramRowPujas, cronogramRowBuenaPro = null;

            cronogramRowConv = new AuctionActivitiesDetail.CronogramRow();
            cronogramRowConv.setOrder(1);
            cronogramRowConv.setActivity("Registro de participación");
            cronogramRowConv.setDatetimeInit((String)((JSONObject)responseJson.get("ConvocatoriaPhase")).get("DatetimeInit"));
            cronogramRowConv.setDatetimeEnd((String)((JSONObject)responseJson.get("ConvocatoriaPhase")).get("DatetimeEnd"));
            //Participation register flag
            Boolean participationRegisterFlag;
            AuctionBidderEntityId auctionBidderEntityId = new AuctionBidderEntityId(auctionDAO.findByCode(auctionCode).getIdAuction(),
                    bidderEntityDAO.findByCode(bidderEntityCode).getIdBidderEntity());
            Optional<AuctionBidder> auctionBidder =  auctionBidderDAO.findById(auctionBidderEntityId);
            participationRegisterFlag = auctionBidder.isPresent() ;
            cronogramRowConv.setRegisterFlag(participationRegisterFlag);
            //End participation register flag

            if ( !((String)((JSONObject)responseJson.get("ConvocatoriaPhase")).get("DatetimeEndStamp")).equals("") ){

                cronogramRowConv.setStatus(2);
                cronogramRowList.add(cronogramRowConv);
                if (participationRegisterFlag) {
                    cronogramRowPrimera = new AuctionActivitiesDetail.CronogramRow();
                    cronogramRowPrimera.setOrder(2);
                    cronogramRowPrimera.setActivity("Presentación de ofertas");
                    cronogramRowPrimera.setDatetimeInit((String) ((JSONObject) responseJson.get("PrimeraOfertaPhase")).get("DatetimeInit"));
                    cronogramRowPrimera.setDatetimeEnd((String) ((JSONObject) responseJson.get("PrimeraOfertaPhase")).get("DatetimeEnd"));
                    //First bid register flag
                    Boolean firstBidRegisterFlag;
                    AuctionItemBidderId auctionItemBidderId = new AuctionItemBidderId(auctionDAO.findByCode(auctionCode).getIdAuction(),
                            bidderEntityDAO.findByCode(bidderEntityCode).getIdBidderEntity(), auctionItemDAO.findByAuctionCode(auctionCode).getIdAuctionItem());
                    Optional<AuctionItemBidder> auctionItemBidder = auctionItemBidderDAO.findById(auctionItemBidderId);
                    firstBidRegisterFlag = auctionItemBidder.isPresent();
                    cronogramRowPrimera.setRegisterFlag(firstBidRegisterFlag);
                    //End bid register flag
                    if (!((String) ((JSONObject) responseJson.get("PrimeraOfertaPhase")).get("DatetimeEndStamp")).equals("")) {
                        cronogramRowPrimera.setStatus(2);
                        cronogramRowList.add(cronogramRowPrimera);
                        cronogramRowPujas = new AuctionActivitiesDetail.CronogramRow();
                        cronogramRowPujas.setOrder(3);
                        cronogramRowPujas.setActivity("Mejora de precios");
                        cronogramRowPujas.setDatetimeInit((String) ((JSONObject) responseJson.get("PujaPhase")).get("DatetimeInit"));
                        cronogramRowPujas.setDatetimeEnd((String) ((JSONObject) responseJson.get("PujaPhase")).get("DatetimeEnd"));

                        if (!((String) ((JSONObject) responseJson.get("PujaPhase")).get("DatetimeEndStamp")).equals("")) {
                            cronogramRowPujas.setStatus(2);
                            cronogramRowList.add(cronogramRowPujas);
                            cronogramRowBuenaPro = new AuctionActivitiesDetail.CronogramRow();
                            cronogramRowBuenaPro.setOrder(4);
                            cronogramRowBuenaPro.setActivity("Otorgamiendo de buena pro");
                            cronogramRowBuenaPro.setDatetimeInit((String) ((JSONObject) responseJson.get("BuenaProPhase")).get("DatetimeInit"));
                            cronogramRowBuenaPro.setDatetimeEnd((String) ((JSONObject) responseJson.get("BuenaProPhase")).get("DatetimeEnd"));
                            if (!((String) ((JSONObject) responseJson.get("PujaPhase")).get("DatetimeEndStamp")).equals(""))
                                cronogramRowBuenaPro.setStatus(2);
                            else cronogramRowBuenaPro.setStatus(1);
                            cronogramRowList.add(cronogramRowBuenaPro);
                        } else {
                            cronogramRowPujas.setStatus(1);
                            cronogramRowList.add(cronogramRowPujas);
                        }
                        ;
                    } else {
                        cronogramRowPrimera.setStatus(1);
                        cronogramRowList.add(cronogramRowPrimera);
                    }
                }
            }
            else{
                cronogramRowConv.setStatus(1);
                cronogramRowList.add(cronogramRowConv);
            }




            auctionActivitiesDetail.setActivities(cronogramRowList);
            return auctionActivitiesDetail;
        }
        else return null;
    }

    @Override
    public AuctionFistBidDetail getAuctionFirstBid(String auctionCode, String bidderEntityCode) throws URISyntaxException, IOException, InterruptedException, JSONException {
        AuctionFistBidDetail auctionFistBidDetail = new AuctionFistBidDetail();
        Auction auction = auctionDAO.findByCode(auctionCode);
        BidderEntity bidderEntity = bidderEntityDAO.findByCode(bidderEntityCode);
        auctionFistBidDetail.setCode(auction.getCode());
        auctionFistBidDetail.setName(auction.getName());
        List<AuctionFistBidDetail.Item> items = new ArrayList<AuctionFistBidDetail.Item>();
        List<AuctionItem> auctionItems = auctionItemDAO.listByAuctionCode(auctionCode);
        Integer counter= 0;
        for (AuctionItem auctionItem: auctionItems) {
            counter++;
            AuctionFistBidDetail.Item item = new AuctionFistBidDetail.Item();
            item.setOrder(counter);
            item.setId(auctionItem.getIdAuctionItem());
            item.setName(auctionItem.getName());
            AuctionItemBidderId auctionItemBidderId = new AuctionItemBidderId(auction.getIdAuction(),bidderEntity.getIdBidderEntity(),auctionItem.getIdAuctionItem());
            Optional<AuctionItemBidder>auctionItemBidder = auctionItemBidderDAO.findById(auctionItemBidderId);
            Float firstBid = null;
            String datetimeReg = null;
            if (auctionItemBidder.isPresent()){
                firstBid = auctionItemBidder.get().getFirstBid();
                datetimeReg = auctionItemBidder.get().getFirstBidRegisterDatetime().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
            }
            item.setFirstBid(firstBid);
            item.setDatetimeReg(datetimeReg);
            items.add(item);

        }
        auctionFistBidDetail.setItems(items);
        return  auctionFistBidDetail;


    }

    @Override
    public AuctionPriceBidDetail getAuctionPriceBid(String auctionCode, String bidderEntityCode) throws URISyntaxException, IOException, InterruptedException, JSONException {
        AuctionPriceBidDetail auctionPriceBidDetail = new AuctionPriceBidDetail();
        Auction auction = auctionDAO.findByCode(auctionCode);
        BidderEntity bidderEntity = bidderEntityDAO.findByCode(bidderEntityCode);
        auctionPriceBidDetail.setBidderName(bidderEntity.getName());


        List<AuctionPriceBidDetail.Item> items = new ArrayList<AuctionPriceBidDetail.Item>();
        List<AuctionItem> auctionItems = auctionItemDAO.listByAuctionCode(auctionCode);
        Integer counter= 0;
        for (AuctionItem auctionItem: auctionItems) {
            counter++;
            AuctionPriceBidDetail.Item item = new AuctionPriceBidDetail.Item();
            item.setOrder(counter);
            item.setId(auctionItem.getIdAuctionItem());
            item.setName(auctionItem.getName());

            AuctionItemBidderId auctionItemBidderId = new AuctionItemBidderId(auction.getIdAuction(),bidderEntity.getIdBidderEntity(),auctionItem.getIdAuctionItem());
            Optional<AuctionItemBidder>auctionItemBidder = auctionItemBidderDAO.findById(auctionItemBidderId);
            if (auctionItemBidder.isPresent()){
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:5000/get-special-info-auction/"+auctionCode+"/"+bidderEntityCode+"/"+auctionItemBidder.get().getAuctionItem().getGoodServiceType().getCode()))
                        .headers("Content-Type", "application/json")
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if(response.statusCode() == HttpStatus.OK.value()){
                    JSONObject responseJson = new JSONObject(response.body());

                    item.setCurrentBid((float) responseJson.getDouble("CurrentBid"));
                    item.setLastBid((float)responseJson.getDouble("LastBid"));
                    item.setBidState((Integer)responseJson.get("State"));
                    item.setIsOpen((Boolean) responseJson.get("IsOpen"));
                }

            }
            items.add(item);
        }
        auctionPriceBidDetail.setItems(items);
        return  auctionPriceBidDetail;
    }

    @Override
    public Boolean registerBidderParticipation(String auctionCode, String bidderEntityCode, String participationRegisterDate) throws URISyntaxException, IOException, InterruptedException, JSONException {
        Auction auctionDB = auctionDAO.findByCode(auctionCode);
        BidderEntity bidderEntityDB = bidderEntityDAO.findByCode(bidderEntityCode);
        HttpClient client = HttpClient.newHttpClient();
        JSONObject registerParticipationData = new JSONObject();
        registerParticipationData.put("datetimeReg",participationRegisterDate);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/register-participation/"+auctionDB.getCode()+"/"+bidderEntityDB.getCode()))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(registerParticipationData.toString()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == HttpStatus.OK.value()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            LocalDateTime dateTime = LocalDateTime.parse(participationRegisterDate, formatter);
            AuctionBidder auctionBidder = new AuctionBidder();
            auctionBidder.setRegisterDateTime(dateTime);
            auctionBidder.setIdAuction(auctionDB.getIdAuction());
            auctionBidder.setIdBidderEntity(bidderEntityDB.getIdBidderEntity());
            AuctionBidder auctionBidderCreated =  auctionBidderDAO.save(auctionBidder);
            return auctionBidderCreated!=null;
        }
        else return false;

    }

    @Override
    public Boolean registerFirstBid(String auctionCode, String bidderEntityCode,Long idAuctionItem, String firstBidRegisterDate, Float firstBid) throws URISyntaxException, IOException, InterruptedException, JSONException {
        Auction auctionDB = auctionDAO.findByCode(auctionCode);
        BidderEntity bidderEntityDB =  bidderEntityDAO.findByCode(bidderEntityCode);
        AuctionItem auctionItemDB =  auctionItemDAO.getReferenceById(idAuctionItem);
        HttpClient client = HttpClient.newHttpClient();
        JSONObject registerFirstBidData = new JSONObject();
        registerFirstBidData.put("datetimeReg",firstBidRegisterDate);
        registerFirstBidData.put("firstBid",firstBid);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/register-first-bid/"+auctionDB.getCode()+"/"+bidderEntityDB.getCode() + "/"+auctionItemDB.getGoodServiceType().getCode()))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(registerFirstBidData.toString()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == HttpStatus.OK.value()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            LocalDateTime dateTime = LocalDateTime.parse(firstBidRegisterDate, formatter);
            AuctionItemBidder auctionItemBidder = new AuctionItemBidder();

            auctionItemBidder.setFirstBidRegisterDatetime(dateTime);
            auctionItemBidder.setFirstBid(firstBid);
            auctionItemBidder.setIdAuction(auctionDB.getIdAuction());
            auctionItemBidder.setIdBidderEntity(bidderEntityDB.getIdBidderEntity());
            auctionItemBidder.setIdAuctionItem(auctionItemDB.getIdAuctionItem());


            AuctionItemBidder auctionItemBidderCreated =  auctionItemBidderDAO.save(auctionItemBidder);

            return auctionItemBidderCreated!=null;
        }
        else return false;


    }

    @Override
    public Boolean registerPriceBid(String auctionCode, String bidderEntityCode, Long idAuctionItem, String newBidRegisterDatetime, Float newBid) throws URISyntaxException, IOException, InterruptedException, JSONException {
        Auction auctionDB = auctionDAO.findByCode(auctionCode);
        BidderEntity bidderEntityDB =  bidderEntityDAO.findByCode(bidderEntityCode);
        AuctionItem auctionItemDB =  auctionItemDAO.getReferenceById(idAuctionItem);
        AuctionItemBidderId auctionItemBidderId = new AuctionItemBidderId(auctionDB.getIdAuction(),bidderEntityDB.getIdBidderEntity(),idAuctionItem);
        Optional<AuctionItemBidder> auctionItemBidder =  auctionItemBidderDAO.findById(auctionItemBidderId);
        if (auctionItemBidder.isPresent()){
            HttpClient client = HttpClient.newHttpClient();
            JSONObject registerPriceBidData = new JSONObject();
            registerPriceBidData.put("datetimeReg",newBidRegisterDatetime);
            registerPriceBidData.put("newBid",newBid);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:5000/register-price-bid/"+auctionDB.getCode()+"/"+bidderEntityDB.getCode() + "/"+auctionItemDB.getGoodServiceType().getCode()))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(registerPriceBidData.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == HttpStatus.OK.value()){
                AuctionItemBidder auctionItemBidderDB = auctionItemBidder.get();
                auctionItemBidderDB.setLastBid(auctionItemBidderDB.getCurrentBid());
                auctionItemBidderDB.setCurrentBid(newBid);

                AuctionItemBidder auctionItemBidderUpdated =  auctionItemBidderDAO.save(auctionItemBidderDB);

                return auctionItemBidderUpdated!=null;
            }
            else return false;
        }
        else return false;
    }

    @Override
    public Boolean registerBuenaPro(String auctionCode, Long idAuctionItem, String firstEntityCode, String secondEntityCode, String buenaProRegisterDatetime) throws URISyntaxException, IOException, InterruptedException, JSONException {
        Auction auctionDB = auctionDAO.findByCode(auctionCode);

        BidderEntity firstBidderDB =  bidderEntityDAO.findByCode(firstEntityCode);
        BidderEntity secondBidderDB =  bidderEntityDAO.findByCode(secondEntityCode);
        AuctionItem auctionItemDB =  auctionItemDAO.getReferenceById(idAuctionItem);
        AuctionItemBidderId firstAuctionItemBidderId = new AuctionItemBidderId(auctionDB.getIdAuction(),firstBidderDB.getIdBidderEntity(),idAuctionItem);
        AuctionItemBidderId secondAuctionItemBidderId = new AuctionItemBidderId(auctionDB.getIdAuction(),secondBidderDB.getIdBidderEntity(),idAuctionItem);
        Optional<AuctionItemBidder> firstAuctionItemBidder =  auctionItemBidderDAO.findById(firstAuctionItemBidderId);
        Optional<AuctionItemBidder> secondAuctionItemBidder =  auctionItemBidderDAO.findById(secondAuctionItemBidderId);
        if (firstAuctionItemBidder.isPresent() && secondAuctionItemBidder.isPresent()){
            HttpClient client = HttpClient.newHttpClient();
            JSONObject registerBuenaProData = new JSONObject();
            registerBuenaProData.put("datetimeReg",buenaProRegisterDatetime);
            registerBuenaProData.put("firstBidderEntityCode",firstBidderDB.getCode());
            registerBuenaProData.put("secondBidderEntityCode",secondBidderDB.getCode());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:5000/register-buena-pro/"+auctionDB.getCode()+"/"+auctionItemDB.getGoodServiceType().getCode()))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(registerBuenaProData.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == HttpStatus.OK.value()){
                firstAuctionItemBidder.get().setRanking(1);
                secondAuctionItemBidder.get().setRanking(2);
                AuctionItemBidder firstAuctionItemBidderUpdated = auctionItemBidderDAO.save(firstAuctionItemBidder.get());
                AuctionItemBidder secondAuctionItemBidderUpdated = auctionItemBidderDAO.save(secondAuctionItemBidder.get());
                return firstAuctionItemBidderUpdated!=null && secondAuctionItemBidderUpdated!=null;
            }
            return false;
        }
        else return false;

    }

    @Override
    public List<AuctionBuenaProDetail> getAuctionBuenaProInfo(String auctionCode) throws URISyntaxException, IOException, InterruptedException, JSONException {
        List<AuctionBuenaProDetail> auctionBuenaProDetailList = new ArrayList<AuctionBuenaProDetail>();
        Auction auctionDB = auctionDAO.findByCode(auctionCode);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/get-buenapro-info-auction/"+auctionCode))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray responseJson = new JSONArray(response.body());

        JSONObject jsonObj;
        for (int i=0; i < responseJson.length(); i++) {
            jsonObj =  responseJson.getJSONObject(i);
            AuctionBuenaProDetail auctionBuenaProDetail =  new AuctionBuenaProDetail();
            String itemCode = (String)jsonObj.get("CodigoBienServicio");
            AuctionItem auctionItemDB =  auctionItemDAO.findByAuctionCode(auctionCode);
            auctionBuenaProDetail.setItemCode(itemCode);
            auctionBuenaProDetail.setItemName(auctionItemDB.getName());
            auctionBuenaProDetail.setIdAuctionItem(auctionItemDB.getIdAuctionItem());
            auctionBuenaProDetail.setBuenaProRegistered((Boolean)jsonObj.get("BuenaProOtorgada"));

            List<AuctionBuenaProDetail.BidderInfo> bidders = new ArrayList<AuctionBuenaProDetail.BidderInfo>();

            JSONArray postoresArr = (JSONArray)jsonObj.get("Postores");
            JSONObject postorObj;
            for (int j=0; j < postoresArr.length(); j++) {
                postorObj =  postoresArr.getJSONObject(j);
                AuctionBuenaProDetail.BidderInfo bidder = new AuctionBuenaProDetail.BidderInfo();
                String bidderCode = (String)postorObj.get("CodigoPostor");
                bidder.setBidderEntityCode(bidderCode);
                BidderEntity bidderEntityDB = bidderEntityDAO.findByCode(bidderCode);
                bidder.setBidderEntityName(bidderEntityDB.getName());
                bidder.setBestBid((float)postorObj.getDouble("MejorOferta"));
                bidder.setBestBidDatetimeReg((String)postorObj.get("FechaRegMejorOferta"));
                bidder.setRanking((Integer) postorObj.get("OrdenMerito"));
                bidders.add(bidder);
            }
            Collections.sort(bidders, new SortBidderInfo());
            auctionBuenaProDetail.setBidders(bidders);
            auctionBuenaProDetailList.add(auctionBuenaProDetail);
        }


        return auctionBuenaProDetailList;


    }

    @Override
    public String getStateEntityUsernameByAuctionCode(String auctionCode) {
        String stateEntityCode = auctionDAO.findByCode(auctionCode).getStateEntity().getCode();
        RabUser rabUser = rabuserDAO.findByStateEntityCode(stateEntityCode);
        return rabUser.getUsername();
    }


}
