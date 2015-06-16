/********************************************************************
 * File Name:    PhysicalAccountMgrService.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.services;

import java.util.List;

import org.binyu.myfinance.backend.daos.PhysicalAccountMapper;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@Service
public class PhysicalAccountMgrService
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private PhysicalAccountMapper repo;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  public List<PhysicalAccount> getPhysicalAccountList()
  {
    return repo.getPhysicalAccountList();
  }

  // PROTECTED METHODS ----------------------------------------------

  @Transactional(rollbackFor = Throwable.class)
  public PhysicalAccount addPhysicalAccount(PhysicalAccount accountToAdd)
  {
    try
    {
      repo.addPhysicalAccount(accountToAdd);
      return accountToAdd;
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
