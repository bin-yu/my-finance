/********************************************************************
 * File Name:    AccountTestUtils.java
 *
 * Date Created: Jun 16, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend.utils;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.binyu.myfinance.backend.dtos.PhysicalAccount;
import org.binyu.myfinance.backend.dtos.VirtualAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.testng.Assert;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class AccountTestUtils
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  private static SecureRandom random = new SecureRandom();

  // INSTANCE VARIABLES ---------------------------------------------

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  public static List<PhysicalAccount> insertRandomPhysicalAccounts(JdbcTemplate jdbcTemplate, int accountNum) throws SQLException
  {
    List<PhysicalAccount> allAccounts = new ArrayList<PhysicalAccount>(accountNum);
    for (int i = 0; i < accountNum; i++)
    {
      String name = "RandomAcct-" + random.nextInt();
      String desc = "desc-" + random.nextInt();
      PhysicalAccount account = insertPhysicalAccount(jdbcTemplate, name, desc);
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

  public static PhysicalAccount insertPhysicalAccount(JdbcTemplate jdbcTemplate, String name, String desc) throws SQLException
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

  public static List<VirtualAccount> insertRandomVirtualAccounts(JdbcTemplate jdbcTemplate, int accountNum) throws SQLException
  {
    List<VirtualAccount> allAccounts = new ArrayList<VirtualAccount>(accountNum);
    for (int i = 0; i < accountNum; i++)
    {
      String name = "RandomAcct-" + random.nextInt();
      String desc = "desc-" + random.nextInt();
      VirtualAccount account = insertVirtualAccount(jdbcTemplate, name, desc);
      allAccounts.add(account);
    }
    Collections.sort(allAccounts, new Comparator<VirtualAccount>()
    {

      @Override
      public int compare(VirtualAccount act1, VirtualAccount act2)
      {
        return Long.valueOf(act1.getId()).compareTo(Long.valueOf(act2.getId()));
      }

    });
    return allAccounts;
  }

  public static VirtualAccount insertVirtualAccount(JdbcTemplate jdbcTemplate, String name, String desc) throws SQLException
  {
    jdbcTemplate.update("insert into virtual_accounts(name,description) values (?,?)", new Object[] { name, desc },
        new int[] { Types.CHAR, Types.CHAR });
    VirtualAccount account = jdbcTemplate.queryForObject("select * from virtual_accounts where name = ?", new Object[] { name },
        new RowMapper<VirtualAccount>()
        {

          @Override
          public VirtualAccount mapRow(ResultSet rs, int rowNum) throws SQLException
          {
            VirtualAccount account = new VirtualAccount();
            account.setId(rs.getLong("id"));
            account.setName(rs.getString("name"));
            account.setDescription(rs.getString("description"));
            return account;
          }

        });
    return account;
  }

  // PROTECTED METHODS ----------------------------------------------

  public static void initAccountStore(JdbcTemplate jdbcTemplate, long physicalAccountId, long virtualAccountId, long amount)
      throws SQLException
  {
    int updateCnt = jdbcTemplate.update("insert into account_stores(physical_account_id,virtual_account_id,amount) values (?,?,?)",
        new Object[] {
          physicalAccountId, virtualAccountId, amount },
        new int[] { Types.BIGINT, Types.BIGINT, Types.BIGINT });
    Assert.assertEquals(updateCnt, 1);
  }

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
