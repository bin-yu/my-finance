/********************************************************************
 * File Name:    PhysicalAccountControllerTest.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.binyu.myfinance.backend.daos.PhysicalAccountMapper;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class PhysicalAccountControllerTest extends AbstractIntegrationTest
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  private SecureRandom random = new SecureRandom();
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  PhysicalAccountMapper physicalAccountMapper;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @Test(dataProvider = "testGetPhysicalAccountListData")
  public void testGetPhysicalAccountList(int accountNum) throws Exception
  {
    insertRandomPhysicalAccounts(accountNum);
    MvcResult result = this.mockMvc
        .perform(get("/physical_accounts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    List<PhysicalAccount> actList = deserialize(result, List.class);
    Assert.assertEquals(accountNum, actList.size());
  }

  @Test
  public void testAddPhysicalAccountWithValidInput() throws Exception
  {
    PhysicalAccount actToAdd = new PhysicalAccount("test", "desc");
    MvcResult result = this.mockMvc
        .perform(
            put("/physical_accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToAdd))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    PhysicalAccount finalAct = deserialize(result, PhysicalAccount.class);
    Assert.assertEquals(finalAct.getName(), actToAdd.getName());
    Assert.assertEquals(finalAct.getDescription(), actToAdd.getDescription());
    PhysicalAccount dbAct = physicalAccountMapper.getPhysicalAccountById(finalAct.getId());
    Assert.assertEquals(finalAct, dbAct);
  }

  @Test(dataProvider = "testAddPhysicalAccountWithInvalidInputData")
  public void testAddPhysicalAccountWithInvalidInput(String accountName) throws Exception
  {
    // prepare one dummy acount for dup test
    insertPhysicalAccount("dummy");

    PhysicalAccount actToAdd = new PhysicalAccount(accountName, "desc");
    this.mockMvc
        .perform(
            put("/physical_accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToAdd))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json;charset=UTF-8"));
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------
  @DataProvider(name = "testGetPhysicalAccountListData")
  private Object[][] testGetPhysicalAccountListData()
  {
    return new Object[][] {
      { 0 },
      { 1 }
    };
  }

  @DataProvider(name = "testAddPhysicalAccountWithInvalidInputData")
  private Object[][] testAddPhysicalAccountWithInvalidInputData()
  {
    return new Object[][] {
      // use duplicate name
      { "dummy" },
      // use empty name
      { null },
    };
  }

  private void insertRandomPhysicalAccounts(int accountNum) throws SQLException
  {
    for (int i = 0; i < accountNum; i++)
    {
      String name = "RandomAcct-" + random.nextInt();
      insertPhysicalAccount(name);
    }
  }

  private void insertPhysicalAccount(String name) throws SQLException
  {
    jdbcTemplate.update("insert into physical_accounts(name) values (?)", new Object[] { name }, new int[] { Types.CHAR });

  }

  // ACCESSOR METHODS -----------------------------------------------

}
