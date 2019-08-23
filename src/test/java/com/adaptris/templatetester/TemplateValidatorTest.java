package com.adaptris.templatetester;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TemplateValidatorTest {

  @Test
  public void testValidateTemplate() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.emptyList(), Collections.emptyMap()).validateTemplate(getResourcePath("adapters/adapter.xml"),
        errors);

    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void testValidateTemplateInvalid() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.emptyList(), Collections.emptyMap()).validateTemplate(getResourcePath("adapters/invalid-adapter.xml"),
        errors);

    Assertions.assertFalse(errors.isEmpty());
    Assertions.assertEquals("com.thoughtworks.xstream.mapper.CannotResolveClassException: invalid-adapter",
        errors.get("adapters/invalid-adapter.xml"));
  }

  @Test
  public void testValidateTemplateInvalidIgnored() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.singletonList("adapters/invalid-adapter.xml"), Collections.emptyMap())
    .validateTemplate(getResourcePath("adapters/invalid-adapter.xml"), errors);

    Assertions.assertTrue(errors.isEmpty());
  }

  @Test
  public void testValidateTemplateWithValuesToModifyNotModified() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.emptyList(), Collections.emptyMap())
    .validateTemplate(getResourcePath("producers/standard-http-producer.xml"), errors);

    Assertions.assertFalse(errors.isEmpty());
    String error = errors.get("producers/standard-http-producer.xml");
    Assertions.assertTrue(error.contains("com.thoughtworks.xstream.converters.ConversionException:"));
    Assertions.assertTrue(error.contains("---- Debugging information ----"));
    Assertions.assertTrue(error.contains("path                : /producer/method-provider/method"));
  }

  @Test
  public void testValidateTemplateWithValuesToModify() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();
    Map<String, Map<String, String>> valuesToModify = Collections.singletonMap("producers/standard-http-producer.xml",
        Collections.singletonMap("/producer/method-provider/method", "GET"));

    new TemplateValidator(Collections.emptyList(), valuesToModify)
    .validateTemplate(getResourcePath("producers/standard-http-producer.xml"), errors);

    Assertions.assertTrue(errors.isEmpty());
  }

  private Path getResourcePath(String name) throws URISyntaxException {
    return Paths.get(this.getClass().getClassLoader().getResource(name).toURI());
  }

}
