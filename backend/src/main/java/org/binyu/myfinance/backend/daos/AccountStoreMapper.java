/********************************************************************
 * File Name:    AccountStoreMapper.java
 *
 * Date Created: Jun 16, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.daos;

import org.apache.ibatis.annotations.Param;
import org.binyu.myfinance.backend.dtos.AccountStore;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public interface AccountStoreMapper
{

  // CONSTANTS ------------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  AccountStore getStore(@Param(value = "physicalAccountId") long physicalAccountId,
      @Param(value = "virtualAccountId") long virtualAccountId);

  void updateStore(AccountStore store);

  void addStore(AccountStore store);
}
