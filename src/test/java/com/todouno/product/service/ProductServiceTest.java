package com.todouno.product.service;

import static org.mockito.Mockito.when;

import com.todouno.product.dao.ProductRepository;
import com.todouno.product.model.Product;
import com.todouno.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
class ProductServiceTest {

  @TestConfiguration
  static class ProductServiceTestContextConfiguration {

    @Bean
    public ProductService productService() {
      return new ProductServiceImpl();
    }
  }

  @Autowired
  private ProductService productService;

  @MockBean
  private ProductRepository productRepository;

  @Mock
  private Product product1;

  @Mock
  private Product product2;

  @Mock
  private Product product3;


  @BeforeEach
  void setUp() {

    product1 = Product.builder().id("1").name("test1").stock(3).price(1.0).build();

    product2 = Product.builder().id("2").name("test2").stock(3).price(1.0).build();

    product3 = Product.builder().id("3").name("test3").stock(3).price(1.0).build();

  }

  @Test
  public void findAll() {
    when(productRepository.findAll())
        .thenReturn(Flux.just(product1, product2, product3));

    Flux<Product> found = productService.findAll();

    assertResults(found, product1, product2, product3);
  }

  @Test
  public void whenValidId_thenProductShouldBeFound() {
    when(productRepository.findById(product1.getId()))
        .thenReturn(Mono.just(product1));

    Mono<Product> found = productService.findById(product1.getId());

    assertResults(found, product1);
  }

  @Test
  public void save() {
    when(productRepository.save(product1)).thenReturn(Mono.just(product1));

    Mono<Product> saved = productService.save(product1);

    assertResults(saved, product1);
  }

  @Test
  public void update() {
    when(productRepository.save(product1))
        .thenReturn(Mono.just(product1));
    when(productRepository.findById(product1.getId())).thenReturn(Mono.just(product1));

    Mono<Product> updated = productService.update(product1, product1.getId());
    assertResults(updated, product1);
  }


  @Test
  public void delete() {
    when(productRepository.findById(product1.getId()))
        .thenReturn(Mono.just(product1));
    when(productRepository.delete(product1))
        .thenReturn(Mono.empty());

    Mono<String> deleted = productService.delete(product1.getId());

    StepVerifier
        .create(deleted)
        .expectNext(product1.getId())
        .verifyComplete();
  }

  private void assertResults(Publisher<Product> publisher, Product... expectedClientTypes) {
    StepVerifier
        .create(publisher)
        .expectNext(expectedClientTypes)
        .verifyComplete();
  }

}