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

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class VirtualAccount implements Cloneable
{

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  private long id;
  private String name;
  private String description;
  private long budget;
  private long amount;

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
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (amount ^ (amount >>> 32));
    result = prime * result + (int) (budget ^ (budget >>> 32));
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
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
    if (amount != other.amount)
      return false;
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

  public long getAmount()
  {
    return amount;
  }

  public void setAmount(long amount)
  {
    this.amount = amount;
  }

  public long getBudget()
  {
    return budget;
  }

  public void setBudget(long budget)
  {
    this.budget = budget;
  }

}
