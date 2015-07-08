/********************************************************************
 * File Name:    AccountAuditMapper.java
 *
 * Date Created: Jun 16, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.binyu.myfinance.backend.dtos.AccountTransaction;
import org.binyu.myfinance.backend.dtos.ExtAccountTransactionRecord;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public interface AccountAuditMapper
{

  // CONSTANTS ------------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  void addRecord(AccountTransaction transactionToDo);

  AccountTransaction getRecord(long id);

  List<ExtAccountTransactionRecord> searchRecords(@Param("filter") TransactionSearchFilter filter, RowBounds rowBounds);
}
