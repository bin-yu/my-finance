/********************************************************************
 * File Name:    ReportsService.java
 *
 * Date Created: Jul 22, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.services;

import java.util.Calendar;
import java.util.List;

import org.binyu.myfinance.backend.daos.ReportsMapper;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;
import org.binyu.myfinance.backend.dtos.VirtualAccountUsageSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@Service
public class ReportsService
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------
  private static final Logger LOG = LoggerFactory.getLogger(ReportsService.class);
  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private ReportsMapper reportsRepo;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  public List<VirtualAccountUsageSummary> getMonthlyUsageSummary()
  {
    Calendar startOfThisMonth = Calendar.getInstance();
    startOfThisMonth.set(Calendar.DAY_OF_MONTH, 1);
    startOfThisMonth.set(Calendar.HOUR_OF_DAY, 0);
    startOfThisMonth.set(Calendar.MINUTE, 0);
    startOfThisMonth.set(Calendar.SECOND, 0);
    startOfThisMonth.set(Calendar.MILLISECOND, 0);
    TransactionSearchFilter filter = new TransactionSearchFilter(startOfThisMonth.getTime(), null);
    // TODO Auto-generated method stub
    return reportsRepo.getVAUsageSummary(filter);
  }
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
