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
import org.binyu.myfinance.backend.exceptions.InvalidInputException;
import org.binyu.myfinance.backend.services.PhysicalAccountMgrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

  private static final Logger LOG = LoggerFactory.getLogger(PhysicalAccountController.class);
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

  @RequestMapping(method = RequestMethod.POST)
  public PhysicalAccount addPhysicalAccount(@RequestBody PhysicalAccount accountToAdd)
  {
    PhysicalAccount finalAccount = service.addPhysicalAccount(accountToAdd);
    return finalAccount;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void updatePhysicalAccount(@PathVariable long id, @RequestBody PhysicalAccount accountToUpdate)
      throws InvalidInputException
  {
    if (id != accountToUpdate.getId())
    {
      throw new InvalidInputException("The given account information is not for this id");
    }
    service.updatePhysicalAccount(accountToUpdate);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void deletePhysicalAccount(@PathVariable long id)
      throws InvalidInputException
  {
    service.deletePhysicalAccount(id);
  }
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------
}
