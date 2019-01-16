package com.db.paragon.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by rribes on 05/11/2018.
 */
@NoRepositoryBean
public interface PartyAddressRepositoryCustom {

    void clean(String tableName);
    void clear();

}
