package com.db.paragon;

import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.WebApplicationInitializer;


 
@SpringBootApplication
@EnableAsync 
//@EnableScheduling jajajajaja Conflictosssss de la nueva rama
@EnableJpaRepositories
@ComponentScan(value = "com.db.paragon")
@Configuration
@PropertySource(value="classpath:paragon/config/application.properties")
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer{
	
	private static final Logger LOGGER = LogManager.getLogger(Application.class);
	
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
    	
        return application.sources(Application.class);
    }

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public Executor asyncExecutor(){
    	LOGGER.error("Entra en asyncExecutor ");
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	executor.setCorePoolSize(20);
    	executor.setMaxPoolSize(20);
    	executor.setQueueCapacity(500);
    	executor.setThreadNamePrefix("thread-");
    	executor.initialize();
    	return executor;
    }
    
}
