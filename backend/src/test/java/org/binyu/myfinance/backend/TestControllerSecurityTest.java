/********************************************************************
 * File Name:    TestControllerSecurityTest.java
 *
 * Date Created: Jun 15, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
public class TestControllerSecurityTest extends AbstractWebIntegrationTest
{

  /**
   * 
   */
  public TestControllerSecurityTest()
  {
    // TODO Auto-generated constructor stub
  }

  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @Test
  public void testSayHello() throws Exception
  {
    String name = "Marry";
    ResponseEntity<String> resp = restTemplate.exchange(
        constructFullURL("/hello/" + name), HttpMethod.GET, null,
        String.class);
    assertEquals(resp.getStatusCode(), HttpStatus.OK);
  }
  // PROTECTED METHODS ----------------------------------------------

  // PRIVATE METHODS ------------------------------------------------

  // ACCESSOR METHODS -----------------------------------------------

}
