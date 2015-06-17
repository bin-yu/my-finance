/********************************************************************
 * File Name:    AccountStore.java
 *
 * Date Created: Jun 16, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.dtos;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class AccountStore
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  private long physicalAccountId;
  private long virtualAccountId;
  private long amount;

  // CONSTRUCTORS ---------------------------------------------------

  /**
   * 
   */
  public AccountStore()
  {
    // TODO Auto-generated constructor stub
  }

  public AccountStore(long physicalAccountId, long virtualAccountId, long amount)
  {
    super();
    this.physicalAccountId = physicalAccountId;
    this.virtualAccountId = virtualAccountId;
    this.amount = amount;
  }

  // PUBLIC METHODS -------------------------------------------------

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (amount ^ (amount >>> 32));
    result = prime * result + (int) (physicalAccountId ^ (physicalAccountId >>> 32));
    result = prime * result + (int) (virtualAccountId ^ (virtualAccountId >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AccountStore other = (AccountStore) obj;
    if (amount != other.amount)
      return false;
    if (physicalAccountId != other.physicalAccountId)
      return false;
    if (virtualAccountId != other.virtualAccountId)
      return false;
    return true;
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

  public long getPhysicalAccountId()
  {
    return physicalAccountId;
  }

  public void setPhysicalAccountId(long physicalAccountId)
  {
    this.physicalAccountId = physicalAccountId;
  }

  public long getVirtualAccountId()
  {
    return virtualAccountId;
  }

  public void setVirtualAccountId(long virtualAccountId)
  {
    this.virtualAccountId = virtualAccountId;
  }

  public long getAmount()
  {
    return amount;
  }

  public void setAmount(long amount)
  {
    this.amount = amount;
  }
}
