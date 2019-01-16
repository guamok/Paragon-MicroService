package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyCrmXrefParagon;

/**
 * Created by rribes on 12/09/2018.
 */
@Repository
public interface PartyCrmXrefRepository extends JpaRepository<PartyCrmXrefParagon, Long>, PartyCrmXrefRepositoryCustom {
}
