package com.db.paragon.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.db.paragon.repository.RunRepositoryCustom;

/**
 * Created by rribes on 22/10/2018.
 */

@Repository
public class RunRepositoryImpl implements RunRepositoryCustom {

	 @Value("${spring.datasource.username}")
	 private  String USER_DATABASE;
	 
	 @Value("${spring.datasource.owner}")
	 private  String USER_DATABASE_OWNER;
	 
    @PersistenceContext
    EntityManager entityManager;
    private static final Logger LOGGER = LogManager.getLogger(RunRepositoryImpl.class);

    @Override
    public String findLastSuccessfulDate() {
    	String sql = "SELECT to_char(trunc (last_execution_date,'dd')) FROM  CRDS_EXECUTION_DATA_V";
        
        String sDate = (String) entityManager.createNativeQuery(sql).getSingleResult();
        
        if(sDate != null){
        	LOGGER.error("The DATE from database:  "+ sDate);
        	return sDate;
        }
        Date de = new DateTime().toDate();
        return de.toString();
    }
    
    
    @Override
    public Long findCurrentId() throws Exception{
//    	LOGGER.error("In findCurrentId:  ");
//    	
//    	
//    	
//    	
//    	Map<String,Object> proper = entityManager.getProperties();
//    	LOGGER.error("proper.toString():  "+proper.toString());
//    	
//    	
//    	
//    	DecryptedProperties dp = this.loadPasswd();
//    	String otro = dp.getDbDataPassword();
//    	LOGGER.error(" otro: "+otro);
//    	//CRDSClientServiceImpl.loadPasswd();
//    	
//    	entityManager.setProperty("spring.datasource.password", otro);
//    	LOGGER.error(" passed entityManager.setProperty..... ");
//    	
    	
    	Long lCurrentId =null;
    	
        
    	try{
    		//LOGGER.error("In try:  ");
    		String sql = "SELECT to_number(current_id) FROM  CRDS_EXECUTION_DATA_V";
    		//LOGGER.error("Passe sql  ");
    		BigDecimal bdCurrentId = (BigDecimal) entityManager.createNativeQuery(sql).getSingleResult();
    		LOGGER.error("bdCurrentId:"+bdCurrentId);
    	
    		lCurrentId = bdCurrentId.longValue();
    	}catch(ClassCastException e){
    		LOGGER.error("Exception in findCurrentId:  "+ e);
    		throw new Exception(e);
    	}catch(Exception e){
    		LOGGER.error("Exception in findCurrentId in exception:  "+ e);
    		throw new Exception(e);
    	}
        
        
        if(lCurrentId != null){
        	//is ok
        	LOGGER.error("The current_id from database:  "+ lCurrentId);
        	return lCurrentId;
        }
        
        
        return lCurrentId;
    }
    
    /**
     * update_job_log
     * 
     * Example for database.
     * 
     * declare
     *		v_ret_value   job_log.jlg_id%TYPE;
	 *	begin
 	 *		v_ret_value := Job_Control_Pkg.create_job_log ('CRDS LOAD-DELTA', 'S', 'Create for starting the process.(DELTA)'); 
	 *
	 *	end; 
     * 
     * @param id
     * @param status
     * @param msg
     * @return
     */
    @Override
    public void procedureUpdate_job_log(Long id, String status, String msg) {
    	    	
    	LOGGER.error("In procedureUpdate_job_log: id: "+id+" status: "+status+"msg: "+msg);
    	 
    	 
       StoredProcedureQuery query = entityManager.createStoredProcedureQuery("JOB_CONTROL_PKG.update_job_log");
      
       query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
       query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
       query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
    	 
       query.setParameter(1, id);
       query.setParameter(2, status);
       query.setParameter(3, msg);
       
       try{
    	   boolean valor = query.execute();
    	   //LOGGER.error("Updated:  "+ valor);
    	   
       }catch(Exception e){
    	   
    	   LOGGER.error("Exception in procedureUpdate_job_log:  "+ e);
       }
    	
    }
    
    /**
     * 
     * example on database:   execute Write_Import_Stat ('CRDS LOAD-DELTA', '17-JUL-18', 'Java process','whats going on? ', 226801);
     *  
     */
    @Override
    public void procedureWrite_Import_Stat(String submissionRegion, String submissionPeriod, String feedType, String logMessage, Long id){
    	    	
    	LOGGER.error("In procedureWrite_Import_Stat: submissionRegion: "+submissionRegion+", submissionPeriod: "+submissionPeriod+" , feedType: "+feedType+", logMessage: "+logMessage+ ",id: "+id);
    	 
    	 
       StoredProcedureQuery query = entityManager.createStoredProcedureQuery("Write_Import_Stat");
      
       query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
       query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
       query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
       query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
       query.registerStoredProcedureParameter(5, Long.class,   ParameterMode.IN);
    	 
       query.setParameter(1, submissionRegion);
       query.setParameter(2, submissionPeriod);
       query.setParameter(3, feedType);
       query.setParameter(4, logMessage);
       query.setParameter(5, id);
       
       try{
    	   //LOGGER.error("In try procedureWrite_Import_Stat: ");
    	   boolean valor = query.execute();
    	   
       }catch(Exception e){
    	   LOGGER.error("Exception in procedureWrite_Import_Stat:  "+ e);
       }
    	
    }
    
    
    public boolean procedureTruncateTables(){
    	
    	
    	

    	
    	
    	LOGGER.error("In procedureTruncateTables");
    	// DM_OWNER.TRUNCATE_TABLE('JAVA_USER','IMPORT_CRDS_PARTY_BASE ','DM_OWNER');
    	ArrayList<String> tables = new ArrayList<String>(
    	//Arrays.asList("PD_STG_IMPORT_PUBLIC_MAPPINGS","PD_STG_IMPORT_CPTY_CAT","PD_STG_IMPORT_CPTY_CAT","PD_STG_IMPORT_CPTY_EXTRA","PD_STG_IMPORT_PURE_CRMS_COM_ATT","PD_STG_IMPORT_CRMS_RATINGS","PD_STG_IMPORT_PUBLIC_COMPANY","PD_STG_IMPORT_PURE_CRMS_CUST","PD_STG_NACE_INDUSTRY_CODES","PD_STG_PARAGON_LEGAL_HIER","PD_STG_CRMS_LOAD_LIMITS"));
    			Arrays.asList("PD_STG_PARTY",
    			    	"PD_STG_PARTY_HIERARCHY",
    			    	"PD_STG_PARTY_ADDRESS",
    			    	"PD_STG_NACE_INDUSTRY_CODES",
    			    	"PD_STG_CRM_PARTY_ATTRS",
    			    	"PD_STG_PARTY_IDENTIFIER",
    			    	"PD_STG_PARTY_EXT_ATTRIBUTES",
    			    	//"PD_STG_CRM_PARTY_XREF",
    			    	"PD_STG_CRM_PARTY_CREDIT_AREA",
    			    	"PD_STG_CRM_PARTY_CONTACT",
    			    	"PD_STG_CRM_PARTY_RATING"));
    	
    	for(int table=0; table<tables.size() ;table++){
    		
    		StoredProcedureQuery query = entityManager.createStoredProcedureQuery(USER_DATABASE_OWNER+".TRUNCATE_TABLE");
    		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
    		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
    		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
       	 
    		//TODO: Change
    		query.setParameter(1, USER_DATABASE);
    		//query.setParameter(1, USER_DATABASE_OWNER);
    		query.setParameter(2, tables.get(table));
    		query.setParameter(3, USER_DATABASE_OWNER);
    		
        
    		try{
    			LOGGER.error("Table to truncate: "+tables.get(table));
    			boolean value = query.execute();
    			//LOGGER.error("and the value was:"+value);
    			
    		}catch(Exception e){
    			LOGGER.error("Exception in procedureTruncateTables, on table "+tables.get(table)+" and the exception is :"+e);
    			return false;
    		}
    		
    	}
    	
    	return true;

    }
    
    
    @Override
    public boolean procedureCrdsLoadDelta(){
    	    	
    	LOGGER.error("In procedureCrdsLoadDelta ");
    	 
    	 
       StoredProcedureQuery query = entityManager.createStoredProcedureQuery(USER_DATABASE_OWNER+".CRDS_LOAD_DELTA");
      
       try{
    	   boolean valor = query.execute();
    	   
       }catch(Exception e){
    	   LOGGER.error("Exception in procedureCrdsLoadDelta:  "+ e);
    	   return false;
       }

       return true;
    }
    
    /**
     * dm_pkg.do_crds_mapping_delta_load;
     */
    @Override
    public boolean procedureCrdsLoadDeltaMapping(){
    	    	
    	LOGGER.error("In procedureCrdsLoadDeltaMapping ");
    	 
    	StoredProcedureQuery query = entityManager.createStoredProcedureQuery(USER_DATABASE_OWNER+".dm_pkg.do_crds_mapping_delta_load");
      
       try{
    	   //LOGGER.error("In try procedureCrdsLoadDelta: ");
    	   //boolean valor = query.execute();
    	   boolean valor = query.execute();
    	   
       }catch(Exception e){
    	   LOGGER.error("Exception in procedureCrdsLoadDeltaMapping:  "+ e);
    	   return false;
       }

       return true;
    }
    
    
}
