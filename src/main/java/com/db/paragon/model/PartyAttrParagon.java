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
 * Created by rribes on 10/09/2018.
 */

@Entity
@Table(name  ="PD_STG_CRM_PARTY_ATTRS")
public class PartyAttrParagon extends ImportEntity  implements Persistable<UUID>{

	/**
	 * 
	 * 
	 * V_DIF_CRM_PARTY_ATTRS.BANK_NUMBER
V_DIF_CRM_PARTY_ATTRS.BUSINESS_GROUP_CODE
"V_DIF_CRM_PARTY_ATTRS.BUSINESS_GROUP_CODE, not the ID
Para view provides a code, not the internal id - from syscode: d_ParBusinessArea"
V_DIF_CRM_PARTY_ATTRS.CCDB_NO
V_DIF_CRM_PARTY_ATTRS.RESPONSIBILITY



{
    "ORG_ID": "7440587",
    "CCDB_NO": 66150362,
    "BANK_NUMBER": "",
    "CP_CLASS": 28,
    "RESPONSIBILITY": "CIB",
    "IIBC_CODE": "410",
    "IIBC_DESC": "Retail",
    "BUSINESS_GROUP_CODE": "Corp Midcap EU2",
    "BUSINESS_GROUP_DESCRIPTION": "Corporate Midcap Europe 2",
    "P1_GROUP": "",
    "ACCOUNT_NUMBER": "",
    "WATCH_LIST_STATUS": "",
    "WORK_OUT_STATUS": "",
    "RELATIONSHIP_STATUS": "",
    "BUSINESS_AREA": "",
    "IS_CREDIT_GROUP": 1,
    "CP_LEGAL_TYPE": "GROUP",
    "CP_TYPE": "LGENT",
    "IS_INTERNAL": "0",
    "NON_CREDIT_RELEVANT": "1",
    "REVIEW_DATE": "2099-12-31 00:00:00",
    "SUBSIDIARY_LEVEL": "Not Required",
    "COMPANY_REGISTER_ID": -1,
    "REGISTER_NUMBER": "",
    "REGISTER_CITY": "",
    "WATCHLIST_COUNTERPARTY_LEVEL": "N",
    "WATCHLIST_GROUP_LEVEL": "Y",
    "LEI_NUMBER": "",
    "REGISTRATION_DATE": "",
    "REGISTER_TYPE": "Unknown",
    "PARTY_REGISTER_COUNTRY_ID": "??",
    "BBK_NACE_CODE": "",
    "ARCS_OTC_FLAG": "",
    "DELEGATED_MNC_SUB": 2,
    "KWG13": 0,
    "KWG14": "",
    "KWG15": 0,
    "KWG18": 0,
    "KWG18_DATE": "",
    "OUTSOURCED": 2,
    "UNQUALIFIED": "",
    "LAST_FINANCIALS_DATE": "",
    "INCORPORATION_DATE": "",
    "BASE_AUTH_LEVEL": "",
    "CP_FROZEN_STATUS": "0",
    "BALANCE_SHEET_DATE": "",
    "CA_DESC": "MIDCAP CORP",
    "AUDITED": "",
    "TAX_NUMBER": "",
    "SP_ISSUER_ID": "",
    "RULE_431_DESIGNATED": 2,
    "RULE_431_EXEMPT": 2,
    "BUSINESS_AREA_SHORT": "",
    "RESPONSIBLE_BRANCH_NAME": "",
    "COLLATERALISED": "2",
    "LEGAL_CONFIDENTIALITY": "",
    "RESPONSIBILITY_STATUS_SHORT": "",
    "TRADING_STATUS": "STOP",
    "WORK_OUT_STATUS_SHORT": "",
    "LEGAL_CP_ID": "",
    "PARTY_COMMENT": "",
    "RESPONSIBILITY_SHORT": "CIB",
    "RESPONSIBLE_BRANCH_CODE": "",
    "PLM_LEGAL_NAME": "Unknown",
    "WATCH_LIST_STATUS_SHORT": "",
    "SOV_LOCAL_RATING": "",
    "SOV_FOREIGN_RATING": "",
    "HashCode": "256F6237822F8E5AEC5917472979B1E7B12EB95EBE2D744692E9B8729C64AAB5"
  },


BANK_NUMBER%2CBUSINESS_GROUP_CODE%2CCCDB_NO%2CRESPONSIBILITY


CONSTRAINT "ICE_PK" PRIMARY KEY ("RECORD_NO", "SUBMISSION_REGION", "SUBMISSION_PERIOD") in IMPORT_CPTY_EXTRA table

	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "ORG_ID")
	@JsonProperty("ORG_ID")
    private String orgId;
	
	@Column(name = "BANK_NUMBER")
    @JsonProperty("BANK_NUMBER")
    private String bankNumber;


	@Column(name = "BUSINESS_GROUP_CODE")
    @JsonProperty("BUSINESS_GROUP_CODE")
    private String businessGroupCode;

    @Column(name = "CCDB_NO")
    @JsonProperty("CCDB_NO")
    private String ccdbNo;
    
    @Column(name = "RESPONSIBILITY")
    @JsonProperty("RESPONSIBILITY")
    private String responsibility;
    
    
    @Column(name = "CP_LEGAL_TYPE")
    @JsonProperty("CP_LEGAL_TYPE")
    private String cpLegalType;
    
    @Column(name = "CP_TYPE")
    @JsonProperty("CP_TYPE")
    private String cpType;
    

    

	public String getCpLegalType() {
		return cpLegalType;
	}

	public void setCpLegalType(String cpLegalType) {
		this.cpLegalType = cpLegalType;
	}

	public PartyAttrParagon() {
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getBankNumber() {
		return bankNumber;
	}
	
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	

	public String getBusinessGroupCode() {
		return businessGroupCode;
	}

	public void setBusinessGroupCode(String businessGroupCode) {
		this.businessGroupCode = businessGroupCode;
	}

	public String getCcdbNo() {
		return ccdbNo;
	}

	public void setCcdbNo(String ccdbNo) {
		this.ccdbNo = ccdbNo;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public void setId(UUID id) {
        this.id = id;
    }

	public String getCpType() {
		return cpType;
	}

	public void setCpType(String cpType) {
		this.cpType = cpType;
	}
	
	
	
    @Override
    public String toString() {
        return "V_DIF_CRM_PARTY_ATTRS{" +
        		"ORG_ID=" + orgId +
//                "						" + bankNumber +//BANK_NUMBER
                "						" + businessGroupCode +//BUSINESS_GROUP_CODE=
                "						" + ccdbNo  +//CCDB_NO=
                "						" + responsibility//RESPONSIBILITY= 
                ;
    }
    
    @Override
   	public boolean isNew() {
   		return true;
   	}
}
