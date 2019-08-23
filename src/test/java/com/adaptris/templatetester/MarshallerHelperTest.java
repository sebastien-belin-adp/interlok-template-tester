package com.adaptris.templatetester;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MarshallerHelperTest {

  @Test
  public void testValidateXml() throws URISyntaxException {
    String error = new MarshallerHelper().validateXml(getResourceFile("adapters/adapter.xml"));

    Assertions.assertNull(error);
  }

  @Test
  public void testValidateXmlNullFile() throws URISyntaxException {
    String error = new MarshallerHelper().validateXml(null);

    Assertions.assertEquals("file may not be null", error);
  }

  @Test
  public void testValidateXmlInvalid() throws URISyntaxException {
    String error = new MarshallerHelper().validateXml(getResourceFile("adapters/invalid-adapter.xml"));

    Assertions.assertEquals("com.thoughtworks.xstream.mapper.CannotResolveClassException: invalid-adapter", error);
  }

  @Test
  public void testValidateXmlNullAdapter() throws URISyntaxException {
    String error = new MarshallerHelper().validateXml(getResourceFile("adapters/null-adapter.xml"));

    Assertions.assertEquals("Unmarshalled object is null", error);
  }

  private File getResourceFile(String name) throws URISyntaxException {
    return new File(this.getClass().getClassLoader().getResource(name).toURI());
  }

}
