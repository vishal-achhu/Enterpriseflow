package com.enterpriseflow.dashboard;
import com.enterpriseflow.orders.CustomerOrderRepository;
import com.enterpriseflow.customer.CustomerRepository;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
 private final CustomerOrderRepository orders; private final CustomerRepository customers;
 public DashboardController(CustomerOrderRepository orders,CustomerRepository customers){this.orders=orders;this.customers=customers;}
 @GetMapping("/summary") public Map<String,Object> summary(){
  var all=orders.findAll();
  BigDecimal revenue=all.stream().filter(o->!o.getStatus().equals("CANCELLED"))
    .map(o->o.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add);
  long open=all.stream().filter(o->o.getStatus().equals("DRAFT")||o.getStatus().equals("CONFIRMED")).count();
  return Map.of("customerCount",customers.count(),"orderCount",orders.count(),"openOrders",open,"revenue",revenue);
 }
}
