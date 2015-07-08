/********************************************************************
 * File Name:    AccountTransactionMgrService.java
 *
 * Date Created: Jun 16, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.services;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.binyu.myfinance.backend.daos.AccountAuditMapper;
import org.binyu.myfinance.backend.daos.AccountStoreMapper;
import org.binyu.myfinance.backend.daos.PhysicalAccountMapper;
import org.binyu.myfinance.backend.daos.VirtualAccountMapper;
import org.binyu.myfinance.backend.dtos.AccountStore;
import org.binyu.myfinance.backend.dtos.AccountTransaction;
import org.binyu.myfinance.backend.dtos.ExtAccountTransactionRecord;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;
import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.binyu.myfinance.backend.exceptions.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@Service
public class AccountTransactionMgrService
{

  // CONSTANTS ------------------------------------------------------

  private static final Logger LOG = LoggerFactory.getLogger(AccountTransactionMgrService.class);
  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private AccountAuditMapper auditRepo;
  @Autowired
  private AccountStoreMapper accountStoreRepo;
  @Autowired
  private PhysicalAccountMapper pAccountRepo;
  @Autowired
  private VirtualAccountMapper vAccountRepo;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @Transactional(rollbackFor = Throwable.class)
  public AccountTransaction newTransaction(AccountTransaction transactionToDo) throws InvalidInputException
  {

    LOG.info("executing new transaction\n:" + transactionToDo);
    // do validations
    if (transactionToDo.getAmount() <= 0)
    {
      throw new InvalidInputException("The transaction amount must be greater than zero!");
    }
    boolean hasFrom = validateAccount(transactionToDo.getFromPhysicalAccountId(), transactionToDo.getFromVirtualAccountId());
    boolean hasTo = validateAccount(transactionToDo.getToPhysicalAccountId(), transactionToDo.getToVirtualAccountId());

    // do transactions
    if (hasFrom)
    {
      LOG.info("Substracting " + transactionToDo.getAmount() + " money from Account[" + transactionToDo.getFromPhysicalAccountId()
          + "," + transactionToDo.getFromVirtualAccountId() + "]...");
      updateAccountStoreAmount(transactionToDo.getFromPhysicalAccountId(), transactionToDo.getFromVirtualAccountId(),
          -transactionToDo.getAmount());
    }
    if (hasTo)
    {
      LOG.info("Adding " + transactionToDo.getAmount() + " money to Account[" + transactionToDo.getToPhysicalAccountId() + ","
          + transactionToDo.getToVirtualAccountId() + "]...");

      updateAccountStoreAmount(transactionToDo.getToPhysicalAccountId(), transactionToDo.getToVirtualAccountId(),
          transactionToDo.getAmount());
    }
    // add audit record
    LOG.info("adding audit record...");
    auditRepo.addRecord(transactionToDo);
    return transactionToDo;
  }

  private boolean validateAccount(long physicalAccountId, long virtualAccountId) throws InvalidInputException
  {
    boolean has = false;
    if (physicalAccountId != PhysicalAccount.NO_ACCOUNT
        && virtualAccountId != VirtualAccount.NO_ACCOUNT)
    {
      if ((pAccountRepo.getPhysicalAccountById(physicalAccountId) == null) ||
          (vAccountRepo.getVirtualAccountById(virtualAccountId) == null))
      {
        throw new InvalidInputException("The from account does not exist!");
      }
      has = true;
    }
    return has;
  }

  public List<ExtAccountTransactionRecord> searchAudits(TransactionSearchFilter filter)
  {
    return auditRepo.searchRecords(filter, new RowBounds(filter.getOffset(), filter.getLimit()));
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
  public AccountTransaction[] newTransactions(AccountTransaction[] transactionsToDo) throws InvalidInputException
  {
    for (AccountTransaction transaction : transactionsToDo)
    {
      newTransaction(transaction);
    }
    return transactionsToDo;
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  private void updateAccountStoreAmount(long physicalAccountId, long virtualAccountId, long amount)
  {
    AccountStore store = accountStoreRepo.getStore(physicalAccountId,
        virtualAccountId);
    if (store == null)
    {
      accountStoreRepo.addStore(new AccountStore(physicalAccountId, virtualAccountId, amount));
    }
    else
    {
      store.setAmount(store.getAmount() + amount);
      accountStoreRepo.updateStore(store);
    }
  }

  // ACCESSOR METHODS -----------------------------------------------

}
