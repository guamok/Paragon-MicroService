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
 * Created by rribes on 30/09/2018.
 */

@Entity
@Table(name="PD_STG_CRM_PARTY_RATING")
public class PartyCrmRatingParagon extends ImportEntity  implements Persistable<UUID>{

	/**
	 * 
	 * 
	 * 
	 * {
    "ORG_ID": "1174741",
    "RATING_TYPE": "DBL",
    "RATING": "A1",
    "RATING_DATE": "2002-02-27 00:00:00",
    "RATING_REVIEW_DATE": "",
    "RATING_APPROVAL_DATE": "",
    "RAI_KEY": "",
    "HAIRCUT_PERCENTAGE": 80,
    "RAI_DESCRIPTION": "",
    "TCP_RAT_OVERRIDE": "",
    "RATING_TYPE_DESCRIPTION": "DB Internal Local Rating",
    "HashCode": "5ECC363A6DD87788DD751C03F6AD1A90A9365CF85844FA2B79894765D568CEE6"
  },

     FOR US

	 {
	 "ORG_ID": "1174741",
    "RATING_TYPE": "DBL",
    "RATING": "A1",
    "RATING_DATE": "2002-02-27 00:00:00"
  },



	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "ORG_ID")
	 @JsonProperty("ORG_ID")
	 private String orgId;
	 

	@Column(name = "RATING")
    @JsonProperty("RATING")
    private String rating;

    @Column(name = "RATING_TYPE")
    @JsonProperty("RATING_TYPE")
    private String ratingType;

    @Column(name = "RATING_DATE")
    @JsonProperty("RATING_DATE")
    private String ratingDate;
    
    

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

    public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRatingType() {
		return ratingType;
	}

	public void setRatingType(String ratingType) {
		this.ratingType = ratingType;
	}

	public String getRatingDate() {
		return ratingDate;
	}

	public void setRatingDate(String ratingDate) {
		this.ratingDate = ratingDate;
	}

	public void setId(UUID id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "V_DIF_CRM_PARTY_RATING{" +
        		"ORG_ID=" + orgId +
                "				 " + rating +//RATING
                "				 " + ratingType +//RATING_TYPE=
                " 				 " + ratingDate//RATING_DATE= 
                ;
    }
    
    @Override
   	public boolean isNew() {
   		return true;
   	}
}
