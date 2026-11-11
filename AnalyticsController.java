package com.enterpriseflow.analytics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import java.util.Map;
@RestController @RequestMapping("/api/analytics")
public class AnalyticsController {
 private final RestClient client;
 public AnalyticsController(@Value("${app.analytics.base-url}") String baseUrl){
  this.client=RestClient.builder().baseUrl(baseUrl).build();
 }
 @PostMapping("/forecast") public Map<?,?> forecast(@RequestBody Map<String,Object> payload){
  return client.post().uri("/forecast").body(payload).retrieve().body(Map.class);
 }
}
