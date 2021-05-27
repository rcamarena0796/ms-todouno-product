package com.todouno.product.service.impl;

import com.todouno.product.dao.ProductRepository;
import com.todouno.product.model.Product;
import com.todouno.product.service.ProductService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

  @Autowired
  private ProductRepository productRepository;

  @Override
  public Flux<Product> findAll() {
    return productRepository.findAll();
  }

  @Override
  public Mono<Product> findById(String id) {
    return productRepository.findById(id);
  }

  @Override
  public Mono<Product> save(Product product) {
    try {
      product.setCreationDate(new Date());
      return productRepository.save(product);

    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Mono<Product> update(Product product, String id) {
    try {
      return productRepository.findById(id)
          .flatMap(dbProduct -> {

            //name
            if (product.getName() != null) {
              dbProduct.setName(product.getName());
            }

            //stock
            dbProduct.setStock(product.getStock());

            //price
            dbProduct.setPrice(product.getPrice());

            return productRepository.save(dbProduct);

          }).switchIfEmpty(Mono.empty());
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Mono<String> delete(String id) {
    try {
      return productRepository.findById(id).map(dbProduct -> {
        productRepository.delete(dbProduct).subscribe();
        return dbProduct.getId();
      }).switchIfEmpty(Mono.empty());
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

}
