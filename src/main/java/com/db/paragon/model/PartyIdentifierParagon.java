package com.db.paragon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

/**
 * Created by rribes on 10/07/2018.
 */

@Entity
@Table(name  ="PD_STG_PARTY_IDENTIFIER")
public class PartyIdentifierParagon extends ImportEntity  implements Persistable<UUID>{

	/**
	 * 
	 * 
	 * //                    V_DIF_PARTY_ALT_IDENTIFIER. ALT_ID WHERE ALT_ID_TYPE = 'BUBA'
//                    V_DIF_PARTY_ALT_IDENTIFIER. ALT_ID WHERE ALT_ID_TYPE = 'BUBAGRP'

                    String queryPartyIdentifiersParagon = URL + "Party/PartyAltId/1.0.0?exp=and(in(ALT_ID_TYPE%2CBUBAGRP%2CBUBA))&attributes=ALT_ID%2CALT_ID_TYPE";

{
    "ORG_ID": "7942414",
    "ALT_ID_TYPE": "BUBA",
    "ALT_ID": "29374899",
    "HashCode": "E7761A2AF91106446E29BA03DAABE49C9DCE7CCEDBD1000058821A115E84DCBA"
    
    
    
    exp=and(in(ALT_ID_TYPE%2CBUBAGRP%2CBUBA))&attributes=ORG_ID%2CALT_ID%2CALT_ID_TYPE
    
    
  }

	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "ORG_ID")
	@JsonProperty("ORG_ID")
	private String orgId;
	 

	@Column(name = "ALT_ID_TYPE")
    @JsonProperty("ALT_ID_TYPE")
    private String altIdType;

    @Column(name = "ALT_ID")
    @JsonProperty("ALT_ID")
    private String altId;


    public PartyIdentifierParagon() {
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
    
    public String getAltIdType() {
		return altIdType;
	}

	public void setAltIdType(String altIdType) {
		this.altIdType = altIdType;
	}

	public String getAltId() {
		return altId;
	}

	public void setAltId(String altId) {
		this.altId = altId;
	}

	@Override
    public String toString() {
        return "V_DIF_PARTY_ALT_IDENTIFIER{" +
                //"id=" + id +
                "ALT_ID=" + altId +
                " 						" + altIdType +  //ALT_ID_TYPE
                " 						" + orgId  //ORG_ID
                ;
    }
    
    @Override
   	public boolean isNew() {
   		return true;
   	}
}
