/********************************************************************
 * File Name:    SubAccount.java
 *
 * Date Created: Jun 22, 2015
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
public class ExtAccountStore
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  private long physicalAccountId;
  private String physicalAccountName;
  private long virtualAccountId;
  private String virtualAccountName;
  private long amount;

  // CONSTRUCTORS ---------------------------------------------------

  public ExtAccountStore()
  {
  }

  public ExtAccountStore(long physicalAccountId, String physicalAccountName, long virtualAccountId, String virtualAccountName,
      long amount)
  {
    super();
    this.physicalAccountId = physicalAccountId;
    this.physicalAccountName = physicalAccountName;
    this.virtualAccountId = virtualAccountId;
    this.virtualAccountName = virtualAccountName;
    this.amount = amount;
  }

  // PUBLIC METHODS -------------------------------------------------

  public static ExtAccountStore newVirtualInstance(long id, String name, long amount)
  {
    return new ExtAccountStore(0, null, id, name, amount);
  }

  public static ExtAccountStore newPhysicalInstance(long id, String name, long amount)
  {
    return new ExtAccountStore(id, name, 0, null, amount);
  }

  @Override
  public String toString()
  {
    return "ExtAccountStore [physicalAccountId=" + physicalAccountId + ", physicalAccountName=" + physicalAccountName
        + ", virtualAccountId=" + virtualAccountId + ", virtualAccountName=" + virtualAccountName + ", amount=" + amount + "]";
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (amount ^ (amount >>> 32));
    result = prime * result + (int) (physicalAccountId ^ (physicalAccountId >>> 32));
    result = prime * result + ((physicalAccountName == null) ? 0 : physicalAccountName.hashCode());
    result = prime * result + (int) (virtualAccountId ^ (virtualAccountId >>> 32));
    result = prime * result + ((virtualAccountName == null) ? 0 : virtualAccountName.hashCode());
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
    ExtAccountStore other = (ExtAccountStore) obj;
    if (amount != other.amount)
      return false;
    if (physicalAccountId != other.physicalAccountId)
      return false;
    if (physicalAccountName == null)
    {
      if (other.physicalAccountName != null)
        return false;
    }
    else if (!physicalAccountName.equals(other.physicalAccountName))
      return false;
    if (virtualAccountId != other.virtualAccountId)
      return false;
    if (virtualAccountName == null)
    {
      if (other.virtualAccountName != null)
        return false;
    }
    else if (!virtualAccountName.equals(other.virtualAccountName))
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

  public String getPhysicalAccountName()
  {
    return physicalAccountName;
  }

  public void setPhysicalAccountName(String physicalAccountName)
  {
    this.physicalAccountName = physicalAccountName;
  }

  public long getVirtualAccountId()
  {
    return virtualAccountId;
  }

  public void setVirtualAccountId(long virtualAccountId)
  {
    this.virtualAccountId = virtualAccountId;
  }

  public String getVirtualAccountName()
  {
    return virtualAccountName;
  }

  public void setVirtualAccountName(String virtualAccountName)
  {
    this.virtualAccountName = virtualAccountName;
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
