package com.todouno.product.controller;


import static org.mockito.Mockito.when;

import com.todouno.product.dao.ProductRepository;
import com.todouno.product.model.Product;
import com.todouno.product.service.ProductService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductController.class)
@Import(ProductController.class)
public class ProductControllerTest {

  @Mock
  private List<Product> expectedProductList;

  @Mock
  private Product expectedProduct;

  @BeforeEach
  void setUp() {

    expectedProduct = Product.builder().id("1").name("test").stock(3).price(1.0).build();

    expectedProductList = Arrays.asList(
        Product.builder().id("1").name("test1").stock(3).price(1.0).build(),
        Product.builder().id("2").name("test2").stock(3).price(1.0).build(),
        Product.builder().id("3").name("test3").stock(3).price(1.0).build()
    );
  }

  @MockBean
  protected ProductService service;

  @MockBean
  ProductRepository repository;

  @Autowired
  private WebTestClient webClient;

  @Test
  void getAllProducts() {
    when(service.findAll()).thenReturn(Flux.fromIterable(expectedProductList));

    webClient.get().uri("/product/findAll")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Product.class)
        .isEqualTo(expectedProductList);
  }

  @Test
  void getProductById_whenProductExists_returnCorrectProduct() {
    when(service.findById(expectedProduct.getId()))
        .thenReturn(Mono.just(expectedProduct));

    webClient.get()
        .uri("/product/find/{numId}", expectedProduct.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Product.class)
        .isEqualTo(expectedProduct);
  }

  @Test
  void getById_whenNotExists_returnNotFound() {
    String id = "-1";
    when(service.findById(id))
        .thenReturn(Mono.empty());

    webClient.get()
        .uri("/product/find/{numId}", id)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void saveProduct() {
    when(service.save(expectedProduct)).thenReturn(Mono.just(expectedProduct));

    webClient.post()
        .uri("/product/save").body(Mono.just(expectedProduct), Product.class)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Product.class)
        .isEqualTo(expectedProduct);
  }


  @Test
  void saveFail() {
    when(service.save(expectedProduct)).thenReturn(Mono.error(new Exception()));

    webClient.post()
        .uri("/product/save").body(Mono.just(expectedProduct), Product.class)
        .exchange()
        .expectStatus()
        .isEqualTo(500);
  }

  @Test
  void updateProduct_whenProductExists_performUpdate() {
    when(service.update(expectedProduct, expectedProduct.getId()))
        .thenReturn(Mono.just(expectedProduct));

    webClient.put()
        .uri("/product/update/{id}", expectedProduct.getId())
        .body(Mono.just(expectedProduct), Product.class)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Product.class)
        .isEqualTo(expectedProduct);
  }

  @Test
  void updateProduct_whenProductNotExist_returnNotFound() {
    String id = "-1";
    when(service.update(expectedProduct, id)).thenReturn(Mono.empty());

    webClient.put()
        .uri("/product/update/{id}", id)
        .body(Mono.just(expectedProduct), Product.class)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void deleteProduct_whenProductExists_performDeletion() {
    when(service.delete(expectedProduct.getId()))
        .thenReturn(Mono.just(expectedProduct.getId()));

    webClient.delete()
        .uri("/product/delete/{id}", expectedProduct.getId())
        .exchange()
        .expectStatus()
        .isOk();
  }


  @Test
  void deleteProduct_whenProductNotExists_returnNotFound() {
    String id = "-1";
    when(service.delete(id)).thenReturn(Mono.empty());

    webClient.delete()
        .uri("/product/delete/{id}", id)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

}