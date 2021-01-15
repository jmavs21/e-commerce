package com.ecom.web.dto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Images implements MultipartFile {

  private String name;

  private String type;

  private String uri;

  public Images() {}

  @Override
  public String getOriginalFilename() {
    return null;
  }

  @Override
  public String getContentType() {
    return null;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public long getSize() {
    return 0;
  }

  @Override
  public byte[] getBytes() throws IOException {
    return null;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return null;
  }

  @Override
  public void transferTo(File dest) throws IOException, IllegalStateException {}
}
