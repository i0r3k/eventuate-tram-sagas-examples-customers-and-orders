package io.eventuate.examples.tram.sagas.ordersandcustomers.orders.web;


import io.eventuate.examples.common.money.Money;

public record CreateOrderRequest(Long customerId, Money orderTotal) {

}
