package org.binyu.myfinance.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class DemoApplication
{
  private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

  public static void main(String[] args)
  {
    LOG.info("Starting with embedded tomcat.");
    SpringApplication.run(RootConfiguration.class, args);
  }
}
