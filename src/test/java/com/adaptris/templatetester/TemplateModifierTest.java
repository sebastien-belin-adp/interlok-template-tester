package com.adaptris.templatetester;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.adaptris.util.XmlUtils;

public class TemplateModifierTest {

  @Test
  public void testModifyXml() throws IOException, URISyntaxException {
    XmlUtils xmlUtils = new XmlUtils();
    xmlUtils.setSource(Files.newInputStream(getResourcePath("producers/standard-http-producer.xml")));

    String method = xmlUtils.getSingleTextItem("/producer/method-provider/method");
    Assertions.assertEquals("", method);

    TemplateModifier templateModifier = new TemplateModifier(xmlUtils);
    templateModifier.modifyXml(Collections.singletonMap("/producer/method-provider/method", "GET"));

    method = xmlUtils.getSingleTextItem("/producer/method-provider/method");
    Assertions.assertEquals("GET", method);
  }

  @Test
  public void testModifyXmlRemoveContent() throws IOException, URISyntaxException {
    XmlUtils xmlUtils = new XmlUtils();
    xmlUtils.setSource(Files.newInputStream(getResourcePath("adapters/adapter.xml")));

    String method = xmlUtils.getSingleTextItem("/adapter/unique-id");
    Assertions.assertEquals("adapter", method);

    TemplateModifier templateModifier = new TemplateModifier(xmlUtils);
    templateModifier.modifyXml(Collections.singletonMap("/adapter/unique-id", null));

    method = xmlUtils.getSingleTextItem("/adapter/unique-id");
    Assertions.assertEquals("", method);
  }

  @Test
  public void testModifyXmlXpathDoesntExist() throws IOException, URISyntaxException {
    XmlUtils xmlUtils = new XmlUtils();
    xmlUtils.setSource(Files.newInputStream(getResourcePath("producers/standard-http-producer.xml")));

    String method = xmlUtils.getSingleTextItem("/xpath/doesnt/exist");
    Assertions.assertEquals("", method);

    TemplateModifier templateModifier = new TemplateModifier(xmlUtils);
    templateModifier.modifyXml(Collections.singletonMap("/xpath/doesnt/exist", "GET"));

    method = xmlUtils.getSingleTextItem("/xpath/doesnt/exist");
    Assertions.assertEquals("", method);
  }

  private Path getResourcePath(String name) throws URISyntaxException {
    return Paths.get(this.getClass().getClassLoader().getResource(name).toURI());
  }

}
