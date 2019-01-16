package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyHierarchyParagon;

/**
 * Created by rribes on 30/10/2018.
 */
@Repository
public interface PartyHierarchyRepository extends JpaRepository<PartyHierarchyParagon, Long>, PartyHierarchyRepositoryCustom {
}
