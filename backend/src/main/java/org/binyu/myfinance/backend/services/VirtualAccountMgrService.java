/********************************************************************
 * File Name:    VirtualAccountMgrService.java
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

import org.binyu.myfinance.backend.daos.VirtualAccountMapper;
import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@Service
public class VirtualAccountMgrService
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private VirtualAccountMapper repo;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  public List<VirtualAccount> getVirtualAccountList()
  {
    return repo.getVirtualAccountList();
  }

  // PROTECTED METHODS ----------------------------------------------

  @Transactional(rollbackFor = Throwable.class)
  public VirtualAccount addVirtualAccount(VirtualAccount accountToAdd)
  {
    try
    {
      repo.addVirtualAccount(accountToAdd);
      return accountToAdd;
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  public void updateVirtualAccount(VirtualAccount accountToUpdate)
  {
    repo.updateVirtualAccount(accountToUpdate);
  }

  public void deleteVirtualAccount(long id)
  {
    repo.deleteVirtualAccount(id);
  }

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
