package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyAttrParagon;
import com.db.paragon.model.PartyCrmRatingParagon;

/**
 * Created by rribes on 26/10/2018.
 */
@Repository
public interface PartyCrmAttrRepository extends JpaRepository<PartyAttrParagon, Long>, PartyCrmAttrRepositoryCustom {
}
