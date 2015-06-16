/**
 * 
 */
package org.binyu.myfinance.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 *
 */

@RestController
public class TestController
{
  private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

  @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
  public String sayHello(@PathVariable String name)
  {
    LOG.info("Called sayHello API with name=" + name);
    return "hello," + name;
  }
}
