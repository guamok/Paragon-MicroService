package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyCrmContactParagon;

/**
 * Created by rribes on 26/10/2018.
 */
@Repository
public interface PartyCrmContactRepository extends JpaRepository<PartyCrmContactParagon, Long>, PartyCrmContactRepositoryCustom {
}
