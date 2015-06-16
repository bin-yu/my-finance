/**
 * 
 */
package org.binyu.myfinance.backend;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;

/**
 * @author Administrator
 *
 */
@SpringApplicationConfiguration(classes = { RootConfiguration.class })
@ActiveProfiles({ "web-integration" })
@WebIntegrationTest(randomPort = true)
public abstract class AbstractWebIntegrationTest extends AbstractTransactionalTestNGSpringContextTests
{

  protected static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";
  @Value("${local.server.port}")
  protected int port;
  protected static RestTemplate restTemplate;

  @BeforeClass
  public static void setup()
  {
    restTemplate = createRestTemplate();
  }

  protected String constructFullURL(String relativePath)
      throws MalformedURLException
  {
    return new URL("http", "localhost", port, relativePath).toString();
  }

  protected static RestTemplate createRestTemplate()
  {
    HttpClient httpClient = HttpClients.custom().build();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
        httpClient);
    RestTemplate template = new RestTemplate(requestFactory);
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>(
        1);
    messageConverters.add(new StringHttpMessageConverter());
    messageConverters.add(new MappingJackson2HttpMessageConverter());
    template.setMessageConverters(messageConverters);
    template.setErrorHandler(new ResponseErrorHandler()
    {

      @Override
      public boolean hasError(ClientHttpResponse response)
          throws IOException
      {
        // let the business method to handle the error code.
        return false;
      }

      @Override
      public void handleError(ClientHttpResponse response)
          throws IOException
      {
      }

    });
    return template;
  }
}
