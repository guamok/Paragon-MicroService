package com.db.paragon.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rribes on 08/11/2018.
 */

@Entity
@Table(name  = "PD_STG_PARTY_EXT_ATTRIBUTES")
public class PartyExtParagon extends ImportEntity implements Persistable<UUID>{

	/**
	 * 
	 */
	
	
//	{
//	    "ATTRIBUTE_NAME": "Batch Code Review",
//	    "PARTY_ID": 3852,
//	    "ORG_ID": "3852",
//	    "ATTRIBUTE_VALUE": "Confirmed! Int Ind Batch Code agreed by GCM & GCIM or PM & PR",
//	    "ATTRIBUTE_OWNER": "Simon Donouzian",
//	    "ATTRIBUTE_DESCRIPTION": "Confirms the IIBC was from PM rather than automatically generated",
//	    "ATTRIBUTE_SHORT_NAME": "GENERIC ATTR 08",
//	    "HashCode": "F9FA6DEBDB718AA4DF22F60C56ECB4F0CE764014DCE1CBD374DEBB39A65878AE"
//	  },

	
	
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
	
	
	
	 @Column(name = "ORG_ID")
	 @JsonProperty("ORG_ID")
	 private String orgId;
	 
	 
	 @Column(name = "ATTRIBUTE_SHORT_NAME")
	 @JsonProperty("ATTRIBUTE_SHORT_NAME")
	 private String attributeShortName;
	 
	 

	 @Column(name = "ATTRIBUTE_VALUE")
	 @JsonProperty("ATTRIBUTE_VALUE")
	 private String attributeValue;
	

	public PartyExtParagon() {
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	 public String getAttributeShortName() {
			return attributeShortName;
		}

	public void setAttributeShortName(String attributeShortName) {
		this.attributeShortName = attributeShortName;
	}
	
	public String getAttributeValue() {
			return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
		
		
    @Override
    public String toString() {
        return "V_DIF_PARTY_EXTENDED_ATTRIBUTES{" +
                //"id=" + id +
                "ORG_ID=" + orgId +
                "					" + attributeShortName + // ATTRIBUTE_SHORT_NAME
                "					" + attributeValue// ATTRIBUTE_VALUE
                
                ;
    }
    
   
    @Override
	public boolean isNew() {
		return true;
	}
}
