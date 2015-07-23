/********************************************************************
 * File Name:    ReportsController.java
 *
 * Date Created: Jul 22, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.controllers;

import java.util.List;

import org.binyu.myfinance.backend.dtos.VirtualAccountUsageSummary;
import org.binyu.myfinance.backend.exceptions.InvalidInputException;
import org.binyu.myfinance.backend.services.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@RestController
@RequestMapping("/reports")
public class ReportsController
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private ReportsService service;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @RequestMapping(value = "/monthlyUsageSummary", method = RequestMethod.GET)
  public List<VirtualAccountUsageSummary> getMonthlyUsageSummary() throws InvalidInputException
  {
    return service.getMonthlyUsageSummary();
  }
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
