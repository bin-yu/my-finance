/********************************************************************
 * File Name:    ExtVirtualAccountMonthlyUsageSummary.java
 *
 * Date Created: Jul 22, 2015
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
public class VirtualAccountUsageSummary
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  private long id;
  private String name;
  private long budget;
  private long expense;

  // CONSTRUCTORS ---------------------------------------------------

  public VirtualAccountUsageSummary()
  {
    super();
  }

  public VirtualAccountUsageSummary(long id, String name, long budget, long used)
  {
    super();
    this.id = id;
    this.name = name;
    this.budget = budget;
    this.expense = used;
  }

  // PUBLIC METHODS -------------------------------------------------

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (budget ^ (budget >>> 32));
    result = prime * result + (int) (expense ^ (expense >>> 32));
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
    VirtualAccountUsageSummary other = (VirtualAccountUsageSummary) obj;
    if (budget != other.budget)
      return false;
    if (expense != other.expense)
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

  public long getBudget()
  {
    return budget;
  }

  public void setBudget(long budget)
  {
    this.budget = budget;
  }

  public long getExpense()
  {
    return expense;
  }

  public void setExpense(long expense)
  {
    this.expense = expense;
  }

}
