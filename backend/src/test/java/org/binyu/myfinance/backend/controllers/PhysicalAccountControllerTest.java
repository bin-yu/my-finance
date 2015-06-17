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
package org.binyu.myfinance.backend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.binyu.myfinance.backend.AbstractIntegrationTest;
import org.binyu.myfinance.backend.daos.PhysicalAccountMapper;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test PhysicalAccountController
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
    List<PhysicalAccount> allAccounts = insertRandomPhysicalAccounts(accountNum);
    MvcResult result = this.mockMvc
        .perform(get("/physical_accounts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    List<PhysicalAccount> actList = deserialize(result, new TypeReference<List<PhysicalAccount>>()
    {
    });
    Assert.assertEquals(actList, allAccounts);
  }

  @Test
  public void testAddPhysicalAccountWithValidInput() throws Exception
  {
    PhysicalAccount actToAdd = new PhysicalAccount("test", "desc");
    MvcResult result = this.mockMvc
        .perform(
            post("/physical_accounts")
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
    insertPhysicalAccount("dummy", "dummy");

    PhysicalAccount actToAdd = new PhysicalAccount(accountName, "desc");
    this.mockMvc
        .perform(
            post("/physical_accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToAdd))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json;charset=UTF-8"));
  }

  @Test
  public void testUpdatePhysicalAccountWithValidInput() throws Exception
  {
    // prepare one dummy acount for update test
    PhysicalAccount actToUpdate = insertPhysicalAccount("dummy", "dummy");

    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
    actToUpdate.setAmount(newAmount);
    this.mockMvc
        .perform(
            put("/physical_accounts/" + actToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
    PhysicalAccount dbAct = physicalAccountMapper.getPhysicalAccountById(actToUpdate.getId());
    Assert.assertEquals(dbAct.getName(), newName);
    Assert.assertEquals(dbAct.getDescription(), newDescription);
    // amount should not change
    Assert.assertEquals(dbAct.getAmount(), 0);
  }

  @Test
  public void testUpdatePhysicalAccountWithPathIdWrong() throws Exception
  {
    // prepare one dummy acount for update test
    PhysicalAccount actOriginal = insertPhysicalAccount("dummy", "dummy");

    PhysicalAccount actToUpdate = actOriginal.clone();
    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
    actToUpdate.setAmount(newAmount);
    this.mockMvc
        .perform(
            put("/physical_accounts/" + (actOriginal.getId() - 10000))
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    // db remain unchanged
    PhysicalAccount dbAct = physicalAccountMapper.getPhysicalAccountById(actOriginal.getId());
    Assert.assertEquals(dbAct, actOriginal);
  }

  @Test
  public void testUpdatePhysicalAccountWithBodyIdWrong() throws Exception
  {
    // prepare one dummy acount for update test
    PhysicalAccount actOriginal = insertPhysicalAccount("dummy", "dummy");

    PhysicalAccount actToUpdate = actOriginal.clone();
    // set a wrong id
    actToUpdate.setId(actOriginal.getId() - 10000);
    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
    actToUpdate.setAmount(newAmount);
    this.mockMvc
        .perform(
            put("/physical_accounts/" + actOriginal.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    // db remain unchanged
    PhysicalAccount dbAct = physicalAccountMapper.getPhysicalAccountById(actOriginal.getId());
    Assert.assertEquals(dbAct, actOriginal);
  }

  @Test
  public void testUpdatePhysicalAccountWithNullName() throws Exception
  {
    // prepare one dummy acount for update test
    PhysicalAccount actOriginal = insertPhysicalAccount("dummy", "dummy");

    PhysicalAccount actToUpdate = actOriginal.clone();
    // set null name
    String newName = null;
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
    actToUpdate.setAmount(newAmount);
    this.mockMvc
        .perform(
            put("/physical_accounts/" + actOriginal.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    // db remain unchanged
    PhysicalAccount dbAct = physicalAccountMapper.getPhysicalAccountById(actOriginal.getId());
    Assert.assertEquals(dbAct, actOriginal);
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

  protected List<PhysicalAccount> insertRandomPhysicalAccounts(int accountNum) throws SQLException
  {
    List<PhysicalAccount> allAccounts = new ArrayList<PhysicalAccount>(accountNum);
    for (int i = 0; i < accountNum; i++)
    {
      String name = "RandomAcct-" + random.nextInt();
      String desc = "desc-" + random.nextInt();
      PhysicalAccount account = insertPhysicalAccount(name, desc);
      allAccounts.add(account);
    }
    Collections.sort(allAccounts, new Comparator<PhysicalAccount>()
    {

      @Override
      public int compare(PhysicalAccount act1, PhysicalAccount act2)
      {
        return Long.valueOf(act1.getId()).compareTo(Long.valueOf(act2.getId()));
      }

    });
    return allAccounts;
  }

  protected PhysicalAccount insertPhysicalAccount(String name, String desc) throws SQLException
  {
    jdbcTemplate.update("insert into physical_accounts(name,description) values (?,?)", new Object[] { name, desc },
        new int[] { Types.CHAR, Types.CHAR });
    PhysicalAccount account = jdbcTemplate.queryForObject("select * from physical_accounts where name = ?", new Object[] { name },
        new RowMapper<PhysicalAccount>()
        {

          @Override
          public PhysicalAccount mapRow(ResultSet rs, int rowNum) throws SQLException
          {
            PhysicalAccount account = new PhysicalAccount();
            account.setId(rs.getLong("id"));
            account.setName(rs.getString("name"));
            account.setDescription(rs.getString("description"));
            return account;
          }

        });
    return account;
  }

  // ACCESSOR METHODS -----------------------------------------------

}
