package com.todouno.product.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Product.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "PRODUCT")
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class Product {

  @Id
  private String id;

  private String name;

  @NotNull(message = "'stock' is required")
  private Integer stock;

  @NotNull(message = "'price' is required")
  private Float price;

  private Date creationDate;

}
