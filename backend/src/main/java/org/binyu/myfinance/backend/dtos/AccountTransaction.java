/********************************************************************
 * File Name:    AccountTransaction.java
 *
 * Date Created: Jun 16, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class AccountTransaction implements Cloneable
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------
  public static enum TransactionType
  {
    UNKNOWN(-1),
    INCOME(1),
    EXPENSE(2),
    TRANSFER(3), ;
    private int id;

    TransactionType(int id)
    {
      this.id = id;
    }

    public int getId()
    {
      return id;
    }

    public static TransactionType fromId(int id)
    {
      for (TransactionType type : TransactionType.values())
      {
        if (id == type.getId())
        {
          return type;
        }
      }
      return UNKNOWN;
    }

  }

  // INSTANCE VARIABLES ---------------------------------------------
  private long id;
  private Date date;
  private TransactionType type;
  private long fromPhysicalAccountId;
  private long fromVirtualAccountId;
  private long toPhysicalAccountId;
  private long toVirtualAccountId;
  private long amount;
  private String description;

  // CONSTRUCTORS ---------------------------------------------------

  /**
   * 
   */
  public AccountTransaction()
  {
    // TODO Auto-generated constructor stub
  }

  public AccountTransaction(long id, Date date, TransactionType type, long fromPhysicalAccountId, long fromVirtualAccountId,
      long toPhysicalAccountId, long toVirtualAccountId, long amount, String description)
  {
    super();
    this.id = id;
    this.date = date;
    this.type = type;
    this.fromPhysicalAccountId = fromPhysicalAccountId;
    this.fromVirtualAccountId = fromVirtualAccountId;
    this.toPhysicalAccountId = toPhysicalAccountId;
    this.toVirtualAccountId = toVirtualAccountId;
    this.amount = amount;
    this.description = description;
  }

  // PUBLIC METHODS -------------------------------------------------

  @Override
  public AccountTransaction clone() throws CloneNotSupportedException
  {
    return (AccountTransaction) super.clone();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (amount ^ (amount >>> 32));
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + (int) (fromPhysicalAccountId ^ (fromPhysicalAccountId >>> 32));
    result = prime * result + (int) (fromVirtualAccountId ^ (fromVirtualAccountId >>> 32));
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + (int) (toPhysicalAccountId ^ (toPhysicalAccountId >>> 32));
    result = prime * result + (int) (toVirtualAccountId ^ (toVirtualAccountId >>> 32));
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    AccountTransaction other = (AccountTransaction) obj;
    if (amount != other.amount)
      return false;
    if (date == null)
    {
      if (other.date != null)
        return false;
    }
    else if (!date.equals(other.date))
      return false;
    if (description == null)
    {
      if (other.description != null)
        return false;
    }
    else if (!description.equals(other.description))
      return false;
    if (fromPhysicalAccountId != other.fromPhysicalAccountId)
      return false;
    if (fromVirtualAccountId != other.fromVirtualAccountId)
      return false;
    if (id != other.id)
      return false;
    if (toPhysicalAccountId != other.toPhysicalAccountId)
      return false;
    if (toVirtualAccountId != other.toVirtualAccountId)
      return false;
    if (type != other.type)
      return false;
    return true;
  }

  @JsonIgnore
  public int getiType()
  {
    return type.getId();
  }

  @JsonIgnore
  public void setiType(int id)
  {
    type = TransactionType.fromId(id);
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public long getFromPhysicalAccountId()
  {
    return fromPhysicalAccountId;
  }

  public void setFromPhysicalAccountId(long fromPhysicalAccountId)
  {
    this.fromPhysicalAccountId = fromPhysicalAccountId;
  }

  public long getFromVirtualAccountId()
  {
    return fromVirtualAccountId;
  }

  public void setFromVirtualAccountId(long fromVirtualAccountId)
  {
    this.fromVirtualAccountId = fromVirtualAccountId;
  }

  public long getToPhysicalAccountId()
  {
    return toPhysicalAccountId;
  }

  public void setToPhysicalAccountId(long toPhysicalAccountId)
  {
    this.toPhysicalAccountId = toPhysicalAccountId;
  }

  public long getToVirtualAccountId()
  {
    return toVirtualAccountId;
  }

  public void setToVirtualAccountId(long toVirtualAccountId)
  {
    this.toVirtualAccountId = toVirtualAccountId;
  }

  public TransactionType getType()
  {
    return type;
  }

  public void setType(TransactionType type)
  {
    this.type = type;
  }

  public long getAmount()
  {
    return amount;
  }

  public void setAmount(long amount)
  {
    this.amount = amount;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

}
