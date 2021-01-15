package com.ecom.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpoPTDtoReq {

  @NotBlank private String token;

  public ExpoPTDtoReq() {}
}
