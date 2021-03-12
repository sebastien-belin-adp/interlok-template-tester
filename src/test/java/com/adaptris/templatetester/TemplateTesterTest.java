package com.adaptris.templatetester;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

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
  public void testHasTemplates() throws CoreException, IOException, URISyntaxException {
    long count = Files.walk(Paths.get(interlokTemplatesDir.toURI())).count();

    assertTrue(count > 0);
  }

  /**
   * Run through all the templates in interlok-templates and validate them
   *
   * @throws CoreException
   * @throws IOException
   * @throws URISyntaxException
   */
  @TestFactory
  public Stream<DynamicTest> dynamicTestTemplates() throws IOException, URISyntaxException {
    return Files.walk(Paths.get(interlokTemplatesDir.toURI())).filter(FileType::isXml).map(path -> createDynamicTest(path));
  }

  private DynamicTest createDynamicTest(Path path) {
    int nameCount = path.getNameCount();
    String subpath = path.subpath(nameCount - Math.min(nameCount, 2), nameCount).toString().replaceAll("\\\\", "/");

    return DynamicTest.dynamicTest(subpath, () -> {
      Map<String, String> errors = new HashMap<>();

      validateTemplate(path, errors);

      assertNoError(errors);
    });

  }

  private void validateTemplate(Path path, Map<String, String> errors) {
    templateValidator.validateTemplate(path, errors);
  }

  private void assertNoError(Map<String, String> errors) {
    if (!errors.isEmpty()) {
      String errorMessage = "There are " + errors.size() + " errors:" + System.lineSeparator()
      + errors.entrySet().stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
      fail(errorMessage);
    }
  }

}
