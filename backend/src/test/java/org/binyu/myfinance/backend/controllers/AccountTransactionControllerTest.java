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
import org.binyu.myfinance.backend.RootConfiguration;
import org.binyu.myfinance.backend.daos.AccountAuditMapper;
import org.binyu.myfinance.backend.daos.AccountStoreMapper;
import org.binyu.myfinance.backend.dtos.AccountStore;
import org.binyu.myfinance.backend.dtos.AccountTransaction;
import org.binyu.myfinance.backend.dtos.AccountTransaction.TransactionType;
import org.binyu.myfinance.backend.dtos.ExtAccountTransactionRecord;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter;
import org.binyu.myfinance.backend.dtos.TransactionSearchFilter.SortOrder;
import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.binyu.myfinance.backend.utils.AccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TODO: Update with a detailed description of the interface/class.
 * 
 */
@SpringApplicationConfiguration(classes = { RootConfiguration.class, AccountTransactionControllerTest.class })
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
  public void prepareBuiltinAccounts() throws SQLException
  {
    fromPAccount = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "from", "dummy");
    toPAccount = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "to", "dummy");
    fromVAccount = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "from", "dummy", 0);
    toVAccount = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "to", "dummy", 0);

  }

  @AfterClass
  public void cleanBuiltinAccounts()
  {
    AccountTestUtils.deletePhysicalAccount(jdbcTemplate, fromPAccount);
    AccountTestUtils.deletePhysicalAccount(jdbcTemplate, toPAccount);
    AccountTestUtils.deleteVirtualAccount(jdbcTemplate, fromVAccount);
    AccountTestUtils.deleteVirtualAccount(jdbcTemplate, toVAccount);
  }

  @Test(dataProvider = "testNewTransactionWithValidInputData")
  public void testNewTransactionWithValidInput(AccountTransaction transactionToDo) throws Exception
  {
    // prepare the accounts related
    long fromAmount = 1000;
    AccountTestUtils.initAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId(), fromAmount);
    AccountStore dbStore = AccountTestUtils.getAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount);
    // prepare other data
    int amountToTransfer = 100;

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
    if (transactionToDo.getFromPhysicalAccountId() != PhysicalAccount.NO_ACCOUNT)
    {
      dbStore = actStoreMapper.getStore(fromPAccount.getId(), fromVAccount.getId());
      Assert.assertEquals(dbStore.getAmount(), fromAmount - amountToTransfer);
    }
    if (transactionToDo.getToPhysicalAccountId() != PhysicalAccount.NO_ACCOUNT)
    {
      dbStore = actStoreMapper.getStore(toPAccount.getId(), toVAccount.getId());
      Assert.assertEquals(dbStore.getAmount(), amountToTransfer);
    }
    // verify the audit record in db
    AccountTransaction rtTrans = deserialize(result, AccountTransaction.class);
    AccountTransaction dbTrans = auditRepo.getRecord(rtTrans.getId());
    transactionToDo.setId(rtTrans.getId());
    Assert.assertEquals(rtTrans, transactionToDo);
    Assert.assertEquals(dbTrans, transactionToDo);
  }

  @Test(dataProvider = "testNewTransactionWithInvalidInputData")
  public void testNewTransactionWithInvalidInput(AccountTransaction transactionToDo) throws Exception
  {
    // prepare the accounts related
    long fromAmount = 1000;
    AccountTestUtils.initAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId(), fromAmount);
    AccountStore dbStore = AccountTestUtils.getAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount);

    postMvcRequestInNestTransaction("/account_transactions", transactionToDo);
    // verify the transaction does not happen in db
    dbStore = AccountTestUtils.getAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount);
    dbStore = AccountTestUtils.getAccountStore(jdbcTemplate, toPAccount.getId(), toVAccount.getId());
    Assert.assertNull(dbStore);
  }

  @Test
  public void testBatchApplyTransactionsWithValidInput() throws Exception
  {
    // prepare the accounts related
    long fromAmount = 1000;
    AccountTestUtils.initAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId(), fromAmount);
    AccountStore dbStore = actStoreMapper.getStore(fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount);
    // prepare other data
    int amountToTransfer1 = 100, amountToTransfer2 = 99;
    AccountTransaction[] transactionsToDo = new AccountTransaction[2];
    transactionsToDo[0] = new AccountTransaction(0, new Date(), TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , amountToTransfer1, "desc");
    transactionsToDo[1] = new AccountTransaction(0, new Date(), TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , amountToTransfer2, "desc");

    MvcResult result = this.mockMvc
        .perform(
            post("/account_transactions/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(transactionsToDo))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    // verify the transaction happens in db
    dbStore = actStoreMapper.getStore(fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount - amountToTransfer1 - amountToTransfer2);
    dbStore = actStoreMapper.getStore(toPAccount.getId(), toVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), amountToTransfer1 + amountToTransfer2);
    // verify the audit record in db
    AccountTransaction[] rtTrans = deserialize(result, AccountTransaction[].class);
    Assert.assertEquals(rtTrans.length, 2);
    for (int i = 0; i < transactionsToDo.length; i++)
    {
      transactionsToDo[i].setId(rtTrans[i].getId());
      Assert.assertEquals(rtTrans[i], transactionsToDo[i]);
      AccountTransaction dbTrans = auditRepo.getRecord(rtTrans[i].getId());
      Assert.assertEquals(dbTrans, transactionsToDo[i]);
    }
  }

  @Test
  public void testBatchApplyTransactionsWithOneInValidTrans() throws Exception
  {
    // prepare the accounts related
    long fromAmount = 1000;
    AccountTestUtils.initAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId(), fromAmount);
    AccountStore dbStore = AccountTestUtils.getAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount);
    // prepare other data
    int amountToTransfer1 = 100, amountToTransfer2 = 99;
    AccountTransaction[] transactionsToDo = new AccountTransaction[2];
    transactionsToDo[0] = new AccountTransaction(0, new Date(), TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , amountToTransfer1, "desc");
    // the second one is an invalid transaction
    transactionsToDo[1] = new AccountTransaction(0, new Date(), TransactionType.TRANSFER
        , -2, fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , amountToTransfer2, "desc");

    postMvcRequestInNestTransaction("/account_transactions/batch", transactionsToDo);
    // verify the transaction does not happen in db
    dbStore = AccountTestUtils.getAccountStore(jdbcTemplate, fromPAccount.getId(), fromVAccount.getId());
    Assert.assertEquals(dbStore.getAmount(), fromAmount);
    dbStore = AccountTestUtils.getAccountStore(jdbcTemplate, toPAccount.getId(), toVAccount.getId());
    Assert.assertNull(dbStore);
  }

  @Test(dataProvider = "testSearchTransactionsData")
  public void testSearchTransactions(TransactionSearchFilter filter, List<ExtAccountTransactionRecord> expTransList)
      throws Exception
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
    List<ExtAccountTransactionRecord> retTransList = deserialize(result, new TypeReference<List<ExtAccountTransactionRecord>>()
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
    ExtAccountTransactionRecord transactionNow1 = new ExtAccountTransactionRecord(0, now.getTime(), TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , 100, "now"
        , fromPAccount.getName(), fromVAccount.getName()
        , toPAccount.getName(), toVAccount.getName());
    auditRepo.addRecord(transactionNow1);

    ExtAccountTransactionRecord transactionOneDayAgo = new ExtAccountTransactionRecord(0, oneDayAgo.getTime(),
        TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , 100, "oneDayAgo"
        , fromPAccount.getName(), fromVAccount.getName()
        , toPAccount.getName(), toVAccount.getName());
    auditRepo.addRecord(transactionOneDayAgo);

    ExtAccountTransactionRecord transactionOlderThanOneMonth = new ExtAccountTransactionRecord(0, olderThanOneMonth.getTime(),
        TransactionType.TRANSFER
        , fromPAccount.getId(), fromVAccount.getId()
        , toPAccount.getId(), toVAccount.getId()
        , 200, "olderThanOneMonth"
        , fromPAccount.getName(), fromVAccount.getName()
        , toPAccount.getName(), toVAccount.getName());
    auditRepo.addRecord(transactionOlderThanOneMonth);

    List<ExtAccountTransactionRecord> fullList = new ArrayList<ExtAccountTransactionRecord>(3);
    fullList.add(transactionOlderThanOneMonth);
    fullList.add(transactionOneDayAgo);
    fullList.add(transactionNow1);

    List<ExtAccountTransactionRecord> fullDescList = new ArrayList<ExtAccountTransactionRecord>(fullList);
    Collections.sort(fullDescList, new Comparator<ExtAccountTransactionRecord>()
    {

      @Override
      public int compare(ExtAccountTransactionRecord t1, ExtAccountTransactionRecord t2)
      {
        return t2.getDate().compareTo(t1.getDate());
      }

    });

    List<ExtAccountTransactionRecord> listExceptNow = new ArrayList<ExtAccountTransactionRecord>(2);
    listExceptNow.add(transactionOneDayAgo);
    listExceptNow.add(transactionOlderThanOneMonth);

    List<ExtAccountTransactionRecord> oneMonthList = new ArrayList<ExtAccountTransactionRecord>(2);
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
  @DataProvider(name = "testNewTransactionWithValidInputData")
  private Object[][] testNewTransactionWithValidInputData()
  {
    return new Object[][] {
      // transfer money
      { new AccountTransaction(0, new Date(), TransactionType.TRANSFER
          , fromPAccount.getId(), fromVAccount.getId()
          , toPAccount.getId(), toVAccount.getId()
          , 100, "desc") },
      // income
      { new AccountTransaction(0, new Date(), TransactionType.INCOME
          , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
          , toPAccount.getId(), toVAccount.getId()
          , 100, "desc") },
      // expense
      { new AccountTransaction(0, new Date(), TransactionType.EXPENSE
          , fromPAccount.getId(), fromVAccount.getId()
          , PhysicalAccount.NO_ACCOUNT, VirtualAccount.NO_ACCOUNT
          , 100, "desc") },
    };
  }

  @DataProvider(name = "testNewTransactionWithInvalidInputData")
  private Object[][] testNewTransactionWithInvalidInputData()
  {
    return new Object[][] {
      // invalid from physical account id
      { new AccountTransaction(0, new Date(), TransactionType.TRANSFER
          , -2, fromVAccount.getId()
          , toPAccount.getId(), toVAccount.getId()
          , 100, "desc") },
      // invalid to physical account id
      { new AccountTransaction(0, new Date(), TransactionType.TRANSFER
          , fromPAccount.getId(), fromVAccount.getId()
          , -2, toVAccount.getId()
          , 100, "desc") },
      // invalid from virtual account id
      { new AccountTransaction(0, new Date(), TransactionType.TRANSFER
          , fromPAccount.getId(), -2
          , toPAccount.getId(), toVAccount.getId()
          , 100, "desc") },
      // invalid to virtual account id
      { new AccountTransaction(0, new Date(), TransactionType.TRANSFER
          , fromPAccount.getId(), fromVAccount.getId()
          , toPAccount.getId(), -2
          , 100, "desc") },
      // negative amount to transfer
      { new AccountTransaction(0, new Date(), TransactionType.TRANSFER
          , fromPAccount.getId(), fromVAccount.getId()
          , toPAccount.getId(), toVAccount.getId()
          , -100, "desc") }
    };
  }
  // ACCESSOR METHODS -----------------------------------------------

}
