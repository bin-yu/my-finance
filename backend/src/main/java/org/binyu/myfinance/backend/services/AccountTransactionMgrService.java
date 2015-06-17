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
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;
import org.binyu.myfinance.backend.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@Service
public class AccountTransactionMgrService
{

  // CONSTANTS ------------------------------------------------------

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
    // do validations
    if (transactionToDo.getAmount() <= 0)
    {
      throw new InvalidInputException("The transaction amount must be greater than zero!");
    }
    boolean hasFrom = false;
    boolean hasTo = false;
    if (transactionToDo.getFromPhysicalAccountId() >= 0 && transactionToDo.getFromVirtualAccountId() >= 0)
    {
      if ((pAccountRepo.getPhysicalAccountById(transactionToDo.getFromPhysicalAccountId()) == null) ||
          (vAccountRepo.getVirtualAccountById(transactionToDo.getFromVirtualAccountId()) == null))
      {
        throw new InvalidInputException("The from account does not exist!");
      }
      hasFrom = true;
    }
    if (transactionToDo.getToPhysicalAccountId() >= 0 && transactionToDo.getToVirtualAccountId() >= 0)
    {
      if ((pAccountRepo.getPhysicalAccountById(transactionToDo.getToPhysicalAccountId()) == null) ||
          (vAccountRepo.getVirtualAccountById(transactionToDo.getToVirtualAccountId()) == null))
      {
        throw new InvalidInputException("The to account does not exist!");
      }
      hasTo = true;
    }
    // do transactions
    if (hasFrom)
    {
      updateAccountStoreAmount(transactionToDo.getFromPhysicalAccountId(), transactionToDo.getFromVirtualAccountId(),
          -transactionToDo.getAmount());
    }
    if (hasTo)
    {
      updateAccountStoreAmount(transactionToDo.getToPhysicalAccountId(), transactionToDo.getToVirtualAccountId(),
          transactionToDo.getAmount());
    }
    // add audit record
    auditRepo.addRecord(transactionToDo);
    return transactionToDo;
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

  public List<AccountTransaction> searchAudits(TransactionSearchFilter filter)
  {
    return auditRepo.searchRecords(filter, new RowBounds(filter.getOffset(), filter.getLimit()));
  }

}
