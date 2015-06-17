/********************************************************************
 * File Name:    AccountTransactionControllerTest.java
 *
 * Date Created: Jun 16, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.binyu.myfinance.backend.AbstractIntegrationTest;
import org.binyu.myfinance.backend.daos.AccountAuditMapper;
import org.binyu.myfinance.backend.daos.AccountStoreMapper;
import org.binyu.myfinance.backend.dtos.AccountStore;
import org.binyu.myfinance.backend.dtos.AccountTransaction;
import org.binyu.myfinance.backend.dtos.AccountTransaction.TransactionType;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter.SortOrder;
import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.binyu.myfinance.backend.utils.AccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class AccountTransactionControllerTest extends AbstractIntegrationTest
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  private AccountStoreMapper actStoreMapper;
  @Autowired
  private AccountAuditMapper auditRepo;

  private PhysicalAccount fromPAccount;
  private PhysicalAccount toPAccount;
  private VirtualAccount fromVAccount;
  private VirtualAccount toVAccount;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @BeforeClass
  public void prepareBuiltinAccountAndTransactionData() throws SQLException
  {
    fromPAccount = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "from", "dummy");
    toPAccount = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "to", "dummy");
    fromVAccount = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "from", "dummy");
    toVAccount = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "to", "dummy");

  }

  @Test
  public void testNewTransactionWithValidInput() throws Exception
  {
    // prepare the accounts related
    long fromAmount = 1000;
    AccountTestUtils.initAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId(), fromAmount);
    AccountStore dbStore = actStoreMapper.getStore(fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount);
    // prepare other data
    int amountToTransfer = 100;
    AccountTransaction transactionToDo = new AccountTransaction(0, new Date(), TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , amountToTransfer, "desc");

    MvcResult result = this.mockMvc
        .perform(
            post("/account_transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(transactionToDo))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    // verify the transaction happens in db
    dbStore = actStoreMapper.getStore(fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount - amountToTransfer);
    dbStore = actStoreMapper.getStore(toPAccount.getId(), toVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), amountToTransfer);
    // verify the audit record in db
    AccountTransaction rtTrans = deserialize(result, AccountTransaction.class);
    AccountTransaction dbTrans = auditRepo.getRecord(rtTrans.getId());
    transactionToDo.setId(rtTrans.getId());
    Assert.assertEquals(rtTrans, transactionToDo);
    Assert.assertEquals(dbTrans, transactionToDo);
  }

  @Test(dataProvider = "testSearchTransactionsData")
  public void testSearchTransactions(TransactionSearchFilter filter, List<AccountTransaction> expTransList) throws Exception
  {
    // prepareSearchTransactionData();
    // do the search
    MockHttpServletRequestBuilder myReq = get("/account_transactions")
        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        .param("sortOrder", filter.getSortOrder())
        .param("offset", String.valueOf(filter.getOffset()))
        .param("limit", String.valueOf(filter.getLimit()));

    if (filter.getFromDate() != null)
    {
      myReq = myReq.param("fromDate", filter.getFromDate().toString());
    }
    if (filter.getToDate() != null)
    {
      myReq = myReq.param("toDate", filter.getToDate().toString());
    }

    MvcResult result = this.mockMvc
        .perform(myReq)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    List<AccountTransaction> retTransList = deserialize(result, new TypeReference<List<AccountTransaction>>()
    {
    });
    Assert.assertEquals(retTransList, expTransList);
  }

  // PROTECTED METHODS ----------------------------------------------

  @DataProvider(name = "testSearchTransactionsData")
  private Object[][] testSearchTransactionsData()
  {
    // prepare transaction record in db
    Calendar now = Calendar.getInstance();
    Calendar oneDayAgo = Calendar.getInstance();
    oneDayAgo.add(Calendar.DAY_OF_MONTH, -1);
    Calendar oneMonthAgo = Calendar.getInstance();
    oneMonthAgo.add(Calendar.MONTH, -1);

    Calendar olderThanOneMonth = Calendar.getInstance();
    olderThanOneMonth.add(Calendar.MONTH, -1);
    olderThanOneMonth.add(Calendar.DAY_OF_MONTH, -1);
    AccountTransaction transactionNow1 = new AccountTransaction(0, now.getTime(), TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , 100, "now");
    auditRepo.addRecord(transactionNow1);

    AccountTransaction transactionOneDayAgo = new AccountTransaction(0, oneDayAgo.getTime(), TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , 100, "oneDayAgo");
    auditRepo.addRecord(transactionOneDayAgo);

    AccountTransaction transactionOlderThanOneMonth = new AccountTransaction(0, olderThanOneMonth.getTime(),
        TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , 200, "olderThanOneMonth");
    auditRepo.addRecord(transactionOlderThanOneMonth);

    List<AccountTransaction> fullList = new ArrayList<AccountTransaction>(3);
    fullList.add(transactionOlderThanOneMonth);
    fullList.add(transactionOneDayAgo);
    fullList.add(transactionNow1);

    List<AccountTransaction> fullDescList = new ArrayList<AccountTransaction>(fullList);
    Collections.sort(fullDescList, new Comparator<AccountTransaction>()
    {

      @Override
      public int compare(AccountTransaction t1, AccountTransaction t2)
      {
        return t2.getDate().compareTo(t1.getDate());
      }

    });

    List<AccountTransaction> listExceptNow = new ArrayList<AccountTransaction>(2);
    listExceptNow.add(transactionOneDayAgo);
    listExceptNow.add(transactionOlderThanOneMonth);

    List<AccountTransaction> oneMonthList = new ArrayList<AccountTransaction>(2);
    oneMonthList.add(transactionNow1);
    oneMonthList.add(transactionOneDayAgo);
    return new Object[][] {
      { new TransactionSearchFilter(null, null), fullDescList },
      { new TransactionSearchFilter(null, null, SortOrder.ASC), fullList },
      { new TransactionSearchFilter(null, null, SortOrder.DESC, 1, 10), listExceptNow },
      { new TransactionSearchFilter(oneMonthAgo.getTime(), null), oneMonthList }
    };
  }
  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
