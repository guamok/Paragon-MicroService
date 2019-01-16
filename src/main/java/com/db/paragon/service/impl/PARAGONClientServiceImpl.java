package com.db.paragon.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.db.paragon.model.PartyAddressParagon;
import com.db.paragon.model.PartyAttrParagon;
import com.db.paragon.model.PartyCrmContactParagon;
import com.db.paragon.model.PartyCrmCreditAreaParagon;
import com.db.paragon.model.PartyCrmRatingParagon;
import com.db.paragon.model.PartyCrmXrefParagon;
import com.db.paragon.model.PartyExtParagon;
import com.db.paragon.model.PartyHierarchyParagon;
import com.db.paragon.model.PartyIdentifierParagon;
import com.db.paragon.model.PartyIndustryParagon;
import com.db.paragon.model.PartyParagon;
import com.db.paragon.repository.PartyAddressRepository;
import com.db.paragon.repository.PartyCrmAttrRepository;
import com.db.paragon.repository.PartyCrmContactRepository;
import com.db.paragon.repository.PartyCrmCreditAreaRepository;
import com.db.paragon.repository.PartyCrmRatingRepository;
import com.db.paragon.repository.PartyCrmXrefRepository;
import com.db.paragon.repository.PartyExtAttrRepository;
import com.db.paragon.repository.PartyHierarchyRepository;
import com.db.paragon.repository.PartyIdentifierRepository;
import com.db.paragon.repository.PartyIndustryRepository;
import com.db.paragon.repository.PartyRepository;
import com.db.paragon.repository.RunRepository;
import com.db.paragon.service.PARAGONClientService;

/**
 * Created by rribes on 22/09/2018.
 */

@Service
@Component
public class PARAGONClientServiceImpl implements PARAGONClientService  {
	
	
  
    @Autowired
    private RunRepository runRepository;
    
    
    @Autowired
    private PartyCrmXrefRepository paragonPartyCrmXrefRepository;
    
    @Autowired
    private PartyCrmRatingRepository paragonPartyCrmRatingRepository;
    
    @Autowired
    private PartyCrmAttrRepository paragonPartyCrmAttrRepository;
    
    @Autowired
    private PartyCrmContactRepository paragonPartyCrmContactRepository;
    
    @Autowired
    private PartyCrmCreditAreaRepository paragonPartyCrmCreditAreaRepository;
    
    @Autowired
    private PartyIndustryRepository partyIndustryRepository;
    
    @Autowired
    private PartyRepository partyRepository;
    
    @Autowired
    private PartyIdentifierRepository partyIdentifierRepository;
    
    @Autowired
    private PartyHierarchyRepository partyHierarchyRepository;
    
    @Autowired
    private PartyAddressRepository partyAddressRepository;
    
    
    @Autowired
    private PartyExtAttrRepository partyExtAttribRepository;
    
    
    

    
    private boolean isDelta         				 		 = true;
    //PARAGON
    private static final String PARTY_XREF 					 = "Party XREF(Mapping)";
    private static final String PARTY_INDUSTRY 				 = "Party Industry";
    private static final String CRM_RATINGS 				 = "Crm Ratings";
    private static final String CRM_ATTRS 					 = "Crm Attrs";
    private static final String CRM_CREDIT_AREA 			 = "Credit Area";
    private static final String CRM_CONTACTS 				 = "Contacts";
    private static final String PARTY         			 	 = "Party";
    private static final String PARTY_IDENTIFIERS 			 = "Party Identifiers";
    private static final String PARTY_HIERARCHY    			 = "Party Hierarchy";
    private static final String PARTY_ADDRESS    			 = "Party Address";
    private static final String PARTY_EXT_ATTRIBUTES    	 = "Ext Attributes";
    
    
    
//    private static final String DELTA         				 = "Delta";
//    private static final String PART_DELTA         			 = "Part Delta";
//    private static final String FULL         			 	 = "Full";
//    private static final String PART_FULL         			 = "Part Full";
    
    private static final String  PARAGON_FULL      = "PARAGON LOAD-FULL";
    private static final String  PARAGON_DELTA     = "PARAGON LOAD-DELTA";
    private static final String  JAVA_PROCESS      = "Java Process";
    
    
    private static final String  COMPLETE = "C";
    private static final String  FAILED   = "F";
    //private static final String  STARTED  = "S";
    
    
    private Long findCurrentId; 

    private static final Logger LOGGER = LogManager.getLogger(PARAGONClientServiceImpl.class);
    
    
    
    @Value("${resource.certificate}")
    private  String CERTIFICATE;
    
    
    @Value("${ws.gcix.base_url}")
    private  String BASE_URL;
    
    @Value("${url.changed.id.between.dates}")
    private  String URL_CHANGED_ID_BETWEEN_DATES;
    
    @Value("${party.base.url}") // Party/
    private  String PARTY_BASE_URL;
    
    @Value("${crm.base.url}") // Crm/
    private  String CRM_BASE_URL;
    
    @Value("${issuer.rating.base.url}")
    private  String ISSUER_RATING_BASE_URL;
    
    @Value("${industry.classification.base.url}")
    private  String INDUSTRY_CLASSIFICATION_BASE_URL;
    
    
    @Value("${account.xref.base.url}")
    private  String ACCOUNT_XREF_BASE_URL;
    
    @Value("${time.sleep.stop}")
    private  long timeStoping;
    
    @Value("${time.sleep.range}")
    private  long timeRange;
    
    
   
    @Value("${full.max.items}")
    private int FULL_MAX_ITEMS;
    
 
    
    @Value("${delta.max.items}")
    private int DELTA_MAX_ITEMS;
    
    @Value("${delta.number.month.between.dates}")
    private int DELTA_NUMBER_MONTH_BET_DATES;

    
    @Value("${spring.datasource.username}")
    private String username;
    
    // RELATIVE LINKS FOR cDIF
    @Value("${service.relative.link.crm.crmpartyxref}")
    private  String RELINKCRMPARTYXREF;
    
    @Value("${service.relative.link.crm.crmpartycontact}")
    private  String RELINKCRMPARTYCONTACT;
    
    @Value("${service.relative.link.crm.crmpartycreditarea}")
    private  String RELINKCRMPARTYCREDITAREA;
    
    @Value("${service.relative.link.crm.crmpartyattrs}")
    private  String RELINKCRMPARTYATTRS ;
    
    @Value("${service.relative.link.crm.crmpartyratings}")
    private  String RELINKCRMPARTYRATINGS ;
    
    @Value("${service.relative.link.party.partyindustry}")
    private  String RELINKPARTYINDUSTRY ;
    
    @Value("${service.relative.link.party.party}")
    private  String RELINKPARTY;
    
    @Value("${service.relative.link.party.partyaltid}")
    private  String RELINKPARTYALTID ;
    
    @Value("${service.relative.link.party.partyhierarchy}")
    private  String RELINKPARTYHIERARCHY;
    
    @Value("${service.relative.link.party.partyaddress}")
    private  String RELINKPARTYADDRESS;
    
    @Value("${service.relative.link.party.partyextattributes}")
    private  String RELINKPARTYEXTATTRIBUTES;
    
    
    
    
    
    private String base64encodedCert;

    private String startDate;

    private String endDate;

    private HttpEntity<Object> requestEntity;


    
    private String getCurrentlyDateForDataBase(){
    	
   	 DateTime dt = new DateTime();
        DateTimeFormatter dateTimeFormater = DateTimeFormat.forPattern("dd-MMM-yy"); 
        //SimpleDateFormat sDateFormat = new SimpleDateFormat("dd-MMM-yy"); 
        //String d = dateTimeFormater.print(new DateTime().minusMonths(1)); a month
        String d = dateTimeFormater.print(new DateTime().minusMonths(DELTA_NUMBER_MONTH_BET_DATES));
        return d;
   }
    

  
    /**
     * Finished OK 
     * 
     * @param delta
     */
    private void importFinishedSuccessFully(boolean delta) {
    	
    	LOGGER.error(" In importFinishedSuccessFully" );
        if(delta){
        	LOGGER.error("Populated to GCIX database all information from PARAGON." );
        	runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Populated to GCIX database all information from PARAGON.", findCurrentId);
        	//Now, call to oracle
        	try{
        		//LOGGER.error(" Calling to  procedurePARAGONLoadDelta" );
        		
        		runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Calling to CRDS_LOAD_DELTA storeprocedure from java process....", findCurrentId);
        		boolean isOkCrdsDelta = runRepository.procedureCrdsLoadDelta();
        		if(isOkCrdsDelta){
        			
        			LOGGER.error(" The CrdsLoadDelta procedure on database worked fine." );
        			runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "The CRDS_LOAD_DELTA storeprocedure on database worked fine.", findCurrentId);
        			runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Calling to dm_pkg.do_crds_mapping_delta_load storeprocedure from java process....", findCurrentId);
        			
        			boolean isOkCrdsMapping = runRepository.procedureCrdsLoadDeltaMapping();
        			
        			if(isOkCrdsMapping){
        				runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "The dm_pkg.do_crds_mapping_delta_load storeprocedure on database worked fine.", findCurrentId);
        				runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Putting status JOB to Complete.", findCurrentId);
            			LOGGER.error(" The CrdsLoadDeltaMapping procedure on database worked fine." );
            			runRepository.procedureUpdate_job_log(findCurrentId,COMPLETE,PARAGON_DELTA); 
            			
            		}else{
            			LOGGER.error(" The CrdsLoadDeltaMapping procedure on database has failed." );
            		}
        			
        			
        			// dm_pkg.do_crds_mapping_delta_load;
        			//TODO: Calls David
        			
        		}else{
        			LOGGER.error(" The CrdsLoadDelta procedure on database has failed." );
        		}
        		
        		
        		LOGGER.error(" finished procedureCrdsLoadDelta." );
        	}catch(Exception e){
        		LOGGER.error(" procedureCrdsLoadDelta ; Exception e: " +e );
        	}
        		
        	
        	
        }else{
        	LOGGER.error(" Finished OK FULL PROCESS!! " );
        	runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Finished OK FULL PROCESS from JAVA.", findCurrentId);
        	//runRepository.procedureUpdate_job_log(findCurrentId,COMPLETE,PARAGON_FULL);
        }
    }
    
    /**
     * If there is a part doesnt work well, so abort the process, then try again in few minutes.
     * 
     * 
     * @param delta
     * @param jTable
     */
    private void savePartialFinishedSuccessFully(boolean delta, String jTable, boolean isOK) {
    	//LOGGER.error(" In savePartialFinishedSuccessFully" );
    	
        // setCode
        if(isOK){ // OK? Go ahead!
        	//
        	if(isDelta){
        		LOGGER.error(jTable + " on Delta has been satisfactorily. Go ahead with the next table." );
        		runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, jTable+" has been populated sucessfully.", findCurrentId);
        	}else{
        		LOGGER.error(jTable + " on FULL satisfactorily. Go ahead with the next table." );
        		runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, jTable+" has been populated sucessfully.", findCurrentId);
        	}
        }else{ // NotOK, save errors in database and stop process.
        	
        	if(isDelta){
        		LOGGER.error("Finished  Delta unsatisfactorily. Please try again after several minutes." );
        		runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, jTable+" has been populated unsatisfactorily. Please try again after a few minutes.", findCurrentId);
        		runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_DELTA);
        	}else{
        		LOGGER.error("Finished Full unsatisfactorily. Please try again after several minutes." );
        		runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, jTable+" has been populated unsatisfactorily. Please try again after a few minutes.", findCurrentId);
        		runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_FULL);
        	}
        	
        	
        	// Save ON Stats
        	
        	// shoot save total unsatisfactorily and close the program.
        	 LOGGER.error("ERROR: Please check database STATS and Update job log tables for seeing the ERRORS.");
         	 LOGGER.error("ERROR: Anyway, you can shoot more later, it can works well, is possible that the error has been for connectivity with web service.");
         	
         	 //exitApplication(-1);
        }
    }
    
   
    
//    private boolean setCurrentId() {
//    	LOGGER.error("In setCurrentId ");
//    	try{
//    		findCurrentId = runRepository.findCurrentId();
//    		LOGGER.error("Current Id from database: " + findCurrentId.toString());
//    		
//    	}catch(Exception e ){
//    		LOGGER.error("Exception setting current id. Exception: " );
//    		if(isDelta){
//    			runRepository.procedureUpdate_job_log(new Long(-1),FAILED,PARAGON_DELTA );
//    			runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Hasn't get from Java application Id's Process. You'll check if this exists or create other one.", new Long(-1));
//    			return false;
//    			//exitApplication(-1);
//        	}else{
//        		runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Hasn't get from Java application Id's Process. You'll check if this exists or create other one."+ e, new Long(-1));
//        		runRepository.procedureUpdate_job_log(new Long(-1),FAILED,PARAGON_FULL );
//        		return false;
//        		//exitApplication(-1);
//        	}
//    		
//    	}
//    	return true;
//    }
    
    
    /**
     * setDates() 
     * 
     * Only For DELTA process.
     */
    private void setDates() {

        DateTime dt = new DateTime();
        DateTimeFormatter dateTimeFormater = DateTimeFormat.forPattern("YMMdd"); //
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd-MMM-yy"); //
        //LOGGER.error("START DATE con JODA TIME ---- " + dateTimeFormater.print(dt) + "------" + "END DATE ---- " + dateTimeFormater.print(dt.minusMonths(1)));

        LOGGER.error("PARAMS QUERY ---- DATE: " + dt.toDate() );
        String sDate = runRepository.findLastSuccessfulDate();
        LOGGER.error("Ahora la date es  ---- DATE: " + sDate.toString() );
        Date date= null;
        
        try {
			date = sDateFormat.parse(sDate);
		} catch (ParseException e) {
			LOGGER.error(" ERROR: Parsing the date. Anyway, puting dates." );
			this.startDate = dateTimeFormater.print(new DateTime().minusMonths(1));
			LOGGER.error("Fixed: Start date it will be:  " + this.startDate );
			this.endDate = dateTimeFormater.print(new DateTime());
			LOGGER.error("Fixed: End date it will be:  " + this.endDate );
			runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "ERROR: Parsing the date, maybe there isn't date from database for getting. Anyway, puting dates, Start date is:  "+this.startDate+" and End Date is:"+ this.endDate , findCurrentId);
		} 
        
        if(date != null){
        	//LOGGER.error("  DATE != null:  " + date );
            this.startDate = dateTimeFormater.print(new DateTime(date));
            //LOGGER.error(" this.startDate:  " +  this.startDate );
            this.endDate = dateTimeFormater.print(new DateTime());
            LOGGER.error(" START DATE  ---- " + this.startDate + "------" + "END DATE ---- " + this.endDate);
            runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, " Dates for DELTA process: start date is:  "+this.startDate+" and End Date is:"+ this.endDate , findCurrentId);
        } else {
        	//LOGGER.error(" ELSE DATE == null:  " );
            this.endDate = dateTimeFormater.print(new DateTime());
            this.startDate = dateTimeFormater.print(new DateTime().minusMonths(1));
            LOGGER.error(" NULL - START DATE  ---- " + this.startDate + "------" + "END DATE ---- " + this.endDate);
            runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Dates for DELTA process: Start date is:  "+this.startDate+" and End date is:"+ this.endDate , findCurrentId);
        }
    }

    //Initialization methods
    private void createRequestHttpEntity(String cert){
    	LOGGER.error("In createRequestHttpEntity....");
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("X-AUTH-TOKEN", cert);
        this.requestEntity = new HttpEntity<>(httpHeaders);
    }

    
    public String getPasswordUsingEnvironment(Environment environment){
		return environment.getProperty("spring.datasource.password");
    	
    }
    
    private void loadCertificate() {
    	
        try {
        	URI uri = new URI("file:"+ CERTIFICATE);
        	
            File file = new File(uri);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputStream.read(bytes);

            //base64 encode the certificate and set on the X-AUTH-TOKEN HTTP header
            this.base64encodedCert = Base64.getEncoder().encodeToString(bytes);
            LOGGER.error("base64encodedCert");
        } catch (IOException e) {
        	LOGGER.error("IOException --- " + e.getMessage());
        	if(isDelta){
        		runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "ERROR: Problems with Certificate, IOException: "+ e, findCurrentId);
        		runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_DELTA ); 
        	}else{
        		runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, "ERROR: Problems with Certificate, IOException: "+ e, findCurrentId);
        		runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_FULL );
        	}
        } catch (URISyntaxException e) {
            LOGGER.error("URISyntaxException --- " + e.getMessage());
            if(isDelta){
        		runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "ERROR: Problems with Certificate, URISyntaxException: "+ e, findCurrentId);
        		runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_DELTA ); 
        	}else{
        		runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, "ERROR: Problems with Certificate, URISyntaxException: "+ e, findCurrentId);
        		runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_FULL );
        	}
        }
    }

    

    private boolean truncateTables () {
    	
    	LOGGER.error("Truncate Tables. ");
    	
    	boolean trunTables =runRepository.procedureTruncateTables();
    	if(!trunTables){
    		LOGGER.error("ERROR Truncating Tables. The process can not continue. The job num:"+findCurrentId+", please see in database.");
    		runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Truncate tables is not working fine. ", findCurrentId);
    		runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_DELTA);
    		return false;
    	}
    	LOGGER.error("Finished Truncate Tables.");
    	return true;
    	
    }

       
    
    // PARAGON************************************************************************************************
    @Transactional(timeout = 1000)
    public void storePartyCrmXrefParagon(List<PartyCrmXrefParagon> lpartyCrmXrefParagon) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing storePartyCrmXrefParagon");
    	LOGGER.error("size storePartyCrmXrefParagon: "+lpartyCrmXrefParagon.size());
    	paragonPartyCrmXrefRepository.save(lpartyCrmXrefParagon);
    	paragonPartyCrmXrefRepository.flush();
    	paragonPartyCrmXrefRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of storePartyCrmXrefParagon took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyCrmRatings(List<PartyCrmRatingParagon> ratings) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing ratings");
    	LOGGER.error("size ratings:"+ratings.size());
    	paragonPartyCrmRatingRepository.save(ratings);
    	paragonPartyCrmRatingRepository.flush();
    	paragonPartyCrmRatingRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of storePartyCrmRatings took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyCrmAttr(List<PartyAttrParagon> attr) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing Attr");
    	LOGGER.error("size attr:"+attr.size());
    	paragonPartyCrmAttrRepository.save(attr);
    	paragonPartyCrmAttrRepository.flush();
    	paragonPartyCrmAttrRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of storePartyCrmAttr took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyCrmContact(List<PartyCrmContactParagon> contacts) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing contacts");
    	LOGGER.error("size attr:"+contacts.size());
    	paragonPartyCrmContactRepository.save(contacts);
    	paragonPartyCrmContactRepository.flush();
    	paragonPartyCrmContactRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of contacts took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyCrmCreditArea(List<PartyCrmCreditAreaParagon> creditArea) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing creditArea");
    	LOGGER.error("size attr:"+creditArea.size());
    	paragonPartyCrmCreditAreaRepository.save(creditArea);
    	paragonPartyCrmCreditAreaRepository.flush();
    	paragonPartyCrmCreditAreaRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of credit Area took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyIndustry(List<PartyIndustryParagon> partyIndustry) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing Party Industry");
    	LOGGER.error("size attr:"+partyIndustry.size());
    	partyIndustryRepository.save(partyIndustry);
    	partyIndustryRepository.flush();
    	partyIndustryRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of party industry took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storeParty(List<PartyParagon> party) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing Party ");
    	LOGGER.error("size attr:"+party.size());
    	partyRepository.save(party);
    	partyRepository.flush();
    	partyRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of Party took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyIdentifiers(List<PartyIdentifierParagon> idenfitiers) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing Party Identifiers");
    	LOGGER.error("size attr:"+idenfitiers.size());
    	partyIdentifierRepository.save(idenfitiers);
    	partyIdentifierRepository.flush();
    	partyIdentifierRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of Identifiers took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyHierarchy(List<PartyHierarchyParagon> hierarchy) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing Party hierarchies");
    	LOGGER.error("size attr:"+hierarchy.size());
    	partyHierarchyRepository.save(hierarchy);
    	partyHierarchyRepository.flush();
    	partyHierarchyRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of Hierarchy took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    @Transactional(timeout = 1000)
    public void storePartyAddress(List<PartyAddressParagon> address) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing Party Address");
    	LOGGER.error("size attr:"+address.size());
    	partyAddressRepository.save(address);
    	partyAddressRepository.flush();
    	partyAddressRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of Address took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    @Transactional(timeout = 1000)
    public void storePartyExtAttributes(List<PartyExtParagon> extAttribute) {
    	
    	Instant previous, current;
    	previous = Instant.now();
    	LOGGER.error("Storing Party ExtAttributes");
    	LOGGER.error("size attr:"+extAttribute.size());
    	partyExtAttribRepository.save(extAttribute);
    	partyExtAttribRepository.flush();
    	partyExtAttribRepository.clear();
        current = Instant.now();
        LOGGER.error("Insertion of ExtAttr took " + ChronoUnit.MILLIS.between(previous, current) + " ms");
    }
    
    
    
    
    
 
 /**
  * 
  * 
  * 
  * @param exception
  * @param call : What table is implicated?
  */
 	private void exceptionGenericCallCDIF(String exception, String call){
 		
 		if(exception!=null){
 			LOGGER.error("ERROR:A ERROR has ocurred while getting the response (http) from CDIF'S PARAGON. Error: "+exception);
 			LOGGER.error("ERROR: Implicated table: "+call);
 		}
       	//LOGGER.error("ERROR: Response from PARAGON has been: "+ response.getBody().toString());
 		
       	try{
       		if(isDelta){
       			runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "ERROR:A error has ocurred while trying the response (http) from CDIF'S PARAGON. Error: "+ exception, findCurrentId);
       			runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_DELTA); 
       		}else{
       			runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, "ERROR:A ERROR has ocurred while trying the response (http) from CDIF'S PARAGON. Error: "+ exception , findCurrentId);
       			// 	In FULL case, try back the call
       			//runRepository.procedureUpdate_job_log(findCurrentId,FAILED,PARAGON_FULL );
       		}
       	}catch(Exception e){
     			LOGGER.error("ERROR: While saving in database. Exit application. ");
     			//exitApplication(-1);
     	}
 	}
 	
 	/**
 	 * 
 	 *  Only save on state, don't break the program.
 	 * 
 	 * @param call : What table is implicated?
 	 * @param responseBody : Response from PARAGON cDIF.
 	 */
 	private void exceptionResponseNot200(String call, String responseBody){
 		
 		if(responseBody!=null){
 			LOGGER.error("The response (http) has been: "+responseBody);
 		}
 		
 		try{
 			if(isDelta){
 				runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, call+": Response: "+responseBody, findCurrentId);
 			}else{
 				runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, call+"Response: "+responseBody , findCurrentId);
 			}
 		}catch(Exception e){
 			LOGGER.error("ERROR: While saving in database. Exit application. ");
 			//exitApplication(-1);
 		}
		
 	}
 	
 	/**
 	 * Only save on state, don't break the program.
 	 * 
 	 * exception: Exception raised.
 	 * @param call : What table is implicated?
 	 * @param exception
 	 */
 	private void exceptionSaveDatabase(String call, String exception){
 		
 		if(call!=null){
 			LOGGER.error("ERROR: Some problem while storing "+call);
 		}
 		
 		try{
 			if(isDelta){
 				runRepository.procedureWrite_Import_Stat(PARAGON_DELTA, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Exception while storing in database: "+call+" .Exception:"+exception, findCurrentId);
 			}else{
 				runRepository.procedureWrite_Import_Stat(PARAGON_FULL, getCurrentlyDateForDataBase(), JAVA_PROCESS, "Exception while storing in database: "+call+" .Exception:"+exception , findCurrentId);
 			}
 		}catch(Exception e){
 			LOGGER.error("ERROR: While saving in database. Exit application. ");
 			//exitApplication(-1);
 		}
		
 	}

 	
 	@Override
	public void getFullParagon() throws URISyntaxException, InterruptedException {
 		
 		//Long id = 1l;
    	LOGGER.error("In getFullParagon");
        //Instant previous, current;
        //Read the certificate
        loadCertificate();
        //initialize request http entity
        LOGGER.error("pass certificate.....");
        createRequestHttpEntity(this.base64encodedCert);
        LOGGER.error("pass createRequestHttpEntity.....");
    	  
        
        //truncate ALL tables before inserting
        LOGGER.error("NOOO Truncate tables PARAGON");
        truncateTables(); 
        LOGGER.error("Truncated tables.");

        
        	
       	//LOGGER.error("Call web service getFullParagon");
        	
        	
        	
        	
     
        	
        	// 	CALLS TO WS and Store on DBB
        	
        	//LOGGER.error("************* Begin  PARTY_XREF ************* " );
//        	CompletableFuture<Boolean> uno = callFullParagon(PARTY_XREF);
//        	CompletableFuture<Boolean> dos = callFullParagon500(PARTY_XREF);
//        	
//        	CompletableFuture.allOf(uno,dos);
        	
        	//savePartialFinishedSuccessFully(true, PARTY_BASE,isOk_base);
        	//LOGGER.error("CONTINUAAAAA???? ");
        	//LOGGER.error("************* Ended  PARTY_XREF ************* " );
        	//LOGGER.error("Finished to print PARAGON PartyBase.");
        	
        
//        if(isOk_indClassification && isOk_partyAddres && isOk_accXref && isOk_identifiers && isOk_hierarchy && isOk_base && isOk_base && isOk_issuerrating){
//        	importFinishedSuccessFully(true);
//        }else{
//        	LOGGER.error("ERROR: Please check database for seeing the ERRORS.");
//        	LOGGER.error("ERROR: Party Industry Delta: "+ isOk_indClassification + ", Party Address Delta: "+isOk_partyAddres+
//        			" Party AccountXref Delta: "+isOk_accXref+" Party Identifiers Delta: "+isOk_identifiers+" Party Hierarchy Delta: "+ isOk_hierarchy+" Party Base Delta: "+isOk_base+" Party Issuerrating Delta: "+isOk_issuerrating);
//        	LOGGER.error("ERROR: Anyway, you can shoot more later, it can works well, it can be problems the connectivity with web service.");
//        	LOGGER.error("Finished web service DELTA NOTOK.");
//        	// Never it should enter here so ONLY dap's logs	importFinishedSuccessFully(false);
//        }
        
     
 	}
 	
 	
 	
 	
 	
 	
 	

 	
 	
 	/**
 	 * PD_STG_CRM_PARTY_XREF (mapping)
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonXref() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonXref(jTable) "+PARTY_XREF);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyCrmXrefParagon> 	 _xref 	  = new ArrayList<PartyCrmXrefParagon>();
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        	
        		errors=false;
        		//failed=false;
        	
  					do{
  						try{
        							_xref.clear();
        							query=null;
        				
        							LOGGER.error("In PARTY_XREF ");
        							query = BASE_URL + RELINKCRMPARTYXREF +FULL_MAX_ITEMS+"&offset="+page;
        							LOGGER.error("query: "+query);
        							//LOGGER.error("query2: "+query2);
        							//	query = BASE_URL + PARTY_BASE_URL +"Base/1.0.0?exp=gt(PartyId%2C1)&order=PartyId&limit="+FULL_MAX_ITEMS+"&offset="+page;
        							_xref = callParagonCrmXref(query);
        							
        							LOGGER.error("Get MAPPING(XREF), page: "+page+", size: "+_xref.size());
        							
        							if(_xref.size() !=0 ){
        								LOGGER.error("saving in database............");
        								storePartyCrmXrefParagon(_xref); //PD_STG_CRM_PARTY_XREF
        								LOGGER.error("SAVED!!");
        							}
        							break;
        						
        				}catch (Exception e) {
        	        		
        						LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_XREF );
        						LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_XREF );
        						errors = true;
        	        		
        						try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+PARTY_XREF+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(PARTY_XREF, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(PARTY_XREF, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			 }while(  (errors && time < timeStoping));
        	        
				errors=false;
        		page++;
        		
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || (_xref.size() !=0  && page <= 250 ));
        
        LOGGER.error("PAGE in xref: ..."+ String.valueOf(page));
        
        LOGGER.error("FINISHED ..."+ PARTY_XREF );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL, with the first 300  -> "+PARTY_XREF);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 		
 	}
 	
 	
 	
 	/**
 	 * PD_STG_CRM_PARTY_XREF2 (mapping)
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonXref2() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonXref2() 251->500 "+PARTY_XREF);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        
        int page=251;
        List<PartyCrmXrefParagon> 	 _xref 	  = new ArrayList<PartyCrmXrefParagon>();
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        		do{
        				try{
        							_xref.clear();
        							query=null;
        				
        							LOGGER.error("In PARTY_XREF ");
        							query = BASE_URL + RELINKCRMPARTYXREF +FULL_MAX_ITEMS+"&offset="+page;
        							//query = "https://uat1.cdif-portal.risk.global.intranet.db.com/counterparty/dif/data/" + "Crm/CrmPartyXRef/1.0.0?exp=gt(ORG_ID%2C1)&attributes=ORG_ID%2CSOURCE_SYSTEM_NAME%2CPARTY_LEGAL_NAME%2CPARTY_SHORT_NAME%2CSOURCE_LOCATION_NAME%2CSOURCE_PARTY_ID%2CSOURCE_SYSTEM_CODE&limit="+FULL_MAX_ITEMS+"&offset="+page;
        							LOGGER.error("query: "+query);
        							//LOGGER.error("query2: "+query2);
        							//	query = BASE_URL + PARTY_BASE_URL +"Base/1.0.0?exp=gt(PartyId%2C1)&order=PartyId&limit="+FULL_MAX_ITEMS+"&offset="+page;
        							_xref = callParagonCrmXref(query);
        							
        							LOGGER.error("Get MAPPING(XREF), page: "+page+", size: "+_xref.size());
        							
        							if(_xref.size() !=0 ){
        								LOGGER.error("saving in database............");
        								storePartyCrmXrefParagon(_xref); //PD_STG_CRM_PARTY_XREF
        								LOGGER.error("SAVED!!");
        							}
        							break;
        						
        			}catch (Exception e) {
        	        		
        					LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_XREF );
        					LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_XREF );
        					errors = true;
        	        		try {
        							time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        							LOGGER.error("ERROR: Try again..."+PARTY_XREF+" , waiting: "+ time );
        							Thread.sleep(time); //4000 -> 4s
        							LOGGER.error("ERROR: time: "+ time );
        	        		
        	        		} catch (InterruptedException ed) {
        							LOGGER.error("Exception: "+ed);
        							return CompletableFuture.completedFuture(false);
        					}
        							
        					if(errors && (time >= timeStoping)){
        							
        						LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        						LOGGER.error(" ERROR is:   "+e.getMessage());
        						
//        						this.exceptionSaveDatabase(PARTY_XREF, "There is a ERROR and time has passed too, save in database the error.");
//        						this.exceptionSaveDatabase(PARTY_XREF, e.getMessage());
        							
        						return CompletableFuture.completedFuture(false);
        					}
        			}
        		}while(  (errors && time < timeStoping));
        	        
        				errors=false;
        				page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || (_xref.size() !=0 && page <= 500 ) );
        
        LOGGER.error("FINISHED ..."+ PARTY_XREF );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL 2 -> "+PARTY_XREF);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 		
 	}
 	
 	
 	/**
 	 * PD_STG_CRM_PARTY_XREF3 (mapping)
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonXref3() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonXref(jTable2) 501 -> 750 "+PARTY_XREF);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        
        int page=501;
        List<PartyCrmXrefParagon> 	 _xref 	  = new ArrayList<PartyCrmXrefParagon>();
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        		do{
        				try{
        							_xref.clear();
        							query=null;
        				
        							LOGGER.error("In PARTY_XREF ");
        							query = BASE_URL + RELINKCRMPARTYXREF +FULL_MAX_ITEMS+"&offset="+page;
        							//query = "https://uat1.cdif-portal.risk.global.intranet.db.com/counterparty/dif/data/" + "Crm/CrmPartyXRef/1.0.0?exp=gt(ORG_ID%2C1)&attributes=ORG_ID%2CSOURCE_SYSTEM_NAME%2CPARTY_LEGAL_NAME%2CPARTY_SHORT_NAME%2CSOURCE_LOCATION_NAME%2CSOURCE_PARTY_ID%2CSOURCE_SYSTEM_CODE&limit="+FULL_MAX_ITEMS+"&offset="+page;
        							LOGGER.error("query: "+query);
        							//LOGGER.error("query2: "+query2);
        							//	query = BASE_URL + PARTY_BASE_URL +"Base/1.0.0?exp=gt(PartyId%2C1)&order=PartyId&limit="+FULL_MAX_ITEMS+"&offset="+page;
        							_xref = callParagonCrmXref(query);
        							
        							LOGGER.error("Get MAPPING(XREF), page: "+page+", size: "+_xref.size());
        							
        							if(_xref.size() !=0 ){
        								LOGGER.error("saving in database............");
        								storePartyCrmXrefParagon(_xref); //PD_STG_CRM_PARTY_XREF
        								LOGGER.error("SAVED!!");
        							}
        							break;
        						
        			}catch (Exception e) {
        	        		
        					LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_XREF );
        					LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_XREF );
        					errors = true;
        	        		try {
        							time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        							LOGGER.error("ERROR: Try again..."+PARTY_XREF+" , waiting: "+ time );
        							Thread.sleep(time); //4000 -> 4s
        							LOGGER.error("ERROR: time: "+ time );
        	        		
        	        		} catch (InterruptedException ed) {
        							LOGGER.error("Exception: "+ed);
        							return CompletableFuture.completedFuture(false);
        					}
        							
        					if(errors && (time >= timeStoping)){
        							
        						LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        						LOGGER.error(" ERROR is:   "+e.getMessage());
        						
//        						this.exceptionSaveDatabase(PARTY_XREF, "There is a ERROR and time has passed too, save in database the error.");
//        						this.exceptionSaveDatabase(PARTY_XREF, e.getMessage());
        							
        						return CompletableFuture.completedFuture(false);
        					}
        			}
        		}while(  (errors && time < timeStoping));
        	        
        				errors=false;
        				page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || (_xref.size() !=0 && page <= 750 )  );
        
        LOGGER.error("FINISHED ..."+ PARTY_XREF );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL 3 -> "+PARTY_XREF);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 		
 	}
 	
 	
 	/**
 	 * PD_STG_CRM_PARTY_XREF4 (mapping)
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonXref4() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonXref(jTable2) 751 -> final "+PARTY_XREF);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        
        int page=751;
        List<PartyCrmXrefParagon> 	 _xref 	  = new ArrayList<PartyCrmXrefParagon>();
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        		do{
        				try{
        							_xref.clear();
        							query=null;
        				
        							LOGGER.error("In PARTY_XREF ");
        							query = BASE_URL + RELINKCRMPARTYXREF + FULL_MAX_ITEMS + "&offset="+page;
        							//query = "https://uat1.cdif-portal.risk.global.intranet.db.com/counterparty/dif/data/" + "Crm/CrmPartyXRef/1.0.0?exp=gt(ORG_ID%2C1)&attributes=ORG_ID%2CSOURCE_SYSTEM_NAME%2CPARTY_LEGAL_NAME%2CPARTY_SHORT_NAME%2CSOURCE_LOCATION_NAME%2CSOURCE_PARTY_ID%2CSOURCE_SYSTEM_CODE&limit="+FULL_MAX_ITEMS+"&offset="+page;
        							LOGGER.error("query: "+query);
        							//LOGGER.error("query2: "+query2);
        							//	query = BASE_URL + PARTY_BASE_URL +"Base/1.0.0?exp=gt(PartyId%2C1)&order=PartyId&limit="+FULL_MAX_ITEMS+"&offset="+page;
        							_xref = callParagonCrmXref(query);
        							
        							LOGGER.error("Get MAPPING(XREF), page: "+page+", size: "+_xref.size());
        							
        							if(_xref.size() !=0 ){
        								LOGGER.error("saving in database............");
        								storePartyCrmXrefParagon(_xref); //PD_STG_CRM_PARTY_XREF
        								LOGGER.error("SAVED!!");
        							}
        							break;
        						
        			}catch (Exception e) {
        	        		
        					LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_XREF );
        					LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_XREF );
        					errors = true;
        	        		try {
        							time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        							LOGGER.error("ERROR: Try again..."+PARTY_XREF+" , waiting: "+ time );
        							Thread.sleep(time); //4000 -> 4s
        							LOGGER.error("ERROR: time: "+ time );
        	        		
        	        		} catch (InterruptedException ed) {
        							LOGGER.error("Exception: "+ed);
        							return CompletableFuture.completedFuture(false);
        					}
        							
        					if(errors && (time >= timeStoping)){
        							
        						LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        						LOGGER.error(" ERROR is:   "+e.getMessage());
        						
//        						this.exceptionSaveDatabase(PARTY_XREF, "There is a ERROR and time has passed too, save in database the error.");
//        						this.exceptionSaveDatabase(PARTY_XREF, e.getMessage());
        							
        						return CompletableFuture.completedFuture(false);
        					}
        			}
        		}while(  (errors && time < timeStoping));
        	        
        				errors=false;
        				page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || _xref.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ PARTY_XREF );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL 4 -> "+PARTY_XREF);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 		
 	}
 	
 	/**
 	 * 
 	 * 
 	 * PD_STG_CRM_PARTY_CONTACT (Contacts)
 	 * 
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonCrmPartyContact() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonCrmPartyContact(jTable) "+CRM_CONTACTS);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyCrmContactParagon>  contacts  = new ArrayList<PartyCrmContactParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					contacts.clear();
        					query=null;
        		
        					LOGGER.error("In callFullParagonCrmPartyContact ");
        					//query = BASE_URL + "Crm/CrmPartyContact/1.0.0?exp=eq(CONTACT_TYPE_CODE%2CKCP%2CPRI_CRED_OFFICER)&attributes=ORG_ID%2CFIRST_NAME%2CLAST_NAME%2CCONTACT_TYPE_CODE&limit="+FULL_MAX_ITEMS+"&offset="+page;
        					query = BASE_URL + RELINKCRMPARTYCONTACT + FULL_MAX_ITEMS + "&offset="+page;
        					
        					//query = "https://eut.gcrs.risk.intranet.db.com/counterparty/dif/data/" + "Crm/CrmPartyContact/1.0.0?exp=eq(CONTACT_TYPE_CODE%2CKCP%2CPRI_CRED_OFFICER)&attributes=ORG_ID%2CFIRST_NAME%2CLAST_NAME%2CCONTACT_TYPE_CODE&limit="+FULL_MAX_ITEMS+"&offset="+page;
        					LOGGER.error("query's callFullParagonCrmPartyContact: "+query);
							//LOGGER.error("query2: "+query2);
							
							
        					contacts = callParagonCrmContact(query);
        					LOGGER.error("Get contacts , page: "+page+", size: "+contacts.size());
        					
        					if(contacts.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyCrmContact(contacts); //PD_STG_CRM_LOAD_LIMITS
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ CRM_CONTACTS );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ CRM_CONTACTS );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+CRM_CONTACTS+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(CRM_CONTACTS, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(CRM_CONTACTS, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        					errors=false;
        				
        		page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || contacts.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ CRM_CONTACTS );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	/**
 	 * 
 	 * 
 	 * PD_STG_CRM_PARTY_CREDIT_AREA
 	 * 
 	 * @param jTable
 	 * @return
 	 * @throws InterruptedException
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonCrmPartyCreditArea() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonCrmPartyCreditArea(jTable) "+CRM_CREDIT_AREA);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyCrmCreditAreaParagon>  creditArea  = new ArrayList<PartyCrmCreditAreaParagon>();
        
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					creditArea.clear();
        					query=null;
        						//TODO: CHILD_ORG_ID DOWNLOAD! . in december? I don't know this, to see.
        					LOGGER.error("In callFullParagonCrmPartyCreditArea");
        					
        					//TODO: The 1.0.0 version doesn't work well, so, change to 1.0.1
        					query = BASE_URL + RELINKCRMPARTYCREDITAREA + FULL_MAX_ITEMS + "&offset="+page;
        					
        					creditArea = callParagonCrmCreditArea(query);
        					LOGGER.error("Get credit Area , page: "+page+", size: "+creditArea.size());
        					
        					if(creditArea.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyCrmCreditArea(creditArea); //PD_STG_CRM_PARTY_CREDIT_AREA
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ CRM_CREDIT_AREA );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ CRM_CREDIT_AREA );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+CRM_CREDIT_AREA+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(CRM_CREDIT_AREA, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(CRM_CREDIT_AREA, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        					errors=false;
        				
        		page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || creditArea.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ CRM_CREDIT_AREA );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	
 	/**
 	 * PD_STG_CRM_PARTY_ATTRS
 	 * 
 	 * @param jTable
 	 * @return
 	 * @throws InterruptedException
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonCrmPartyAttrs() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonPartyAttrs(jTable) "+CRM_ATTRS);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyAttrParagon>  attr  = new ArrayList<PartyAttrParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		
        			do{
        				try{
        					attr.clear();
        					query=null;
        		
        					LOGGER.error("In callFullParagonPartyAttrs ");
        					//query = BASE_URL + "Crm/CrmPartyAttrs/1.0.0?exp=gt(ORG_ID%2C1)&attributes=ORG_ID%2CCP_LEGAL_TYPE%2CBUSINESS_GROUP_CODE%2CCCDB_NO%2CRESPONSIBILITY%2CBANK_NUMBER&limit="+FULL_MAX_ITEMS+"&offset="+page;
        					query = BASE_URL + RELINKCRMPARTYATTRS + FULL_MAX_ITEMS + "&offset="+page;
        					attr = callParagonPartyAttr(query);
        					LOGGER.error("Get ATTRS, page: "+page+", size: "+attr.size());
        					
        					if(attr.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyCrmAttr(attr); //PD_STG_IMPORT_CPTY_EXTRA
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ CRM_ATTRS );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ CRM_ATTRS );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+CRM_ATTRS+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(CRM_ATTRS, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(CRM_ATTRS, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        			errors=false;
        			page++;
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || attr.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ CRM_ATTRS );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	
 	/**
 	 * PD_STG_CRM_PARTY_RATING
 	 * 
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonCrmPartyRatings() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonCrmPartyRatings(jTable) "+CRM_RATINGS);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyCrmRatingParagon>  ratings  = new ArrayList<PartyCrmRatingParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					ratings.clear();
        					query=null;
        					//TODO: Change to 1.0.0 ?? 
        					query = BASE_URL + RELINKCRMPARTYRATINGS + FULL_MAX_ITEMS + "&offset="+page;
        					ratings = callParagonCrmRating(query);
        					LOGGER.error("Get CrmPartyRating, page: "+page+", size: "+ratings.size());
        					
        					if(ratings.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyCrmRatings(ratings); //PD_STG_CRM_PARTY_RATING
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ CRM_RATINGS );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ CRM_RATINGS );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+CRM_RATINGS+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(CRM_RATINGS, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(CRM_RATINGS, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        					errors=false;
        				
        		page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || ratings.size() !=0  );
        
        LOGGER.error("Finish web service FULL "+ CRM_RATINGS );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	/**
 	 * 
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonPartyIndustry() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonPartyIndustry(jTable) "+PARTY_INDUSTRY);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyIndustryParagon> 	 _partyIndustry 	  = new ArrayList<PartyIndustryParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					_partyIndustry.clear();
        					query=null;
        		
        					LOGGER.error("In PARTY_INDUSTRY ");
        					query = BASE_URL + RELINKPARTYINDUSTRY + FULL_MAX_ITEMS + "&offset="+page;
        					_partyIndustry = callParagonPartyIndustry(query);
        					LOGGER.error("Get PARTY INDUSTRY, page: "+page+", size: "+_partyIndustry.size());
        					
        					if(_partyIndustry.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyIndustry(_partyIndustry);
        						LOGGER.error("SAVED Industry!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_INDUSTRY );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_INDUSTRY );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+PARTY_INDUSTRY+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(PARTY_INDUSTRY, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(PARTY_INDUSTRY, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        			errors=false;
        			page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || _partyIndustry.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ PARTY_INDUSTRY );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL "+PARTY_INDUSTRY);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	
 	
 	
 	/**
 	 * 
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullParagonParty() throws InterruptedException{
 		LOGGER.error("Entra en callFullParagonParty(jTable) "+PARTY);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyParagon> 	 _party 	  = new ArrayList<PartyParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					_party.clear();
        					query=null;
        		
        					LOGGER.error("In  "+PARTY);
        					query = BASE_URL + RELINKPARTY + FULL_MAX_ITEMS + "&offset="+page;
        					_party = callParagonParty(query);
        					LOGGER.error("Get PARTY, page: "+page+", size: "+_party.size());
        					
        					if(_party.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storeParty(_party);
        						LOGGER.error("SAVED party!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+PARTY+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(PARTY, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(PARTY, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        			errors=false;
        			page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || _party.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ PARTY );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL "+PARTY);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	
 	
 	
	/**
 	 * IDENTIFIERS
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullPartyIdentifiers() throws InterruptedException{
 		LOGGER.error("Entra en callFullPartyIdentifiers(jTable) "+PARTY_IDENTIFIERS);
        //Long id = 1l;
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyIdentifierParagon> 	 _identifiers 	  = new ArrayList<PartyIdentifierParagon>();

        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					_identifiers.clear();
        					query=null;
        		
        					LOGGER.error("In  "+PARTY_IDENTIFIERS);
        					query = BASE_URL + RELINKPARTYALTID + FULL_MAX_ITEMS + "&offset=" +page;
        					_identifiers = callParagonPartyIdentifier(query);
        					LOGGER.error("Get PARTY Identifiers, page: "+page+", size: "+_identifiers.size());
        					
        					if(_identifiers.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyIdentifiers(_identifiers);
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_IDENTIFIERS );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_IDENTIFIERS );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+PARTY_IDENTIFIERS+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(PARTY_IDENTIFIERS, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(PARTY_IDENTIFIERS, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        			errors=false;
        			page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || _identifiers.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ PARTY_IDENTIFIERS );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL "+PARTY_IDENTIFIERS);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	
 	/**
 	 * hierarchy
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullPartyHierarchy() throws InterruptedException{
 		LOGGER.error("Entra en callFullPartyHierarchy(jTable) "+PARTY_HIERARCHY);
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyHierarchyParagon> 	 _hierarchies 	  = new ArrayList<PartyHierarchyParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		
        			do{
        				try{
        					_hierarchies.clear();
        					query=null;
        		
        					LOGGER.error("In  "+PARTY_HIERARCHY);
        					//https://eut.gcrs.risk.intranet.db.com/counterparty/dif/data/Party/PartyHierarchy/1.0.0?exp=IN(HIERARCHY_TYPE%2CLEGAL%2CCREDIT)&attributes=HIERARCHY_TYPE%2CPARENT_ORG_ID%2CULTIMATE_PARENT_ORG_ID%2CCHILD_ORG_ID&limit=3&offset=1  
        					query = BASE_URL + RELINKPARTYHIERARCHY + FULL_MAX_ITEMS + "&offset="+page;
        					_hierarchies = callParagonPartyHierarchy(query);
        					LOGGER.error("Get PARTY Hierarchy, page: "+page+", size: "+_hierarchies.size());
        					
        					if(_hierarchies.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyHierarchy(_hierarchies);
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_HIERARCHY );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_HIERARCHY );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+PARTY_HIERARCHY+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(PARTY_HIERARCHY, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(PARTY_HIERARCHY, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        			errors=false;
        			page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || _hierarchies.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ PARTY_HIERARCHY );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL "+PARTY_HIERARCHY);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	/**
 	 * V_DIF _PARTY_ADDRESS
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullPartyAddress() throws InterruptedException{
 		LOGGER.error("Entra en callFullPartyAddress(jTable) "+PARTY_ADDRESS);
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyAddressParagon> 	 _address 	  = new ArrayList<PartyAddressParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					_address.clear();
        					query=null;
        		
        					LOGGER.error("In  "+PARTY_ADDRESS);
        					//query = BASE_URL + "Party/PartyAddress/1.0.0?exp=IN(ADDRESS_TYPE_CODE%2CPERMANENT)&attributes=ORG_ID%2CCOUNTRY_CODE_ISO_3%2CAREA_CODE%2CCITY_CODE%2CSOURCE_OVERSEAS_TERRITORY%2CDOMICILE_COUNTRY_NAME%2CCOUNTRY_CODE_ISO_2%2CADDRESS_LINE_1%2CADDRESS_LINE_2&limit="+FULL_MAX_ITEMS+"&offset="+page;
        					//query = BASE_URL + "Party/PartyAddress/1.0.0?attributes=ORG_ID%2CCOUNTRY_CODE_ISO_3%2CAREA_CODE_UNMSKD%2CCITY_CODE_UNMSKD%2CSOURCE_OVERSEAS_TERRITORY%2CDOMICILE_COUNTRY_NAME_UNMSKD%2CCOUNTRY_CODE_ISO_2%2CADDRESS_LINE_1_UNMSKD%2CADDRESS_LINE_2_UNMSKD&limit="+FULL_MAX_ITEMS+"&offset="+page;
        					query = BASE_URL + RELINKPARTYADDRESS + FULL_MAX_ITEMS + "&offset="+page;
        					_address = callParagonPartyAddress(query);
        					LOGGER.error("Get PARTY Address, page: "+page+", size: "+_address.size());
        					
        					if(_address.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyAddress(_address);
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_ADDRESS );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_ADDRESS );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+PARTY_ADDRESS+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(PARTY_ADDRESS, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(PARTY_ADDRESS, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        			errors=false;
        			page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || _address.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ PARTY_ADDRESS );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL "+PARTY_ADDRESS);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	
 	
 	
 	/**
 	 * PD_STG_PARTY_EXT_ATTRIBUTES
 	 * 
 	 */
 	@Async
 	public CompletableFuture<Boolean> callFullPartyExtAttritutes() throws InterruptedException{
 		LOGGER.error("Entra en callFullPartyExtAttritutes(jTable) "+PARTY_EXT_ATTRIBUTES);
        Instant previous, current;
        previous = Instant.now();
        boolean errors=false;
        int page=0;
        List<PartyExtParagon> 	 _extAttribute 	  = new ArrayList<PartyExtParagon>();
        
        
        String query = null;
        
        long time = 0;
        long range= timeRange;
        
        do{
        		errors=false;
        		//failed=false;
        		
        			do{
        				try{
        					_extAttribute.clear();
        					query=null;
        		
        					LOGGER.error("In  "+PARTY_EXT_ATTRIBUTES);
        					// https://eut.gcrs.risk.intranet.db.com/counterparty/dif/data/Party/PartyExtAttributes/1.0.0?exp=gt(ORG_ID%2C1)&attributes=ORG_ID%2CATTRIBUTE_SHORT_NAME%2CATTRIBUTE_VALUE&limit=3&offset=1
        					query = BASE_URL + RELINKPARTYEXTATTRIBUTES +FULL_MAX_ITEMS+"&offset="+page;
        					_extAttribute = callParagonPartyExt(query);
        					LOGGER.error("Get PARTY Ext Attributes, page: "+page+", size: "+_extAttribute.size());
        					
        					if(_extAttribute.size() !=0 ){
        						LOGGER.error("saving in database............");
        						storePartyExtAttributes(_extAttribute);
        						LOGGER.error("SAVED!!");
        					}
        					break;
        				
        				}catch (Exception e) {
        	        		
        							LOGGER.error("ERROR: Some problems while storing or calling webservices "+ PARTY_EXT_ATTRIBUTES );
        							LOGGER.error("ERROR: Anyway It will reconnect with webservice in a seconds, please wait. "+ PARTY_EXT_ATTRIBUTES );
        							errors = true;
        	        		
        							try {
        								time+=range;//more time 0 , 0+4s -> 4+4=8s ..... to timeStoping
        								LOGGER.error("ERROR: Try again..."+PARTY_EXT_ATTRIBUTES+" , waiting: "+ time );
        								Thread.sleep(time); //4000 -> 4s
        								LOGGER.error("ERROR: time: "+ time );
        	        			
        							} catch (InterruptedException ed) {
        								LOGGER.error("Exception: "+ed);
        								return CompletableFuture.completedFuture(false);
        							}
        							
        							if(errors && (time >= timeStoping)){
        								
        								LOGGER.error(" There is a ERROR and time (time >= timeStoping) has passed too, save in database the error.");
        								LOGGER.error(" ERROR is:   "+e.getMessage());
        								
//        								this.exceptionSaveDatabase(PARTY_EXT_ATTRIBUTES, "There is a ERROR and time has passed too, save in database the error.");
//        								this.exceptionSaveDatabase(PARTY_EXT_ATTRIBUTES, e.getMessage());
        							
        								return CompletableFuture.completedFuture(false);
        							}
        						}
        			}while(  (errors && time < timeStoping));
        	        
        			errors=false;
        			page++;
        		
        	
            //current = Instant.now();
            //LOGGER.error(jTable + "FINAL SAVE FULL --- loop ---- " + ChronoUnit.MILLIS.between(previous,current) + " ms");
            
        }while(  (errors && time < timeStoping) || _extAttribute.size() !=0  );
        
        LOGGER.error("FINISHED ..."+ PARTY_EXT_ATTRIBUTES );

        current = Instant.now();
        LOGGER.error("Total time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        LOGGER.error("Finish web service FULL "+PARTY_EXT_ATTRIBUTES);
        if(errors){
        	//return false;
        	return CompletableFuture.completedFuture(false);
        }

        //return true;
        return CompletableFuture.completedFuture(true);
        		
 	}
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
    private List<PartyParagon> callParagonParty(String queryUrl) throws URISyntaxException {
    	
        RestTemplate restTemplate = new RestTemplate();
        //Instant previous, current;
        URI uri = new URI(queryUrl);
        ResponseEntity<List<PartyParagon>> response=null;
        try{
        	//previous = Instant.now();
        	 //LOGGER.error("after previous.");
        	 try{
        		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyParagon>>(){});
        	 }catch(Exception e){
        		 LOGGER.error("Exception response= "+e );
        		 this.exceptionGenericCallCDIF(e.toString(), PARTY );
        	 }
        	//current = Instant.now();
        	//LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        	if(response.getStatusCodeValue() != 200){
        		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
        	}
        }catch(Exception e){
        	LOGGER.error("Exception  " +e);
        }
        return response.getBody();
    }
    
private List<PartyHierarchyParagon> callParagonPartyHierarchy(String queryUrl) throws URISyntaxException {
    	
        RestTemplate restTemplate = new RestTemplate();
        Instant previous, current;
        URI uri = new URI(queryUrl);
        ResponseEntity<List<PartyHierarchyParagon>> response=null;
        try{
        	previous = Instant.now();
        	 LOGGER.error("after previous.");
        	 try{
        		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyHierarchyParagon>>(){});
        	 }catch(Exception e){
        		 LOGGER.error("Exception response= "+e );
        		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
        	 }
        	current = Instant.now();
        	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
        	if(response.getStatusCodeValue() != 200){
        		LOGGER.error("Not is 200 the response= ");
        		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
        	}
        }catch(Exception e){
        	LOGGER.error("Exception  " +e);
        }
        return response.getBody();
    }


private List<PartyAddressParagon> callParagonPartyAddress(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyAddressParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyAddressParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyAddressParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response= "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= ");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}


private List<PartyIndustryParagon> callParagonPartyIndustry(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyIndustryParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PARTY_INDUSTRY");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyIndustryParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyIndustryParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_INDUSTRY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response.");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}



private List<PartyExtParagon> callParagonPartyExt(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyExtParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyExtParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyExtParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyExtParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_EXT_ATTRIBUTES );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= PartyExtParagon");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}



private List<PartyIdentifierParagon> callParagonPartyIdentifier(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyIdentifierParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyIdentifierParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyIdentifierParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyIdentifierParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= PartyIdentifierParagon");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}




private List<PartyAttrParagon> callParagonPartyAttr(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyAttrParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyAttrParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyAttrParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyAttrParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= PartyAttrParagon");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}


private List<PartyCrmRatingParagon> callParagonCrmRating(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyCrmRatingParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyCrmRatingParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyCrmRatingParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyCrmRatingParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= PartyCrmRatingParagon");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}




private List<PartyCrmXrefParagon> callParagonCrmXref(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyCrmXrefParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyCrmXrefParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyCrmXrefParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyCrmXrefParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= PartyCrmXrefParagon");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}




private List<PartyCrmCreditAreaParagon> callParagonCrmCreditArea(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyCrmCreditAreaParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyCrmCreditAreaParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyCrmCreditAreaParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyCrmCreditAreaParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= PartyCrmCreditAreaParagon");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}




private List<PartyCrmContactParagon> callParagonCrmContact(String queryUrl) throws URISyntaxException {
	
    RestTemplate restTemplate = new RestTemplate();
    Instant previous, current;
    URI uri = new URI(queryUrl);
    ResponseEntity<List<PartyCrmContactParagon>> response=null;
    
    try{
    	previous = Instant.now();
    	 LOGGER.error("after previous. PartyCrmContactParagon");
    	 try{
    		 response = restTemplate.exchange(uri, HttpMethod.GET , this.requestEntity, new ParameterizedTypeReference<List<PartyCrmContactParagon>>(){});
    	 }catch(Exception e){
    		 LOGGER.error("Exception response PartyCrmContactParagon = "+e );
    		 this.exceptionGenericCallCDIF(e.toString(), PARTY_HIERARCHY );
    	 }
    	current = Instant.now();
    	LOGGER.error("Get Response STATUS --- " + response.getStatusCodeValue() + " - Elapsed time: " + ChronoUnit.MILLIS.between(previous,current) + " ms");
    	if(response.getStatusCodeValue() != 200){
    		LOGGER.error("Not is 200 the response= PartyCrmCreditAreaParagon");
    		//this.exceptionResponseNot200(PARTY_BASE,response.getBody().toString());
    	}
    }catch(Exception e){
    	LOGGER.error("Exception  " +e);
    }
    return response.getBody();
}






}
