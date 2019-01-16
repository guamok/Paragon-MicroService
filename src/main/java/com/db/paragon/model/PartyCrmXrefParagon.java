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
@Table(name  ="PD_STG_CRM_PARTY_XREF")
/**
 *  CREATE TABLE "DM_OWNER"."PD_STG_IMPORT_PUBLIC_MAPPINGS" 
   (	"ID"  RAW(20), 
   "SOURCESYSTEMNAME" VARCHAR2(255 BYTE), 
	"SOURCESYSTEMLOCATION" VARCHAR2(255 BYTE), 
	"SOURCEID" VARCHAR2(255 BYTE), 
	"SOURCESHORT" VARCHAR2(255 BYTE), 
	"SOURCELEGAL" VARCHAR2(255 BYTE), 
	"SOURCECOUNTRY" VARCHAR2(255 BYTE), 
	"ORGID" VARCHAR2(255 BYTE), 
	"ORGSHORT" VARCHAR2(255 BYTE), 
	"SOURCESYSTEMID" VARCHAR2(255 BYTE),
	"DATE_CREATION" DATE DEFAULT SYSDATE NOT NULL ENABLE
 * 
 *
 */
public class PartyCrmXrefParagon extends ImportEntity  implements Persistable<UUID>{

	/**
	 * 
	 * 
	 * 
		 {
    "ORG_ID": "7851896",
    "SOURCE_SYSTEM_NAME": "MARTINI",
    "SOURCE_LOCATION_NAME": "LONDON",
    "SOURCE_PARTY_ID": "041867445-133700626",
    "PARTY_SHORT_NAME": "5670206",
    "PARTY_LEGAL_NAME": "041867445-133700626",
    "SOURCE_SYSTEM_CODE": 67,
    "SOURCE_CREATED_DATE": "2012-05-17 00:00:00",
    "PARTY_SOURCE_SYSTEM_COUNTRY": "",
    "ISIN": "",
    "CUSIP": "",
    "SEDOL": "",
    "SEC_ID": "",
    "FSA_VALUE": "",
    "UNMAP_DATE": "",
    "UPPERSHORT_CODE": "5670206",
    "XREF_BUSINESS_TYPE": "",
    "SRC_DEPENDENT2": "",
    "SRC_DEPENDENT1": "",
    "USER_REQUIRED": "M",
    "XREF_BUBA_GROUP_NO": "",
    "XREF_BUBA_NO": "",
    "SYSTEM_CODE": 72,
    "HashCode": "AB85E27CBD080E7624D1146BC5431905FFD17BF8BB15D53F5656A6F1E0AC7D52"
  },


CREATE TABLE "DM_OWNER"."PD_STG_IMPORT_PUBLIC_MAPPINGS" 
   (	"ID"  RAW(20), 
   "SOURCESYSTEMNAME" VARCHAR2(255 BYTE), 
	"SOURCESYSTEMLOCATION" VARCHAR2(255 BYTE), 
	"SOURCEID" VARCHAR2(255 BYTE), 
	"SOURCESHORT" VARCHAR2(255 BYTE), 
	"SOURCELEGAL" VARCHAR2(255 BYTE), 
	"SOURCECOUNTRY" VARCHAR2(255 BYTE), 
	"ORGID" VARCHAR2(255 BYTE), 
	"ORGSHORT" VARCHAR2(255 BYTE), 
	"SOURCESYSTEMID" VARCHAR2(255 BYTE),
	"DATE_CREATION" DATE DEFAULT SYSDATE NOT NULL ENABLE
   ) SEGMENT CREATION IMMEDIATE 
 COMPRESS BASIC NOLOGGING
  PCTUSED    0
  PCTFREE    0
  TABLESPACE "DATA04_SML" ;


     FOR US

	 {
    "SOURCE_SYSTEM_NAME": "MARTINI",
    "SOURCE_LOCATION_NAME": "LONDON",
    "SOURCE_PARTY_ID": "041867445-133700626",
    "PARTY_SHORT_NAME": "5670206",
    "PARTY_LEGAL_NAME": "041867445-133700626",
    "SOURCE_SYSTEM_CODE": 67
  },



PARTY_LEGAL_NAME,PARTY_SHORT_NAME,SOURCE_LOCATION_NAME,SOURCE_PARTY_ID,SOURCE_PARTY_ID,SOURCE_SYSTEM_CODE,SOURCE_SYSTEM_NAME

"&attributes=ORG_ID%2CSOURCE_SYSTEM_NAME%2CPARTY_LEGAL_NAME%2CPARTY_SHORT_NAME%2CSOURCE_LOCATION_NAME%2CSOURCE_PARTY_ID%2CSOURCE_SYSTEM_CODE";
	 */
	
	// attributes=SOURCE_SYSTEM_CODE";
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	 @Column(name = "ORG_ID")
	 @JsonProperty("ORG_ID")
	 private String orgId;
	 
	 @Column(name = "SOURCE_SYSTEM_NAME")
	 @JsonProperty("SOURCE_SYSTEM_NAME")
	 private String sourceSystemName;
	 
	 @Column(name = "PARTY_LEGAL_NAME")
	 @JsonProperty("PARTY_LEGAL_NAME")
	 private String partyLegalName;
	 
	 
	 @Column(name = "SOURCE_PARTY_ID")
	 @JsonProperty("SOURCE_PARTY_ID")
	 private String sourcePartyId;
	 
	 
	 @Column(name = "PARTY_SHORT_NAME")
	 @JsonProperty("PARTY_SHORT_NAME")
	 private String partyShortName;


	 @Column(name = "SOURCE_LOCATION_NAME")
	 @JsonProperty("SOURCE_LOCATION_NAME")
	 private String sourceLocationName;
	 
	 

	 @Column(name = "SOURCE_SYSTEM_CODE")
	 @JsonProperty("SOURCE_SYSTEM_CODE")
	 private String sourceSystemCode;
	 
	 @Column(name = "PARTY_SOURCE_SYSTEM_COUNTRY")
	 @JsonProperty("PARTY_SOURCE_SYSTEM_COUNTRY")
	 private String partySourceSystemCountry;
	 
	 





	@Override
    public UUID getId() {
        return id;
    }

	public String getPartySourceSystemCountry() {
		return partySourceSystemCountry;
	}
	
	
	public void setPartySourceSystemCountry(String partySourceSystemCountry) {
		this.partySourceSystemCountry = partySourceSystemCountry;
	}
    
	public void setId(UUID id) {
        this.id = id;
    }
	
	
	  public String getOrgId() {
			return orgId;
		}

	  public void setSourceSystemCode(String sourceSystemCode) {
		  this.sourceSystemCode = sourceSystemCode;
	  }

		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}
	
	 public String getSourceSystemName() {
			return sourceSystemName;
		}


		public void setSourceSystemName(String sourceSystemName) {
			this.sourceSystemName = sourceSystemName;
		}


		public String getSourceLocationName() {
			return sourceLocationName;
		}


		public void setSourceLocationName(String sourceLocationName) {
			this.sourceLocationName = sourceLocationName;
		}


		public String getSourcePartyId() {
			return sourcePartyId;
		}


		public void setSourcePartyId(String sourcePartyId) {
			this.sourcePartyId = sourcePartyId;
		}


		public String getPartyShortName() {
			return partyShortName;
		}


		public void setPartyShortName(String partyShortName) {
			this.partyShortName = partyShortName;
		}


		public String getPartyLegalName() {
			return partyLegalName;
		}


		public void setPartyLegalName(String partyLegalName) {
			this.partyLegalName = partyLegalName;
		}


		public String getSourceSystemCode() {
			return sourceSystemCode;
		}



    @Override
    public String toString() {
        return "V_DIF_CRM_PARTY_XREF{" +
        		"ORG_ID=		 	" + orgId +//SOURCE_LOCATION_NAME=
                "SOURCE_SYSTEM_NAME=				" + sourceSystemName + //SOURCE_SYSTEM_NAME
                "				 	" + sourceLocationName +//SOURCE_LOCATION_NAME=
                "				 	" + sourcePartyId  +//SOURCE_PARTY_ID=
                "				 	" + partyShortName  +//PARTY_SHORT_NAME=
                "				 	" + partyLegalName  +//PARTY_LEGAL_NAME=
                "				 	" + partySourceSystemCountry  +//PARTY_SOURCE_SYSTEM_COUNTRY=
                " 					" + sourceSystemCode//SOURCE_SYSTEM_CODE= 
                ;
    }
    
   


	@Override
   	public boolean isNew() {
   		return true;
   	}
}
