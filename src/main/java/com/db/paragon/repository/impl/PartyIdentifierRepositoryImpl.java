package com.db.paragon.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.db.paragon.repository.PartyIdentifierRepositoryCustom;

/**
 * Created by rribes on 30/10/2018.
 */
@Repository
public class PartyIdentifierRepositoryImpl implements PartyIdentifierRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    
    @Override
    @Transactional
    public void clean(String tableName) {
        String sql = "TRUNCATE TABLE " + tableName;
        entityManager.createNativeQuery(sql).executeUpdate();
    }
    
    @Override
    public void clear() {
        entityManager.clear();
    }
}
