package com.yuqi.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 * @author dipeak
 * @mail yuqi@dipeak.com
 * @description your description
 * @time 2023/4/12 22:22
 **/
public class TestReadFile {
  public static void main(String[] args) {
    try {
      InputStream inputStream = TestReadFile.class.getClassLoader().getResourceAsStream("t1/1.txt");
      List<String> lines = IOUtils.readLines(inputStream, Charset.forName("UTF-8"));
      lines.forEach(System.out::println);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
