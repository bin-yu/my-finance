/********************************************************************
 * File Name:    VirtualAccountMapper.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.daos;

import java.util.List;

import org.binyu.myfinance.backend.dtos.VirtualAccount;

public interface VirtualAccountMapper
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  List<VirtualAccount> getVirtualAccountList();

  void addVirtualAccount(VirtualAccount accountToAdd);

  VirtualAccount getVirtualAccountById(long id);

  void updateVirtualAccount(VirtualAccount accountToUpdate);

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
