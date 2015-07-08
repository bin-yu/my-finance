/********************************************************************
 * File Name:    ExtAccountTransactionRecord.java
 *
 * Date Created: Jul 8, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.dtos;

import java.util.Date;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class ExtAccountTransactionRecord extends AccountTransaction
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  private String fromPhysicalAccountName;
  private String fromVirtualAccountName;
  private String toPhysicalAccountName;
  private String toVirtualAccountName;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

  public ExtAccountTransactionRecord()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  public ExtAccountTransactionRecord(long id, Date date, TransactionType type, long fromPhysicalAccountId,
      long fromVirtualAccountId, long toPhysicalAccountId, long toVirtualAccountId, long amount, String description
      , String fromPhysicalAccountName, String fromVirtualAccountName, String toPhysicalAccountName, String toVirtualAccountName)
  {
    super(id, date, type, fromPhysicalAccountId, fromVirtualAccountId, toPhysicalAccountId, toVirtualAccountId, amount, description);
    this.fromPhysicalAccountName = fromPhysicalAccountName;
    this.fromVirtualAccountName = fromVirtualAccountName;
    this.toPhysicalAccountName = toPhysicalAccountName;
    this.toVirtualAccountName = toVirtualAccountName;
  }

  public String getFromPhysicalAccountName()
  {
    return fromPhysicalAccountName;
  }

  public void setFromPhysicalAccountName(String fromPhysicalAccountName)
  {
    this.fromPhysicalAccountName = fromPhysicalAccountName;
  }

  public String getFromVirtualAccountName()
  {
    return fromVirtualAccountName;
  }

  public void setFromVirtualAccountName(String fromVirtualAccountName)
  {
    this.fromVirtualAccountName = fromVirtualAccountName;
  }

  public String getToPhysicalAccountName()
  {
    return toPhysicalAccountName;
  }

  public void setToPhysicalAccountName(String toPhysicalAccountName)
  {
    this.toPhysicalAccountName = toPhysicalAccountName;
  }

  public String getToVirtualAccountName()
  {
    return toVirtualAccountName;
  }

  public void setToVirtualAccountName(String toVirtualAccountName)
  {
    this.toVirtualAccountName = toVirtualAccountName;
  }

}
