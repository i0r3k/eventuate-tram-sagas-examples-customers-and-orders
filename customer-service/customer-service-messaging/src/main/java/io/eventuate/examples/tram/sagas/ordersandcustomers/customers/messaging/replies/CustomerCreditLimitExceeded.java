package io.eventuate.examples.tram.sagas.ordersandcustomers.customers.messaging.replies;

import io.eventuate.tram.commands.consumer.annotations.FailureReply;

@FailureReply
public class CustomerCreditLimitExceeded implements ReserveCreditResult {
}
