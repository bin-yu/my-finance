/********************************************************************
 * File Name:    PhysicalAccount.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Update with a detailed description of the interface/class.
 * 
 */
public class VirtualAccount implements Cloneable
{

  // CONSTANTS ------------------------------------------------------
  public static final long UNALLOCATED_ACCOUNT_ID = -1;
  public static final long NO_ACCOUNT = -99;
  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  private long id;
  private String name;
  private String description;
  private long budget;
  private List<ExtAccountStore> mappedPhysicalAccounts = new ArrayList<ExtAccountStore>(0);

  // CONSTRUCTORS ---------------------------------------------------

  public VirtualAccount()
  {
    // TODO Auto-generated constructor stub
  }

  // PUBLIC METHODS -------------------------------------------------

  @Override
  public VirtualAccount clone() throws CloneNotSupportedException
  {
    return (VirtualAccount) super.clone();
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

  @Override
  public String toString()
  {
    return "VirtualAccount [id=" + id + ", name=" + name + ", description=" + description + ", budget=" + budget
        + ", mappedPhysicalAccounts=" + mappedPhysicalAccounts + "]";
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (budget ^ (budget >>> 32));
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((mappedPhysicalAccounts == null) ? 0 : mappedPhysicalAccounts.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    VirtualAccount other = (VirtualAccount) obj;
    if (budget != other.budget)
      return false;
    if (description == null)
    {
      if (other.description != null)
        return false;
    }
    else if (!description.equals(other.description))
      return false;
    if (id != other.id)
      return false;
    if (mappedPhysicalAccounts == null)
    {
      if (other.mappedPhysicalAccounts != null)
        return false;
    }
    else if (!mappedPhysicalAccounts.equals(other.mappedPhysicalAccounts))
      return false;
    if (name == null)
    {
      if (other.name != null)
        return false;
    }
    else if (!name.equals(other.name))
      return false;
    return true;
  }

  public VirtualAccount(String name, String description)
  {
    super();
    this.name = name;
    this.description = description;
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  @JsonProperty
  public long getAmount()
  {
    long amount = 0;
    for (ExtAccountStore va : mappedPhysicalAccounts)
    {
      amount += va.getAmount();
    }
    return amount;
  }

  @JsonIgnore
  public void setAmount(long amount)
  {
  }

  public long getBudget()
  {
    return budget;
  }

  public void setBudget(long budget)
  {
    this.budget = budget;
  }

  public List<ExtAccountStore> getMappedPhysicalAccounts()
  {
    return mappedPhysicalAccounts;
  }

  public void setMappedPhysicalAccounts(List<ExtAccountStore> mappedPhysicalAccounts)
  {
    this.mappedPhysicalAccounts = mappedPhysicalAccounts;
  }

}
