package org.apache.camel.examples;

import java.util.Map;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servicenow.ServiceNowConstants;
import org.apache.camel.component.servicenow.ServiceNowParams;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.servicenow.dto.Incident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class CamelConfiguration extends RouteBuilder {

  private static final Logger log = LoggerFactory.getLogger(CamelConfiguration.class);
  
  @Override
  public void configure() throws Exception {
    
    restConfiguration("servlet")
      .bindingMode(RestBindingMode.auto)
      .contextPath("/camel")
      .apiContextPath("/api-doc")
    ;

    rest("/incidents")
      .post("/")
        .consumes(MediaType.APPLICATION_JSON_VALUE)
        .produces(MediaType.APPLICATION_JSON_VALUE)
        .to("direct:post")
    ;
    
    from("direct:post")
      .routeId("handlePostIncidents")
      .transform().groovy("return new org.apache.camel.servicenow.dto.Incident(shortDescription: request.body['description'], urgency: request.body['level'], assignmentGroup: '287ebd7da9fe198100f92cc8d1d2154e', impact: 2);")
      .setHeader(ServiceNowConstants.RESOURCE).constant(ServiceNowConstants.RESOURCE_TABLE)
      .setHeader(ServiceNowConstants.ACTION).constant(ServiceNowConstants.ACTION_CREATE)
      .setHeader(ServiceNowConstants.REQUEST_MODEL).constant(Incident.class)
      .setHeader(ServiceNowParams.PARAM_TABLE_NAME.getHeader()).constant("incident")
      .log(LoggingLevel.INFO, log, "Creating Incident:\\n${body.shortDescription}")
      .to("servicenow://{{camel.component.servicenow.instance-name}}?apiVersion=v1")
      .setBody().constant(null)
    ;
  }
}
