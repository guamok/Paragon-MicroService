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
 * Created by rribes on 30/08/2018.
 */

@Entity
@Table(name = "PD_STG_PARTY")
public class PartyParagon extends ImportEntity implements Persistable<UUID>{

	
	private static final long serialVersionUID = 1219576676085504256L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
	

    @Column(name = "ORG_ID")
    @JsonProperty("ORG_ID")
    private String orgId;

    @Column(name = "LEGAL_NAME_UNMSKD")
    @JsonProperty("LEGAL_NAME_UNMSKD")
    private String legalNameUnmskd;

    @Column(name = "SHORT_NAME")
    @JsonProperty("SHORT_NAME")
    private String shortName;

    @Column(name = "DBCAR_ID")
    @JsonProperty("DBCAR_ID")
    private String dbcarId;


  

    public PartyParagon() {
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getDbcarId() {
    	return dbcarId;
    }
    
    public void setDbcarId(String dbcarId) {
    	this.dbcarId = dbcarId;
    }

    public String getPartyId() {
        return orgId;
    }

    public void setPartyId(String orgId) {
        this.orgId = orgId;
    }

   

    public String getLegalNameUnmskd() {
		return legalNameUnmskd;
	}

	public void setLegalNameUnmskd(String legalNameUnmskd) {
		this.legalNameUnmskd = legalNameUnmskd;
	}

	public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

   

   
    
    @Override
    public String toString() {
        return "V_DIF_PARTY{" +
                //"id=" + id +
                "ORG_ID=" + orgId +
                " 				" + legalNameUnmskd +//LEGAL_NAME=
                " 				 						" +shortName  +//SHORT_NAME=
                " 				 			" + dbcarId  
                ;
    }

	@Override
	public boolean isNew() {
		return true;
	}
}
