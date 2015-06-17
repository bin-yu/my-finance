/********************************************************************
 * File Name:    VirtualAccountControllerTest.java
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

import java.util.List;

import org.binyu.myfinance.backend.AbstractIntegrationTest;
import org.binyu.myfinance.backend.daos.VirtualAccountMapper;
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
 * Test VirtualAccountController
 */
public class VirtualAccountControllerTest extends AbstractIntegrationTest
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------
  @Autowired
  JdbcTemplate jdbcTemplate;
  @Autowired
  VirtualAccountMapper VirtualAccountMapper;

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @Test(dataProvider = "testGetVirtualAccountListData")
  public void testGetVirtualAccountList(int accountNum) throws Exception
  {
    List<VirtualAccount> allAccounts = AccountTestUtils.insertRandomVirtualAccounts(jdbcTemplate, accountNum);
    MvcResult result = this.mockMvc
        .perform(get("/virtual_accounts").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    List<VirtualAccount> actList = deserialize(result, new TypeReference<List<VirtualAccount>>()
    {
    });
    Assert.assertEquals(actList, allAccounts);
  }

  @Test
  public void testAddVirtualAccountWithValidInput() throws Exception
  {
    VirtualAccount actToAdd = new VirtualAccount("test", "desc");
    MvcResult result = this.mockMvc
        .perform(
            post("/virtual_accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToAdd))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn();
    VirtualAccount finalAct = deserialize(result, VirtualAccount.class);
    Assert.assertEquals(finalAct.getName(), actToAdd.getName());
    Assert.assertEquals(finalAct.getDescription(), actToAdd.getDescription());
    VirtualAccount dbAct = VirtualAccountMapper.getVirtualAccountById(finalAct.getId());
    Assert.assertEquals(finalAct, dbAct);
  }

  @Test(dataProvider = "testAddVirtualAccountWithInvalidInputData")
  public void testAddVirtualAccountWithInvalidInput(String accountName) throws Exception
  {
    // prepare one dummy acount for dup test
    AccountTestUtils.insertVirtualAccount(jdbcTemplate, "dummy", "dummy");

    VirtualAccount actToAdd = new VirtualAccount(accountName, "desc");
    this.mockMvc
        .perform(
            post("/virtual_accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToAdd))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json;charset=UTF-8"));
  }

  @Test
  public void testUpdateVirtualAccountWithValidInput() throws Exception
  {
    // prepare one dummy acount for update test
    VirtualAccount actToUpdate = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "dummy", "dummy");
    ;

    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
    actToUpdate.setAmount(newAmount);
    this.mockMvc
        .perform(
            put("/virtual_accounts/" + actToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
    VirtualAccount dbAct = VirtualAccountMapper.getVirtualAccountById(actToUpdate.getId());
    Assert.assertEquals(dbAct.getName(), newName);
    Assert.assertEquals(dbAct.getDescription(), newDescription);
    // amount should not change
    Assert.assertEquals(dbAct.getAmount(), 0);
  }

  @Test
  public void testUpdateVirtualAccountWithPathIdWrong() throws Exception
  {
    // prepare one dummy acount for update test
    VirtualAccount actOriginal = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "dummy", "dummy");
    ;

    VirtualAccount actToUpdate = actOriginal.clone();
    String newName = "changedName";
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
    actToUpdate.setAmount(newAmount);
    this.mockMvc
        .perform(
            put("/virtual_accounts/" + (actOriginal.getId() - 10000))
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    // db remain unchanged
    VirtualAccount dbAct = VirtualAccountMapper.getVirtualAccountById(actOriginal.getId());
    Assert.assertEquals(dbAct, actOriginal);
  }

  @Test
  public void testUpdateVirtualAccountWithBodyIdWrong() throws Exception
  {
    // prepare one dummy acount for update test
    VirtualAccount actOriginal = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "dummy", "dummy");
    ;

    VirtualAccount actToUpdate = actOriginal.clone();
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
            put("/virtual_accounts/" + actOriginal.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    // db remain unchanged
    VirtualAccount dbAct = VirtualAccountMapper.getVirtualAccountById(actOriginal.getId());
    Assert.assertEquals(dbAct, actOriginal);
  }

  @Test
  public void testUpdateVirtualAccountWithNullName() throws Exception
  {
    // prepare one dummy acount for update test
    VirtualAccount actOriginal = AccountTestUtils.insertVirtualAccount(jdbcTemplate, "dummy", "dummy");

    VirtualAccount actToUpdate = actOriginal.clone();
    // set null name
    String newName = null;
    actToUpdate.setName(newName);
    String newDescription = "changedDesc";
    actToUpdate.setDescription(newDescription);
    long newAmount = 10000;
    actToUpdate.setAmount(newAmount);
    this.mockMvc
        .perform(
            put("/virtual_accounts/" + actOriginal.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(actToUpdate))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    // db remain unchanged
    VirtualAccount dbAct = VirtualAccountMapper.getVirtualAccountById(actOriginal.getId());
    Assert.assertEquals(dbAct, actOriginal);
  }

  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------
  @DataProvider(name = "testGetVirtualAccountListData")
  private Object[][] testGetVirtualAccountListData()
  {
    return new Object[][] {
      { 0 },
      { 1 }
    };
  }

  @DataProvider(name = "testAddVirtualAccountWithInvalidInputData")
  private Object[][] testAddVirtualAccountWithInvalidInputData()
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
