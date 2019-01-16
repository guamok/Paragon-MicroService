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
 * Created by rribes on 09/11/2018.
 */

@Entity
@Table(name = "PD_STG_CRM_PARTY_CONTACT")
public class PartyCrmContactParagon extends ImportEntity implements Persistable<UUID>{

	 /**
	 * {
	 * a
	 * 
	 * 
  }

{
    "ORG_ID": "441301",
    "FIRST_NAME": "Rita",
    "LAST_NAME": "Jaafari"
  },



  {
    "ORG_ID": "441301",
    "CONTACT_TYPE_CODE": "KCP",
    "CONTACT_TYPE_DESCRIPTION": "Key Contact Person",
    "CONTACT_LEVEL": "L",
    "FIRST_NAME": "Rita",
    "LAST_NAME": "Jaafari",
    "TELEPHONE": "+49(69)910-37312",
    "FAX": "",
    "E_MAIL": "dummy@db.com",
    "NAME_STRING": "",
    "CONTACT_ID": 20111,
    "HashCode": "C5DCE886452405C7349DFF615EB2A5A694C583B2BAFE6E6337FB409B812A5D10"
  },




			ORG_ID%2CFIRST_NAME%2CLAST_NAME%2CCONTACT_TYPE_CODE
	 */
	private static final long serialVersionUID = 1L;
	// PARENT_ORG_ID%2CULTIMATE_PARENT_ORG_ID";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(name = "ORG_ID")
	@JsonProperty("ORG_ID")
	private String orgId;
	

    @Column(name = "FIRST_NAME")
    @JsonProperty("FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    @JsonProperty("LAST_NAME")
    private String lastName;
    
    @Column(name = "CONTACT_TYPE_CODE")
    @JsonProperty("CONTACT_TYPE_CODE")
    private String contactTypeCode;
    
    

    public PartyCrmContactParagon() {
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactTypeCode() {
		return contactTypeCode;
	}

	public void setContactTypeCode(String contactTypeCode) {
		this.contactTypeCode = contactTypeCode;
	}

	

	@Override
    public String toString() {
        return "V_DIF_CRM_PARTY_CONTACT{" +
                //"id=" + id +
                "ORG_ID=" + orgId +
                "				" + firstName + ", "+ lastName ;// FIRST_NAME + LAST_NAME
    }
    
    @Override
   	public boolean isNew() {
   		return true;
   	}
}
