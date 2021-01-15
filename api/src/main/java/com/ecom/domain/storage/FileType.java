package com.ecom.domain.storage;

public enum FileType {
  FULL("_full"),
  THUMB("_thumb");

  private final String type;

  FileType(String type) {
    this.type = type;
  }

  public String type() {
    return type;
  }

  public String extension() {
    return type + ".jpg";
  }
}
