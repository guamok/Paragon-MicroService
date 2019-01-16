package com.db.paragon.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by rribes on 12/09/2018.
 */
@NoRepositoryBean
public interface PartyCrmXrefRepositoryCustom {

    void clean(String tableName);
    void clear();

}
