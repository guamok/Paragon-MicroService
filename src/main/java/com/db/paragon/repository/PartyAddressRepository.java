package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyAddressParagon;

/**
 * Created by rribes on 05/11/2018.
 */
@Repository
public interface PartyAddressRepository extends JpaRepository<PartyAddressParagon, Long>, PartyAddressRepositoryCustom {
}
