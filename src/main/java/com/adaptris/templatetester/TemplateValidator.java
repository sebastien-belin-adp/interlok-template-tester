package com.adaptris.templatetester;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.adaptris.util.XmlUtils;

public class TemplateValidator {

  private List<String> ignores = new ArrayList<>();
  private Map<String, Map<String, String>> valuesToModify = new HashMap<>();

  private MarshallerHelper marshallerHelper = new MarshallerHelper();

  public TemplateValidator(List<String> ignores, Map<String, Map<String, String>> valuesToModify) {
    this.ignores.addAll(ignores);
    this.valuesToModify.putAll(valuesToModify);
  }

  /**
   * Validate the template at the given path. If there is any error they will be added to the errors map
   *
   * @param path
   * @param errors
   */
  public final void validateTemplate(Path path, Map<String, String> errors) {
    int nameCount = path.getNameCount();
    String subpath = path.subpath(nameCount - Math.min(nameCount, 2), nameCount).toString().replaceAll("\\\\", "/");
    if (ignores.contains(subpath)) {
      System.out.println("Ignoring: " + subpath);
    } else {
      System.out.println("Validating: " + subpath);

      try {
        Path xmlPath = modifyXml(path, subpath);
        String errorMessage = marshallerHelper.validateXml(xmlPath.toFile());

        if (Objects.nonNull(errorMessage)) {
          errors.put(subpath, errorMessage);
        }
      } catch (Exception expt) {
        errors.put(subpath, expt.getLocalizedMessage());
      }
    }
  }

  // Some templates need some modification in order to be unmarshalled
  private Path modifyXml(Path path, String subpath) throws Exception {
    if (valuesToModify.containsKey(subpath)) {
      XmlUtils xmlUtils = new XmlUtils();
      xmlUtils.setSource(Files.newInputStream(path));

      TemplateModifier templateModifier = new TemplateModifier(xmlUtils);
      templateModifier.modifyXml(valuesToModify.get(subpath));

      Path modifiedXmlPath = path.resolveSibling(path.getFileName() + "-modified");
      xmlUtils.writeDocument(Files.newOutputStream(modifiedXmlPath));
      return modifiedXmlPath;
    }
    return path;
  }

}
