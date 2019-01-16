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
 * Created by rribes on 07/09/2018.
 */

@Entity
@Table(name = "PD_STG_NACE_INDUSTRY_CODES")
public class PartyIndustryParagon extends ImportEntity implements Persistable<UUID>{

	/**
	 * //                     V_DIF_PARTY_INDUSTRY.CODE WHERE SCHEME='IIBC'
//                    		 V_DIF_PARTY_INDUSTRY.CODE WHERE SCHEME='NACE'
//                    		 V_DIF_PARTY_INDUSTRY.CODE WHERE SCHEME='SIC'
//                    		 V_DIF_PARTY_INDUSTRY.DESCRIPTION
//                    		 V_DIF_PARTY_INDUSTRY.DESCRIPTION
 * 
 * 
 * {
    "SCHEME": "NACE",
    "CODE": "3002001",
    "DESCRIPTION": "Manufacture of Computers"
  },
 * 

Party/PartyIndustry/1.0.0?exp=gt(ORG_ID%2C1)&exp=and(in(SCHEME%2CNACE%2CSIC%2CIIBC))&attributes=ORG_ID%2CCODE%2CSCHEME%2CDESCRIPTION";


attributes=ORG_ID%2CCODE%2CSCHEME%2CDESCRIPTION


	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "ORG_ID")
    @JsonProperty("ORG_ID")
    private String orgId;
    
	
    @Column(name = "CODE")
    @JsonProperty("CODE")
    private String code;

    @Column(name = "SCHEME")
    @JsonProperty("SCHEME")
    private String scheme;

    @Column(name = "DESCRIPTION")
    @JsonProperty("DESCRIPTION")
    private String description;


    public PartyIndustryParagon() {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public String toString() {
        return "V_DIF_PARTY_INDUSTRY{" +
                //"id=" + id +
                "ORG_ID=" + orgId +
                "			 " + code + //CODE=
                " 				" + scheme  +//SCHEME=
                " 					" + description//DESCRIPTION= 
               ;
    }
    
    @Override
   	public boolean isNew() {
   		return true;
   	}
}
