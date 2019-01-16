package com.db.paragon.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by rribes on 30/10/2018.
 */
@NoRepositoryBean
public interface PartyIndustryRepositoryCustom {

    void clean(String tableName);
    void clear();

}
