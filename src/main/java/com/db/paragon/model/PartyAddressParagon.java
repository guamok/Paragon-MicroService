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
 * Created by rribes on 05/11/2018.
 */

@Entity
@Table(name = "PD_STG_PARTY_ADDRESS")
public class PartyAddressParagon extends ImportEntity implements Persistable<UUID>{

	private static final long serialVersionUID = 1L;
	//private static final Logger LOGGER = LogManager.getLogger(Address.class);
	
	
	
//	V_DIF _PARTY_ADDRESS.COUNTRY_ISO_CODE_3
//	V_DIF_PARTY_ADDRESS.AREA_CODE
//	V_DIF_PARTY_ADDRESS.CITY_CODE
//	V_DIF_PARTY_ADDRESS.DOMICILE_COUNTRY_NAME_UNMSKD
//	V_DIF_PARTY_ADDRESS.INCORP_COUNTRY_DESC
//	V_DIF_PARTY_ADRESS.ADDRESS_LINE_1
//	V_DIF_PARTY_ADRESS.ADDRESS_LINE_2
//	
//	
//	 {
//	    "ORG_ID": "6545750",
//	    "ADDRESS_TYPE_CODE": "PERMANENT",
//	    "ADDRESS_LINE_1": "55 BAKER STREET",
//	    "ADDRESS_LINE_2": "",
//	    "ADDRESS_LINE_3": "",
//	    "CITY_CODE": "LONDON",
//	    "AREA_CODE": "W1U 8EW",
//	    "REGION": "",
//	    "COUNTRY_CODE_ISO_2": "GB",
//	    "COUNTRY_CODE_ISO_3": "GBR",
//	    "DOMICILE_COUNTRY_CODE": "GB",
//	    "DOMICILE_COUNTRY_NAME": "GREAT BRITAIN",
//	    "NATIONALITY_COUNTRY_ID": "GB",
//	    "ISO_CURRENCY_CODE": "GBP",
//	    "SOURCE_OVERSEAS_TERRITORY": "106",
//	    "ISO_CURRENCY_DESC": "Great British Pound",
//	    "COUNTERPARTY_RISK_MARKET_TYPE": "Developed",
//	    "HashCode": "2317CD6B0159F98CAB31C6A3CE888CA399478DBB71185650564ECC00D19617A1"
//	  }


	
//	{
//	    "ORG_ID": "6545750",
//	    "ADDRESS_LINE_1": "55 BAKER STREET",
//	    "ADDRESS_LINE_2": "",
//	    "CITY_CODE": "LONDON",
//	    "AREA_CODE": "W1U 8EW",
//	    "COUNTRY_CODE_ISO_3": "GBR",
//	    "DOMICILE_COUNTRY_CODE": "GB",
//	    "DOMICILE_COUNTRY_NAME": "GREAT BRITAIN"
//	  }

	
	
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

	
	 @Column(name = "ORG_ID")
	 @JsonProperty("ORG_ID")
	 private String orgId;
	 
	 
	 @Column(name = "COUNTRY_CODE_ISO_3")
	 @JsonProperty("COUNTRY_CODE_ISO_3")
	 private String countryCodeIso3;
	
	 @Column(name = "AREA_CODE_UNMSKD")
	 @JsonProperty("AREA_CODE_UNMSKD")
	 private String areaCodeUnmskd;
	 
	 @Column(name = "CITY_CODE_UNMSKD")
	 @JsonProperty("CITY_CODE_UNMSKD")
	 private String cityCodeUnmskd;
	 
	 @Column(name = "SOURCE_OVERSEAS_TERRITORY")
	 @JsonProperty("SOURCE_OVERSEAS_TERRITORY")
	 private String sourceOverseasTerritory;
	 
	 @Column(name = "DOMICILE_COUNTRY_NAME_UNMSKD")
	 @JsonProperty("DOMICILE_COUNTRY_NAME_UNMSKD")
	 private String domicileCountryNameUnmskd;
	 
	 @Column(name = "COUNTRY_CODE_ISO_2")
	 @JsonProperty("COUNTRY_CODE_ISO_2")
	 private String countryCodeIso2;
	 

	@Column(name = "ADDRESS_LINE_1_UNMSKD")
	 @JsonProperty("ADDRESS_LINE_1_UNMSKD")
	 private String addressLine1Unmskd;
	 
	 
	 @Column(name = "ADDRESS_LINE_2_UNMSKD")
	 @JsonProperty("ADDRESS_LINE_2_UNMSKD")
	 private String addressLine2Unmskd;
	 
	 
	
	 
	
	public PartyAddressParagon() {
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

	public String getCountryCodeIso3() {
		return countryCodeIso3;
	}

	public void setCountryCodeIso3(String countryCodeIso3) {
		this.countryCodeIso3 = countryCodeIso3;
	}

	public String getAreaCodeUnmskd() {
		return areaCodeUnmskd;
	}

	public void setAreaCodeUnmskd(String areaCodeUnmskd) {
		this.areaCodeUnmskd = areaCodeUnmskd;
	}

	public String getCityCodeUnmskd() {
		return cityCodeUnmskd;
	}

	public void setCityCodeUnmskd(String cityCodeUnmskd) {
		this.cityCodeUnmskd = cityCodeUnmskd;
	}

	public String getSourceOverseasTerritory() {
		return sourceOverseasTerritory;
	}

	public void setSourceOverseasTerritory(String sourceOverseasTerritory) {
		this.sourceOverseasTerritory = sourceOverseasTerritory;
	}

	public String getDomicileCountryNameUnmskd() {
		return domicileCountryNameUnmskd;
	}

	public void setDomicileCountryNameUnmskd(String domicileCountryNameUnmskd) {
		this.domicileCountryNameUnmskd = domicileCountryNameUnmskd;
	}

	public String getCountryCodeIso2() {
		return countryCodeIso2;
	}

	public void setCountryCodeIso2(String countryCodeIso2) {
		this.countryCodeIso2 = countryCodeIso2;
	}

	public String getAddressLine1Unmskd() {
		return addressLine1Unmskd;
	}

	public void setAddressLine1Unmskd(String addressLine1Unmskd) {
		this.addressLine1Unmskd = addressLine1Unmskd;
	}

	public String getAddressLine2Unmskd() {
		return addressLine2Unmskd;
	}

	public void setAddressLine2Unmskd(String addressLine2Unmskd) {
		this.addressLine2Unmskd = addressLine2Unmskd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    
    
	
//    @Override
//    public String toString() {
//        return "V_DIF_PARTY_ADRESS{" +
//                //"id=" + id +
//                "ORG_ID=" + orgId +
//                "					" + countryIsoCode3 +//COUNTRY_CODE_ISO_3
//                "				 	" + areaCode + //AREA_CODE=
//                "				 	"+ cityCode +//CITY_CODE= 
//                "				 	" + domicileCountryName  +//DOMICILE_COUNTRY_NAME=
//                "					" + domicileCountryCode  +// DOMICILE_COUNTRY_CODE=
//                " 				 	" + addressLine1  +//ADDRESS_LINE_1=
//                "				 	" + addressLine2 //ADDRESS_LINE_2=
//               ;
//    }
    
   
    @Override
	public boolean isNew() {
		return true;
	}
}
