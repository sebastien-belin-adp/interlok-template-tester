package com.adaptris.templatetester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TemplateValidatorTest {

  @Test
  public void testValidateTemplate() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.emptyList(), Collections.emptyMap()).validateTemplate(getResourcePath("adapters/adapter.xml"),
        errors);

    assertTrue(errors.isEmpty());
  }

  @Test
  public void testValidateTemplateInvalid() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.emptyList(), Collections.emptyMap()).validateTemplate(getResourcePath("adapters/invalid-adapter.xml"),
        errors);

    assertFalse(errors.isEmpty());
    assertEquals("invalid-adapter", errors.get("adapters/invalid-adapter.xml"));
  }

  @Test
  public void testValidateTemplateInvalidIgnored() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.singletonList("adapters/invalid-adapter.xml"), Collections.emptyMap())
    .validateTemplate(getResourcePath("adapters/invalid-adapter.xml"), errors);

    assertTrue(errors.isEmpty());
  }

  @Test
  public void testValidateTemplateWithValuesToModifyNotModified() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    new TemplateValidator(Collections.emptyList(), Collections.emptyMap())
    .validateTemplate(getResourcePath("producers/standard-http-producer.xml"), errors);

    assertFalse(errors.isEmpty());
    String error = errors.get("producers/standard-http-producer.xml");
    assertTrue(error.contains("---- Debugging information ----"));
    assertTrue(error.contains("path                : /producer/method-provider/method"));
  }

  @Test
  public void testValidateTemplateWithValuesToModify() throws URISyntaxException {
    Map<String, String> errors = new HashMap<>();
    Map<String, Map<String, String>> valuesToModify = Collections.singletonMap("producers/standard-http-producer.xml",
        Collections.singletonMap("/producer/method-provider/method", "GET"));

    new TemplateValidator(Collections.emptyList(), valuesToModify)
    .validateTemplate(getResourcePath("producers/standard-http-producer.xml"), errors);

    assertTrue(errors.isEmpty());
  }

  private Path getResourcePath(String name) throws URISyntaxException {
    return Paths.get(this.getClass().getClassLoader().getResource(name).toURI());
  }

}
