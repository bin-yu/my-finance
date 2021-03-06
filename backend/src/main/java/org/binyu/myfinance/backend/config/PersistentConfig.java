/********************************************************************
 * File Name:    PersistentConfig.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@Configuration
@MapperScan(basePackages = "org.binyu.myfinance.backend.daos")
public class PersistentConfig
{
  // CONSTANTS ------------------------------------------------------

  private static final String MYBATIS_CONFIG_XML = "mybatis-config.xml";

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @Bean
  public SqlSessionFactoryBean createSessionFactory(DataSource dataSource)
  {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    ClassPathResource configLocation = new ClassPathResource(MYBATIS_CONFIG_XML);
    if (configLocation.exists())
    {
      sessionFactory.setConfigLocation(configLocation);
    }
    DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
    Properties p = new Properties();
    // p.setProperty("Oracle", "oracle");
    p.setProperty("HSQL", "hsql");
    databaseIdProvider.setProperties(p);
    sessionFactory.setDatabaseIdProvider(databaseIdProvider);
    return sessionFactory;
  }
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
