package com.adaptris.templatetester;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class FileTypeTest {

  @Test
  public void testIsXml() {
    boolean isXml = FileType.isXml(Paths.get("file.xml"));
    assertTrue(isXml);
  }

  @Test
  public void testIsXmlWithPath() {
    boolean isXml = FileType.isXml(Paths.get("path", "file.xml"));
    assertTrue(isXml);
  }

  @Test
  public void testIsXmlFalse() {
    boolean isXml = FileType.isXml(Paths.get("file.html"));
    assertFalse(isXml);
  }

}
