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
public class PhysicalAccount implements Cloneable
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  private long id;
  private String name;
  private String description;
  private List<ExtAccountStore> mappedVirtualAccounts = new ArrayList<ExtAccountStore>(0);

  // CONSTRUCTORS ---------------------------------------------------

  public PhysicalAccount()
  {
  }

  // PUBLIC METHODS -------------------------------------------------

  @Override
  public PhysicalAccount clone() throws CloneNotSupportedException
  {
    return (PhysicalAccount) super.clone();
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

  public PhysicalAccount(String name, String description)
  {
    super();
    this.name = name;
    this.description = description;
  }

  @Override
  public String toString()
  {
    return "PhysicalAccount [id=" + id + ", name=" + name + ", description=" + description + ", mappedVirtualAccounts="
        + mappedVirtualAccounts + "]";
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((mappedVirtualAccounts == null) ? 0 : mappedVirtualAccounts.hashCode());
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
    PhysicalAccount other = (PhysicalAccount) obj;
    if (description == null)
    {
      if (other.description != null)
        return false;
    }
    else if (!description.equals(other.description))
      return false;
    if (id != other.id)
      return false;
    if (mappedVirtualAccounts == null)
    {
      if (other.mappedVirtualAccounts != null)
        return false;
    }
    else if (!mappedVirtualAccounts.equals(other.mappedVirtualAccounts))
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
    for (ExtAccountStore va : mappedVirtualAccounts)
    {
      amount += va.getAmount();
    }
    return amount;
  }

  @JsonIgnore
  public void setAmount(long amount)
  {

  }

  public List<ExtAccountStore> getMappedVirtualAccounts()
  {
    return mappedVirtualAccounts;
  }

  public void setMappedVirtualAccounts(List<ExtAccountStore> mappedVirtualAccounts)
  {
    this.mappedVirtualAccounts = mappedVirtualAccounts;
  }

}
