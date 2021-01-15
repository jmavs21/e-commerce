package com.ecom.domain.categories;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(value = "categories")
@Data
public class Category {

  @Id private String id;

  @NotBlank private String name;

  @NotBlank private String icon;

  @NotBlank private String backgroundColor;

  @NotBlank private String color;

  public Category() {}
}
