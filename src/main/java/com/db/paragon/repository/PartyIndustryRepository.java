package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyIndustryParagon;

/**
 * Created by rribes on 30/10/2018.
 */
@Repository
public interface PartyIndustryRepository extends JpaRepository<PartyIndustryParagon, Long>, PartyIndustryRepositoryCustom {
}
