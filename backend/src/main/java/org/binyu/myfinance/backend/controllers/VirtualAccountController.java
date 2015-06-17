/********************************************************************
 * File Name:    VirtualAccountController.java
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

import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.binyu.myfinance.backend.exceptions.InvalidInputException;
import org.binyu.myfinance.backend.services.VirtualAccountMgrService;
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
@RequestMapping("/virtual_accounts")
public class VirtualAccountController
{
  // CONSTANTS ------------------------------------------------------

  private static final Logger LOG = LoggerFactory.getLogger(VirtualAccountController.class);
  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private VirtualAccountMgrService service;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  @RequestMapping(method = RequestMethod.GET)
  public List<VirtualAccount> getVirtualAccountList()
  {
    return service.getVirtualAccountList();
  }

  @RequestMapping(method = RequestMethod.POST)
  public VirtualAccount addVirtualAccount(@RequestBody VirtualAccount accountToAdd)
  {
    VirtualAccount finalAccount = service.addVirtualAccount(accountToAdd);
    return finalAccount;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void updateVirtualAccount(@PathVariable long id, @RequestBody VirtualAccount accountToUpdate)
      throws InvalidInputException
  {
    if (id != accountToUpdate.getId())
    {
      throw new InvalidInputException("The given account information is not for this id");
    }
    service.updateVirtualAccount(accountToUpdate);
  }
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------
}
