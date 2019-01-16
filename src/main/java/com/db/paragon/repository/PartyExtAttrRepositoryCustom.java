package com.db.paragon.repository;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by rribes on 08/11/2018.
 */
@NoRepositoryBean
public interface PartyExtAttrRepositoryCustom {

    void clean(String tableName);
    void clear();

}
