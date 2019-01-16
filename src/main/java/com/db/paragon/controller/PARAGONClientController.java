package com.db.paragon.controller;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.paragon.model.PartyParagon;
import com.db.paragon.service.PARAGONClientService;

/**
 * Created by rribes on 22/10/2018.
 */
@RestController
@RequestMapping("/")
public class PARAGONClientController {

    
    @Autowired
    PARAGONClientService paragonClientService;
    
    private static final Logger LOGGER = LogManager.getLogger(PARAGONClientController.class);

  
    @GetMapping(value = "getFullParagon", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartyParagon> getFullParagon() throws URISyntaxException, InterruptedException {
    	
    	
    	paragonClientService.getFullParagon();//conexion,truncte tables, etc, certificado
//    	LOGGER.error("1- LLAMADA llamadas asincronas!!!!");
//    	CompletableFuture<Boolean> uno = paragonClientService.callFullParagonXref();
//    	
//    	LOGGER.error("2BIS- LLAMADA llamadas asincronas!!!!");
//    	CompletableFuture<Boolean> doce = paragonClientService.callFullParagonXref2();
//    	
//    	LOGGER.error("3BIS- LLAMADA llamadas asincronas!!!!");
//    	CompletableFuture<Boolean> trece = paragonClientService.callFullParagonXref3();
//    	
//    	LOGGER.error("4BIS- LLAMADA llamadas asincronas!!!!");
//    	CompletableFuture<Boolean> catorce = paragonClientService.callFullParagonXref4();
    	
    	
    	LOGGER.error("2- LLAMADA llamadas asincronas!!!!!!");
    	CompletableFuture<Boolean> dos = paragonClientService.callFullParagonPartyIndustry(); // New, WORKS WELL
    	LOGGER.error("3- LLAMADA llamadas asincronas!!!!!!");
    	CompletableFuture<Boolean> tres = paragonClientService.callFullParagonCrmPartyAttrs(); //graba 
    	LOGGER.error("4- LLAMADA llamadas asincronas!!!!!!!");
    	CompletableFuture<Boolean> cuatro = paragonClientService.callFullParagonCrmPartyContact();
    	LOGGER.error("5- LLAMADA llamadas asincronas !!!!!!");
    	CompletableFuture<Boolean> cinco = paragonClientService.callFullParagonCrmPartyCreditArea();
    	
    	LOGGER.error("6- LLAMADA llamadas asincronas PARTY!!!!!!");
    	CompletableFuture<Boolean> seis = paragonClientService.callFullParagonParty();
    	
    	LOGGER.error("7- LLAMADA llamadas asincronas callFullPartyHierarchy!!!!!!");
    	CompletableFuture<Boolean> siete = paragonClientService.callFullPartyHierarchy();
    	
    	LOGGER.error("8- LLAMADA llamadas asincronas!!!!!!");
    	CompletableFuture<Boolean> ocho = paragonClientService.callFullPartyIdentifiers();  // WORKS WELL
    	
    	LOGGER.error("9- LLAMADA llamadas asincronas!!!!!!");
    	CompletableFuture<Boolean> nueve = paragonClientService.callFullParagonCrmPartyRatings(); //WORKS WELL
    	
    	LOGGER.error("10- LLAMADA llamadas asincronas!!!!!!");
    	CompletableFuture<Boolean> ten = paragonClientService.callFullPartyAddress();
    	
    	LOGGER.error("11- LLAMADA llamadas asincronas!!!!!!");
    	CompletableFuture<Boolean> eleven = paragonClientService.callFullPartyExtAttritutes();
    	
    	
    	
    	
    	LOGGER.error("Now It must wait to the all threads finished");
    	//TODO: 
    	//CompletableFuture.allOf(uno,dos,tres,cuatro,cinco,seis,siete,ocho,nueve,ten,eleven,doce,trece,catorce);
    	CompletableFuture.allOf(dos,tres,cuatro,cinco,seis,siete,ocho,nueve,ten, eleven);
    	LOGGER.error("Passed CompletableFuture.allOf(uno,dos,tres,cinco,seis,siete,ocho,nueve,doce,trece,catorce); ");
    	
        return new ResponseEntity<PartyParagon>(HttpStatus.OK);
    }
    
//    @GetMapping(value = "getBusinessParagon", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<PartyParagon> getBusinessParagon() throws URISyntaxException {
//    	paragonClientService.getBusinessParagon();
//        return new ResponseEntity<PartyParagon>(HttpStatus.OK);
//    }
    
    
    
    
}
