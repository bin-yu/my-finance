/********************************************************************
 * File Name:    ReportsMapper.java
 *
 * Date Created: Jul 22, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;
import org.binyu.myfinance.backend.dtos.VirtualAccountUsageSummary;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public interface ReportsMapper
{
  // CONSTANTS ------------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  List<VirtualAccountUsageSummary> getVAUsageSummary(@Param("filter") TransactionSearchFilter filter);
}
