package com.todouno.product.service;

import com.todouno.product.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

  public Mono<Product> findById(String id);

  public Flux<Product> findAll();

  public Mono<Product> update(Product p, String id);

  public Mono<String> delete(String id);

  public Mono<Product> save(Product p);

}