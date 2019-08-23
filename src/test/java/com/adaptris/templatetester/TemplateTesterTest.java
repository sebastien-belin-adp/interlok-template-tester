package com.adaptris.templatetester;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.adaptris.core.CoreException;

public class TemplateTesterTest {

  private URL interlokTemplatesDir = this.getClass().getClassLoader().getResource("interlok-templates");

  private TemplateValidator templateValidator;

  public TemplateTesterTest() {
    Map<String, Map<String, String>> valuesToModify = new HashMap<>();
    // We will add the text value GET to the method node
    valuesToModify.put("services/salesforce-standalone-requestor.xml",
        Collections.singletonMap("/service-list/services/standalone-requestor/producer/method-provider/method", "GET"));
    // We will add the text value GET to the method node
    valuesToModify.put("services/salesforce-standalone-requestor-with-body.xml",
        Collections.singletonMap("/service-list/services/standalone-requestor/producer/method-provider/method", "GET"));
    // Null means that we will remove the xslt-wizard node
    valuesToModify.put("channels/soap-web-service.xml", Collections.singletonMap("/channel/xslt-wizard", null));

    templateValidator = new TemplateValidator(Collections.emptyList(), valuesToModify);
  }

  @Test
  public void testTemplates() throws CoreException, IOException, URISyntaxException {
    Map<String, String> errors = new HashMap<>();

    Files.walk(Paths.get(interlokTemplatesDir.toURI())).filter(FileType::isXml).forEach(jar -> validateTemplate(jar, errors));

    assertNoError(errors);
  }

  private void validateTemplate(Path path, Map<String, String> errors) {
    templateValidator.validateTemplate(path, errors);
  }

  private void assertNoError(Map<String, String> errors) {
    if (!errors.isEmpty()) {
      String errorMessage = "There are " + errors.size() + " errors:" + System.lineSeparator()
      + errors.entrySet().stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
      Assertions.fail(errorMessage);
    }
  }

}
