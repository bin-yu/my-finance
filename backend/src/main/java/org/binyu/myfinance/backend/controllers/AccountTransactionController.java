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

import org.binyu.myfinance.backend.dtos.AccountTransaction;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;
import org.binyu.myfinance.backend.exceptions.InvalidInputException;
import org.binyu.myfinance.backend.services.AccountTransactionMgrService;
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
@RequestMapping("/account_transactions")
public class AccountTransactionController
{
  // CONSTANTS ------------------------------------------------------

  private static final Logger LOG = LoggerFactory.getLogger(AccountTransactionController.class);
  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private AccountTransactionMgrService service;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  @RequestMapping(method = RequestMethod.POST)
  public AccountTransaction newTransaction(@RequestBody AccountTransaction transactionToDo) throws InvalidInputException
  {
    AccountTransaction actualTransaction = service.newTransaction(transactionToDo);
    return actualTransaction;
  }

  @RequestMapping(value = "/batch", method = RequestMethod.POST)
  public AccountTransaction[] batchApplyTransactions(@RequestBody AccountTransaction[] transactionsToDo)
      throws InvalidInputException
  {
    AccountTransaction[] actualTransaction = service.newTransactions(transactionsToDo);
    return actualTransaction;
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<AccountTransaction> searchTransactions(TransactionSearchFilter filter) throws InvalidInputException
  {
    return service.searchAudits(filter);
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------
}
