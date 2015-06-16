/********************************************************************
 * File Name:    PhysicalAccountMapper.java
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

import org.binyu.myfinance.backend.dtos.PhysicalAccount;

public interface PhysicalAccountMapper
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  List<PhysicalAccount> getPhysicalAccountList();

  void addPhysicalAccount(PhysicalAccount accountToAdd);

  PhysicalAccount getPhysicalAccountById(long id);
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
