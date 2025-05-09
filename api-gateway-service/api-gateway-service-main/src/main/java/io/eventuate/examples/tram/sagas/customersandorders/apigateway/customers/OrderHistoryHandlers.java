package io.eventuate.examples.tram.sagas.customersandorders.apigateway.customers;

import io.eventuate.examples.tram.sagas.customersandorders.apigateway.proxies.customerservice.CustomerServiceProxy;
import io.eventuate.examples.tram.sagas.customersandorders.apigateway.proxies.customerservice.GetCustomerResponse;
import io.eventuate.examples.tram.sagas.customersandorders.apigateway.proxies.orderservice.GetOrderResponse;
import io.eventuate.examples.tram.sagas.customersandorders.apigateway.proxies.orderservice.OrderServiceProxy;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public class OrderHistoryHandlers {

  private final OrderServiceProxy orderService;
  private final CustomerServiceProxy customerService;

  public OrderHistoryHandlers(OrderServiceProxy orderService, CustomerServiceProxy customerService) {
    this.orderService = orderService;
    this.customerService = customerService;
  }

  public Mono<ServerResponse> getOrderHistory(ServerRequest serverRequest) {
    String customerId = serverRequest.pathVariable("customerId");

    Mono<Optional<GetCustomerResponse>> customer = customerService.findCustomerById(customerId);

    Mono<List<GetOrderResponse>> orders = orderService.findOrdersByCustomerId(customerId);

    Mono<Optional<GetCustomerHistoryResponse>> map = Mono
            .zip(customer, orders)
            .map(possibleCustomerAndOrders ->
                    possibleCustomerAndOrders.getT1().map(c -> {
                      List<GetOrderResponse> os = possibleCustomerAndOrders.getT2();
                      return new GetCustomerHistoryResponse(c.customerId(), c.name(), c.creditLimit(), os);
                    }));
    return map.flatMap(maybe ->
            maybe.map(c ->
                    ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(fromValue(c)))
                    .orElseGet(() -> ServerResponse.notFound().build()));
  }
}
