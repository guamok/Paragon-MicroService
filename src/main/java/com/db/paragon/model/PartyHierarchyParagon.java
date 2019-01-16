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
 * Created by rribes on 12/10/2018.
 */

@Entity
@Table(name = "PD_STG_PARTY_HIERARCHY")
public class PartyHierarchyParagon extends ImportEntity implements Persistable<UUID>{

	 /**
	 * {
    "HIERARCHY_TYPE": "CREDIT",
    "ULTIMATE_PARENT_ORG_ID": "6545750",
    "PARENT_ORG_ID": "-1"
  }

Completa
{
    "HIERARCHY_TYPE": "CREDIT",
    "ULTIMATE_PARENT_ORG_ID": "8353020",
    "PARENT_ORG_ID": "-1",
    "CHILD_ORG_ID": "8353020",
    "VALID_FROM": "",
    "HashCode": "FC50B38B8CCA337824329D6F12C12AEDBB16BE8DB8FF0F9B641DD7BBC224ACB1"
  },

attributes=HIERARCHY_TYPE%2CPARENT_ORG_ID%2CULTIMATE_PARENT_ORG_ID%2CCHILD_ORG_ID

	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(name = "HIERARCHY_TYPE")
	@JsonProperty("HIERARCHY_TYPE")
	private String hierarchyType;
	
    @Column(name = "ULTIMATE_PARENT_ORG_ID")
    @JsonProperty("ULTIMATE_PARENT_ORG_ID")
    private String ultimatePartyId;
    
    @Column(name = "CHILD_ORG_ID")
    @JsonProperty("CHILD_ORG_ID")
    private String childOrgId;
    
	@Column(name = "PARENT_ORG_ID")
    @JsonProperty("PARENT_ORG_ID")
    private String partyId;

    public PartyHierarchyParagon() {
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParentOrgId() {
        return partyId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.partyId = parentOrgId;
    }

    public String getUltimatePartyId() {
        return ultimatePartyId;
    }

    public void setUltimateParentOrgId(String ultimateParentOrgId) {
        this.ultimatePartyId = ultimateParentOrgId;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }
    
    public String getChildOrgId() {
		return childOrgId;
	}

	public void setChildOrgId(String childOrgId) {
		this.childOrgId = childOrgId;
	}


    @Override
    public String toString() {
        return "V_DIF_PARTY_HIERARCHY{" +
                //"id=" + id +
                "PARENT_ORG_ID=" + partyId +
                "					 " + ultimatePartyId  +//ULTIMATE_PARENT_ORG_ID=
                "					 " + hierarchyType + //HIERARCHY_TYPE=
                "					 " + childOrgId//CHILD_ORG_ID=
                ;
    }
    
    @Override
   	public boolean isNew() {
   		return true;
   	}
}
