/********************************************************************
 * File Name:    ReportsControllerTest.java
 *
 * Date Created: Jul 22, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.binyu.myfinance.backend.AbstractIntegrationTest;
import org.binyu.myfinance.backend.daos.AccountAuditMapper;
import org.binyu.myfinance.backend.dtos.AccountTransaction.TransactionType;
import org.binyu.myfinance.backend.dtos.ExtAccountTransactionRecord;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.binyu.myfinance.backend.dtos.VirtualAccountUsageSummary;
import org.binyu.myfinance.backend.utils.AccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class ReportsControllerTest extends AbstractIntegrationTest
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  private PhysicalAccount pAccount1;
  private PhysicalAccount pAccount2;
  private VirtualAccount vAccount1;
  private VirtualAccount vAccount2;
  @Autowired
  private AccountAuditMapper auditRepo;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @BeforeClass
  public void prepareCommonTestData() throws SQLException
  {
    pAccount1 = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "from", "dummy");
    pAccount2 = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "to", "dummy");
    vAccount1 = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "from", "dummy", 1000);
    vAccount2 = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "to", "dummy", 2000);
  }

  @AfterClass
  public void cleanUp()
  {
    AccountTestUtils.deletePhysicalAccount(jdbcTemplate, pAccount1);
    AccountTestUtils.deletePhysicalAccount(jdbcTemplate, pAccount2);
    AccountTestUtils.deleteVirtualAccount(jdbcTemplate, vAccount1);
    AccountTestUtils.deleteVirtualAccount(jdbcTemplate, vAccount2);
  }

  @Test
  public void testGetMonthlyUsageSummary() throws Exception
  {
    long totalExpenseOfThisMonth = prepareTransactionRecords();

    MockHttpServletRequestBuilder myReq = get("/reports/monthlyUsageSummary")
        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"));

    MvcResult result = this.mockMvc
        .perform(myReq)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andReturn();
    // doing assertions
    List<VirtualAccountUsageSummary> summaryList = deserialize(result, new TypeReference<List<VirtualAccountUsageSummary>>()
    {
    });
    List<VirtualAccountUsageSummary> expList = new ArrayList<VirtualAccountUsageSummary>(1);
    expList.add(new VirtualAccountUsageSummary(vAccount1.getId(), vAccount1.getName(), vAccount1.getBudget(),
        totalExpenseOfThisMonth));
    expList.add(new VirtualAccountUsageSummary(vAccount2.getId(), vAccount2.getName(), vAccount2.getBudget(), 0));
    Assert.assertEquals(summaryList, expList);
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------
  private long prepareTransactionRecords()
  {
    Calendar now = Calendar.getInstance();
    Calendar beginingOfThisMonth = Calendar.getInstance();
    beginingOfThisMonth.set(Calendar.DAY_OF_MONTH, 1);

    Calendar endOfLastMonth = Calendar.getInstance();
    endOfLastMonth.add(Calendar.MONTH, -1);

    long totalExpenseOfThisMonth = 0;
    // Now: one transfer
    ExtAccountTransactionRecord transactionNow1 = new ExtAccountTransactionRecord(0, now.getTime(),
        TransactionType.TRANSFER
        , pAccount1.getId(), vAccount1.getId()
        , pAccount2.getId(), vAccount2.getId()
        , 100, "now"
        , pAccount1.getName(), vAccount1.getName()
        , pAccount2.getName(), vAccount2.getName());
    auditRepo.addRecord(transactionNow1);
    // Now: one expense
    transactionNow1 = new ExtAccountTransactionRecord(0, now.getTime(),
        TransactionType.EXPENSE
        , pAccount1.getId(), vAccount1.getId()
        , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
        , 211, "now"
        , pAccount1.getName(), vAccount1.getName()
        , null, null);
    auditRepo.addRecord(transactionNow1);
    totalExpenseOfThisMonth += transactionNow1.getAmount();
    // Now: one income
    transactionNow1 = new ExtAccountTransactionRecord(0, now.getTime(),
        TransactionType.INCOME
        , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
        , pAccount1.getId(), vAccount1.getId()
        , 300, "now"
        , null, null
        , pAccount1.getName(), vAccount1.getName());
    auditRepo.addRecord(transactionNow1);

    // BeginingOfThisMonth: one transfer
    ExtAccountTransactionRecord transactionBeginingOfThisMonth = new ExtAccountTransactionRecord(0, beginingOfThisMonth.getTime(),
        TransactionType.TRANSFER
        , pAccount1.getId(), vAccount1.getId()
        , pAccount2.getId(), vAccount2.getId()
        , 100, "now"
        , pAccount1.getName(), vAccount1.getName()
        , pAccount2.getName(), vAccount2.getName());
    auditRepo.addRecord(transactionBeginingOfThisMonth);
    // BeginingOfThisMonth: one expense
    transactionBeginingOfThisMonth = new ExtAccountTransactionRecord(0, beginingOfThisMonth.getTime(),
        TransactionType.EXPENSE
        , pAccount1.getId(), vAccount1.getId()
        , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
        , 202, "now"
        , pAccount1.getName(), vAccount1.getName()
        , null, null);
    auditRepo.addRecord(transactionBeginingOfThisMonth);
    totalExpenseOfThisMonth += transactionBeginingOfThisMonth.getAmount();
    // BeginingOfThisMonth: one income
    transactionBeginingOfThisMonth = new ExtAccountTransactionRecord(0, beginingOfThisMonth.getTime(),
        TransactionType.INCOME
        , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
        , pAccount1.getId(), vAccount1.getId()
        , 300, "now"
        , null, null
        , pAccount1.getName(), vAccount1.getName());
    auditRepo.addRecord(transactionBeginingOfThisMonth);

    // EndOfLastMonth: one transfer
    ExtAccountTransactionRecord transactionEndOfLastMonth = new ExtAccountTransactionRecord(0, endOfLastMonth.getTime(),
        TransactionType.TRANSFER
        , pAccount1.getId(), vAccount1.getId()
        , pAccount2.getId(), vAccount2.getId()
        , 100, "now"
        , pAccount1.getName(), vAccount1.getName()
        , pAccount2.getName(), vAccount2.getName());
    auditRepo.addRecord(transactionEndOfLastMonth);
    // EndOfLastMonth: one expense
    transactionEndOfLastMonth = new ExtAccountTransactionRecord(0, endOfLastMonth.getTime(),
        TransactionType.EXPENSE
        , pAccount1.getId(), vAccount1.getId()
        , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
        , 203, "now"
        , pAccount1.getName(), vAccount1.getName()
        , null, null);
    auditRepo.addRecord(transactionEndOfLastMonth);
    // EndOfLastMonth: one income
    transactionEndOfLastMonth = new ExtAccountTransactionRecord(0, endOfLastMonth.getTime(),
        TransactionType.INCOME
        , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
        , pAccount1.getId(), vAccount1.getId()
        , 300, "now"
        , null, null
        , pAccount1.getName(), vAccount1.getName());
    auditRepo.addRecord(transactionEndOfLastMonth);
    return totalExpenseOfThisMonth;
    /*
        ExtAccountTransactionRecord transactionOneDayAgo = new ExtAccountTransactionRecord(0, beginingOfThisMonth.getTime(),
            TransactionType.TRANSFER
            , pAccount1.getId(), vAccount1.getId()
            , pAccount2.getId(), vAccount2.getId()
            , 100, "oneDayAgo"
            , pAccount1.getName(), vAccount1.getName()
            , pAccount2.getName(), vAccount2.getName());
        auditRepo.addRecord(transactionOneDayAgo);*/

  }
  // ACCESSOR METHODS -----------------------------------------------

}
