package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyCrmCreditAreaParagon;

/**
 * Created by rribes on 26/10/2018.
 */
@Repository
public interface PartyCrmCreditAreaRepository extends JpaRepository<PartyCrmCreditAreaParagon, Long>, PartyCrmCreditAreaRepositoryCustom {
}
