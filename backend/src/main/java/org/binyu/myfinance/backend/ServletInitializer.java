package org.binyu.myfinance.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.ErrorPageFilter;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

public class ServletInitializer extends SpringBootServletInitializer
{
  private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

  @Override
  protected SpringApplicationBuilder configure(
      SpringApplicationBuilder application)
  {
    return application.sources(RootConfiguration.class);
  }

  @Override
  protected WebApplicationContext run(SpringApplication
      application)
  {
    LOG.info("Starting with external tomcat.");
    // removing ErrorPageFilter, we don't need it
    application.getSources().remove(ErrorPageFilter.class);
    return super.run(application);
  }

}
