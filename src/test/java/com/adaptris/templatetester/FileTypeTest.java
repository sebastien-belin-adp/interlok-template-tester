package com.adaptris.templatetester;

import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileTypeTest {

  @Test
  public void testIsXml() {
    boolean isXml = FileType.isXml(Paths.get("file.xml"));
    Assertions.assertTrue(isXml);
  }

  @Test
  public void testIsXmlWithPath() {
    boolean isXml = FileType.isXml(Paths.get("path", "file.xml"));
    Assertions.assertTrue(isXml);
  }

  @Test
  public void testIsXmlFalse() {
    boolean isXml = FileType.isXml(Paths.get("file.html"));
    Assertions.assertFalse(isXml);
  }

}
