package com.todouno.product.controller;


import com.todouno.product.model.Product;
import com.todouno.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ProductController.
 */
@Api(tags = "Product API", value = "CRUD operations for products")
@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @ApiOperation(value = "Service used to return all products")
  @GetMapping("/findAll")
  public Flux<Product> findAll() {
    return productService.findAll();
  }

  /**
   * FIND A PRODUCT.
   */
  @ApiOperation(value = "Service used to find a product by id")
  @GetMapping("/find/{id}")
  public Mono<ResponseEntity<Product>> findById(@PathVariable("id") String id) {
    return productService.findById(id).map(c -> ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(c))
        .defaultIfEmpty(ResponseEntity
            .notFound()
            .build()
        );
  }

  /**
   * SAVE A PRODUCT.
   */
  @ApiOperation(value = "Service used to create products")
  @PostMapping("/save")
  public Mono<ResponseEntity<Product>> create(@Valid @RequestBody Product product) {
    return productService.save(product)
        .map(c -> ResponseEntity
            .created(URI.create("/products".concat(c.getId())))
            .contentType(MediaType.APPLICATION_JSON)
            .body(c));
  }

  /**
   * UPDATE A PRODUCT.
   */
  @ApiOperation(value = "Service used to update a product")
  @PutMapping("/update/{id}")
  public Mono<ResponseEntity<Product>> update(@PathVariable("id") String id,
      @Valid @RequestBody Product product) {
    return productService.update(product, id)
        .map(c -> ResponseEntity
            .created(URI.create("/products".concat(c.getId())))
            .contentType(MediaType.APPLICATION_JSON)
            .body(c))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * DELETE A PRODUCT.
   */
  @ApiOperation(value = "Service used to delete a product")
  @DeleteMapping("/delete/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return productService.delete(id).map(res -> ResponseEntity
        .ok()
        .<Void>build())
        .defaultIfEmpty(ResponseEntity
            .notFound()
            .build()
        );

  }
}