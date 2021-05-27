package com.todouno.product.dao;


import com.todouno.product.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * ProductRepository.
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

  public Mono<Product> findByName(String name);

}
