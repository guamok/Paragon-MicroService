package com.db.paragon.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by rribes on 26/10/2018.
 */
@NoRepositoryBean
public interface PartyCrmCreditAreaRepositoryCustom {

    void clean(String tableName);
    void clear();

}
