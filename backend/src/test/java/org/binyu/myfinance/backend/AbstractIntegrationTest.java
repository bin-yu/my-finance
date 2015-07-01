/********************************************************************
 * File Name:    AbstractIntegrationTest.java
 *
 * Date Created: May 18, 2015
 *
 * ------------------------------------------------------------------
 * Copyright (C) 2010 Symantec Corporation. All Rights Reserved.
 *
 *******************************************************************/

// PACKAGE/IMPORTS --------------------------------------------------
package org.binyu.myfinance.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

/**
 * TODO: Update with a detailed description of the interface/class.
 *
 */
@SpringApplicationConfiguration(classes = RootConfiguration.class)
@ActiveProfiles({ "integration" })
@WebAppConfiguration
public abstract class AbstractIntegrationTest extends AbstractTransactionalTestNGSpringContextTests
{
  // CONSTANTS ------------------------------------------------------

  // CLASS VARIABLES ------------------------------------------------

  // INSTANCE VARIABLES ---------------------------------------------

  @Autowired
  private WebApplicationContext wac;
  @Autowired
  public PlatformTransactionManager txManager;
  protected MockMvc mockMvc;

  protected ObjectMapper mapper = new ObjectMapper();

  // CONSTRUCTORS ---------------------------------------------------

  // PUBLIC METHODS -------------------------------------------------
  @BeforeClass
  public void setup()
  {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  // PROTECTED METHODS ----------------------------------------------

  protected String serialize(Object obj) throws JsonProcessingException
  {
    return mapper.writeValueAsString(obj);
  }

  protected <T> T deserialize(MvcResult result, Class<T> cls) throws IOException, JsonParseException, JsonMappingException
  {
    return mapper.readValue(result.getResponse().getContentAsByteArray(), cls);
  }

  protected <T> List<T> deserialize(MvcResult result, TypeReference typeReference) throws JsonParseException,
      JsonMappingException, IOException
  {
    return mapper.readValue(result.getResponse().getContentAsByteArray(), typeReference);
  }

  // PRIVATE METHODS ------------------------------------------------

  protected void postMvcRequestInNestTransaction(String urlPath, Object requestBody) throws Exception, JsonProcessingException
  {
    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    // explicitly setting the transaction name is something that can only be done programmatically
    def.setName("SomeTxName");
    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
    TransactionStatus status = txManager.getTransaction(def);
    this.mockMvc
        .perform(
            post(urlPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(requestBody))
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    if (status.isRollbackOnly())
    {
      txManager.rollback(status);
    }
    else
    {
      txManager.commit(status);
    }
  }

  // ACCESSOR METHODS -----------------------------------------------

}
