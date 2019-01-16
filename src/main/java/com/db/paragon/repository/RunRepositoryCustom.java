package com.db.paragon.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;

/**
 * Created by rribes on 12/10/2018.
 */
@NoRepositoryBean
public interface RunRepositoryCustom {

	String findLastSuccessfulDate();

	Long findCurrentId() throws Exception;

	void procedureUpdate_job_log(Long id, String status, String msg);
	
	void procedureWrite_Import_Stat(String submissionRegion, String submissionPeriod, String feedType, String logMessage, Long id);
	
	boolean procedureTruncateTables();
	
	boolean procedureCrdsLoadDelta();

	boolean procedureCrdsLoadDeltaMapping();

}
