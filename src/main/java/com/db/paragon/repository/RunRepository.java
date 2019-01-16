package com.db.paragon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.paragon.model.PartyParagon;

/**
 * Created by rribes on 22/10/2018.
 */

@Repository
public interface RunRepository extends JpaRepository<PartyParagon, Long> {

	 //LastRun findFirstByDateLoadLessThanEqualAndCode(Date dateLoad, String code);

    String findLastSuccessfulDate();
    
    Long findCurrentId()throws Exception;
    
    void procedureUpdate_job_log(Long id, String status, String msg);
    
    void procedureWrite_Import_Stat(String submissionRegion, String submissionPeriod, String feedType, String logMessage, Long id);
    
    boolean procedureTruncateTables();
    
    boolean procedureCrdsLoadDelta();
    
    boolean procedureCrdsLoadDeltaMapping();
    
    
}
