package com.todouno.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;

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

  @NotBlank(message = "'numDoc' is required")
  private int stock;

  @NotBlank(message = "'numDoc' is required")
  private float price;

  private Date creationDate;

}
