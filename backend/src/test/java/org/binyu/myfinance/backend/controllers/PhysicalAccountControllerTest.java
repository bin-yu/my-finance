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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import org.binyu.myfinance.backend.AbstractIntegrationTest;
import org.binyu.myfinance.backend.daos.PhysicalAccountMapper;
import org.binyu.myfinance.backend.dtos.ExtAccountStore;
import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.binyu.myfinance.backend.utils.AccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
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
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  PhysicalAccountMapper physicalAccountMapper;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @Test(dataProvider = "testGetPhysicalAccountListData")
  public void testGetPhysicalAccountList(int accountNum) throws Exception
  {
    List<PhysicalAccount> allAccounts = AccountTestUtils.insertRandomPhysicalAccounts(jdbcTemplate, accountNum);
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
  public void testGetPhysicalAccountListWithAccountStores() throws Exception
  {
    PhysicalAccount targetPAccount = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "dummy", null);
    VirtualAccount targetVAccount = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "dummy", null, 10);
    long amount = 99;
    AccountTestUtils.updateAccountStore(jdbcTemplate, targetPAccount.getId(), targetVAccount.getId(), amount);
    targetPAccount.getMappedVirtualAccounts().add(
        ExtAccountStore.newVirtualInstance(targetVAccount.getId(), targetVAccount.getName(), amount));
    MvcResult result = this.mockMvc
        .perform(get("/physical_accounts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    List<PhysicalAccount> actList = deserialize(result, new TypeReference<List<PhysicalAccount>>()
    {
    });
    Assert.assertEquals(actList.size(), 1);
    Assert.assertEquals(actList.get(0), targetPAccount);
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
    AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "dummy", "dummy");

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
    PhysicalAccount actToUpdate = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "dummy", "dummy");

    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
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
    PhysicalAccount actOriginal = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "dummy", "dummy");

    PhysicalAccount actToUpdate = actOriginal.clone();
    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
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
    PhysicalAccount actOriginal = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "dummy", "dummy");

    PhysicalAccount actToUpdate = actOriginal.clone();
    // set a wrong id
    actToUpdate.setId(actOriginal.getId() - 10000);
    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
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
    PhysicalAccount actOriginal = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "dummy", "dummy");

    PhysicalAccount actToUpdate = actOriginal.clone();
    // set null name
    String newName = null;
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
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
  public void testDeletePhysicalAccountWithValidInput() throws Exception
  {
    // prepare one dummy acount for delete test
    PhysicalAccount actToDel = AccountTestUtils.insertPhysicalAccount(jdbcTemplate, "dummy", "dummy");

    this.mockMvc
        .perform(
            delete("/physical_accounts/" + actToDel.getId())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
    PhysicalAccount dbAct = physicalAccountMapper.getPhysicalAccountById(actToDel.getId());
    Assert.assertNull(dbAct);
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

  // ACCESSOR METHODS -----------------------------------------------

}
