package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyAddressParagon;
import com.db.paragon.model.PartyExtParagon;

/**
 * Created by rribes on 08/11/2018.
 */
@Repository
public interface PartyExtAttrRepository extends JpaRepository<PartyExtParagon, Long>, PartyExtAttrRepositoryCustom{
}
