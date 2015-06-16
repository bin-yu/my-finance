/********************************************************************
 * File Name:    PhysicalAccountController.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.controllers;

import java.util.List;

import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.binyu.myfinance.backend.services.PhysicalAccountMgrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@RestController
@RequestMapping("/physical_accounts")
public class PhysicalAccountController
{
  // CONSTANTS ------------------------------------------------------

  private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private PhysicalAccountMgrService service;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  @RequestMapping(method = RequestMethod.GET)
  public List<PhysicalAccount> getPhysicalAccountList()
  {
    return service.getPhysicalAccountList();
  }

  @RequestMapping(method = RequestMethod.PUT)
  public PhysicalAccount addPhysicalAccount(@RequestBody PhysicalAccount accountToAdd)
  {
    PhysicalAccount finalAccount = service.addPhysicalAccount(accountToAdd);
    return finalAccount;
  }
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------
}
