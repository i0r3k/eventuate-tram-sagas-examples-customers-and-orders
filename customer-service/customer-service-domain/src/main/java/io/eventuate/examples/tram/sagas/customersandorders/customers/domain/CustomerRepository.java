package io.eventuate.examples.tram.sagas.customersandorders.customers.domain;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
