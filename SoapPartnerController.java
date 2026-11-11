package com.enterpriseflow.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * SOAP boundary/adapter endpoint. The demo safely reports configuration state.
 * Connect a real partner WSDL and generated JAXB contract classes before claiming
 * live SOAP interoperability. Keeping SOAP behind this adapter prevents SOAP types
 * leaking into the REST-facing domain model.
 */
@RestController @RequestMapping("/api/integrations/soap")
public class SoapPartnerController {
 private final String wsdlUrl;
 public SoapPartnerController(@Value("${app.soap.wsdl-url:}") String wsdlUrl){this.wsdlUrl=wsdlUrl;}
 @GetMapping("/partner-status/{reference}")
 public Map<String,String> status(@PathVariable String reference){
  if(wsdlUrl == null || wsdlUrl.isBlank())
   return Map.of("reference",reference,"integration","NOT_CONFIGURED",
     "message","Set SOAP_PARTNER_WSDL_URL and implement the partner contract adapter.");
  return Map.of("reference",reference,"integration","CONFIGURED",
     "message","WSDL configured; bind generated SOAP client operations for the partner contract.");
 }
}
