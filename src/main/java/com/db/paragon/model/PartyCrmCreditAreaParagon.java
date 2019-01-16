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
 * Created by rribes on 18/09/2018.
 */

@Entity
@Table(name = "PD_STG_CRM_PARTY_CREDIT_AREA")
public class PartyCrmCreditAreaParagon extends ImportEntity implements Persistable<UUID>{

	 /**
	 * {
	 * attributes=ORG_ID%2CCREDIT_REGION_ID%2CCREDIT_TEAM_DESCRIPTION
	 * 
	 * 
  }


 "ORG_ID": "1053",
    "CREDIT_DIVISION_ID": 29,
    "CREDIT_DIVISION_CODE": "EMERGING MKT",
    "CREDIT_DIVISION_DESCRIPTION": "EMERGING MARKETS",
    "CREDIT_REGION_ID": 62,
    "CREDIT_REGION_CODE": "EM LA",
    "CREDIT_REGION_DESCRIPTION": "EM LATIN AMERICA",
    "CREDIT_TEAM_ID": 47,
    "CREDIT_TEAM_CODE": "CR_TEAM_52",
    "CREDIT_TEAM_DESCRIPTION": "NCR - UNKNOWN",
    "CREDIT_TEAM_LOCATION": "London",
    "CREDIT_AREA_ID": 14,
    "CREDIT_AREA_CODE": "TEAM11",
    "CREDIT_AREA_DESCRIPTION": "NCR - Latin America",
    "CREDIT_COUNTRY_ID": "BB",
    "HashCode": "8226F39BAE624D2FB7E7AD59FCADE01F0F61135DB203708EAE3616BE3E4D2323"
  },

	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(name = "ORG_ID")
	@JsonProperty("ORG_ID")
	private String orgId;
	

    @Column(name = "CREDIT_REGION_ID")
    @JsonProperty("CREDIT_REGION_ID")
    private long creditRegionId;

    @Column(name = "CREDIT_TEAM_DESCRIPTION")
    @JsonProperty("CREDIT_TEAM_DESCRIPTION")
    private String creditTeamDescription;
    
   

	public PartyCrmCreditAreaParagon() {
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

	public long getCreditRegionId() {
		return creditRegionId;
	}

	public void setCreditRegionId(long creditRegionId) {
		this.creditRegionId = creditRegionId;
	}

	public String getCreditTeamDescription() {
		return creditTeamDescription;
	}

	public void setCreditTeamDescription(String creditTeamDescription) {
		this.creditTeamDescription = creditTeamDescription;
	}
	

	@Override
    public String toString() {
        return "V_DIF_CRM_PARTY_CREDIT_AREA{" +
                //"id=" + id +
                "ORG_ID=" + orgId +
                "				" + creditRegionId  +// CREDIT_REGION_ID=
                "				" + creditTeamDescription ;// CREDIT_TEAM_DESCRIPTION=
    }
    
    @Override
   	public boolean isNew() {
   		return true;
   	}
}
