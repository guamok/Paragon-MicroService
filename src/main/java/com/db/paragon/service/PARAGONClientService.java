package com.db.paragon.service;

import java.net.URISyntaxException;

import java.util.concurrent.CompletableFuture;

/**
 * Created by rribes on 12/10/2018
 */
public interface PARAGONClientService {

    void getFullParagon() throws URISyntaxException, InterruptedException;
    

	CompletableFuture<Boolean> callFullParagonXref() throws InterruptedException;
	CompletableFuture<Boolean> callFullParagonXref2() throws InterruptedException;
	CompletableFuture<Boolean> callFullParagonXref3() throws InterruptedException;
	CompletableFuture<Boolean> callFullParagonXref4() throws InterruptedException;
	

	CompletableFuture<Boolean> callFullParagonPartyIndustry() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullParagonCrmPartyAttrs() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullParagonCrmPartyContact() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullParagonCrmPartyCreditArea() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullParagonParty() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullPartyIdentifiers() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullPartyHierarchy() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullParagonCrmPartyRatings() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullPartyAddress() throws InterruptedException;
	
	CompletableFuture<Boolean> callFullPartyExtAttritutes() throws InterruptedException;
	
	
	
	
}
