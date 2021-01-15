package com.ecom.web.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDto {

  @JsonProperty("value")
  private String id;

  @JsonProperty("label")
  @NotBlank
  private String name;

  @NotBlank private String icon;

  @NotBlank private String backgroundColor;

  @NotBlank private String color;

  public CategoryDto() {}
}
