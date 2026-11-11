package com.enterpriseflow.orders;

import com.enterpriseflow.customer.CustomerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.*;

@RestController @RequestMapping("/api/orders")
public class OrderController {
 private final CustomerOrderRepository orders; private final OrderItemRepository items; private final CustomerRepository customers;
 public OrderController(CustomerOrderRepository orders,OrderItemRepository items,CustomerRepository customers){
  this.orders=orders;this.items=items;this.customers=customers;
 }
 public record ItemRequest(@NotBlank String description,@Min(1) int quantity,@DecimalMin("0.00") BigDecimal unitPrice){}
 public record CreateOrderRequest(@NotNull Long customerId,@NotEmpty List<@Valid ItemRequest> items){}
 public record OrderResponse(Long id,String reference,String status,BigDecimal total,String customer){}
 @GetMapping public List<OrderResponse> list(){return orders.findAll().stream().map(this::dto).toList();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @Transactional
 public OrderResponse create(@Valid @RequestBody CreateOrderRequest r){
  var customer=customers.findById(r.customerId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found"));
  BigDecimal total=r.items().stream().map(i->i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
    .reduce(BigDecimal.ZERO,BigDecimal::add);
  var order=orders.save(new CustomerOrder(customer,"EF-"+UUID.randomUUID().toString().substring(0,8).toUpperCase(),total));
  r.items().forEach(i->items.save(new OrderItem(order,i.description(),i.quantity(),i.unitPrice())));
  return dto(order);
 }
 @PatchMapping("/{id}/status") @Transactional
 public OrderResponse changeStatus(@PathVariable Long id,@RequestParam String status){
  var order=orders.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
  String next=status.toUpperCase();
  Map<String,Set<String>> transitions=Map.of(
    "DRAFT",Set.of("CONFIRMED","CANCELLED"),
    "CONFIRMED",Set.of("FULFILLED","CANCELLED"),
    "FULFILLED",Set.of(), "CANCELLED",Set.of());
  if(!transitions.getOrDefault(order.getStatus(),Set.of()).contains(next))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Invalid order status transition");
  order.setStatus(next); return dto(order);
 }
 private OrderResponse dto(CustomerOrder o){return new OrderResponse(o.getId(),o.getReference(),o.getStatus(),o.getTotal(),o.getCustomer().getName());}
}
