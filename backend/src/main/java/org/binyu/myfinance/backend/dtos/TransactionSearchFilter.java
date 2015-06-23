/********************************************************************
 * File Name:    TransactionQueryFilter.java
 *
 * Date Created: Jun 17, 2015
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
public class TransactionSearchFilter
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------
  public static enum SortOrder
  {
    ASC,
    DESC
  }

  // INSTANCE VARIABLES ---------------------------------------------
  private Date fromDate;// include
  private Date toDate;// not include
  private SortOrder sortOrder = SortOrder.DESC;
  private int offset = 0;
  private int limit = 20;

  // CONSTRUCTORS ---------------------------------------------------

  public TransactionSearchFilter()
  {
  }

  public TransactionSearchFilter(Date fromDate, Date toDate)
  {
    super();
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public TransactionSearchFilter(Date fromDate, Date toDate, SortOrder sortOrder)
  {
    super();
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.sortOrder = sortOrder;
  }

  public TransactionSearchFilter(Date fromDate, Date toDate, SortOrder sortOrder, int offset, int limit)
  {
    super();
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.sortOrder = sortOrder;
    this.offset = offset;
    this.limit = limit;
  }

  // PUBLIC METHODS -------------------------------------------------

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

  @Override
  public String toString()
  {
    return "TransactionSearchFilter [fromDate=" + fromDate + ", toDate=" + toDate + ", sortOrder=" + sortOrder + ", offset="
        + offset + ", limit=" + limit + "]";
  }

  public Date getFromDate()
  {
    return fromDate;
  }

  public void setFromDate(Date fromDate)
  {
    this.fromDate = fromDate;
  }

  public Date getToDate()
  {
    return toDate;
  }

  public void setToDate(Date toDate)
  {
    this.toDate = toDate;
  }

  public String getSortOrder()
  {
    return sortOrder.name();
  }

  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = SortOrder.valueOf(sortOrder);
  }

  public int getOffset()
  {
    return offset;
  }

  public void setOffset(int offset)
  {
    this.offset = offset;
  }

  public int getLimit()
  {
    return limit;
  }

  public void setLimit(int limit)
  {
    this.limit = limit;
  }

}
