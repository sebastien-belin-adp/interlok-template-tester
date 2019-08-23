package com.adaptris.templatetester;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.adaptris.util.XmlUtils;

public class TemplateModifier {

  private XmlUtils xmlUtils;

  public TemplateModifier(XmlUtils xmlUtils) {
    this.xmlUtils = xmlUtils;
  }

  public void modifyXml(Map<String, String> xpathValues) {
    for (Entry<String, String> entry : xpathValues.entrySet()) {
      modifyValue(entry.getKey(), entry.getValue());
    }
  }

  private void modifyValue(String xpath, String value) {
    try {
      NodeList nodes = xmlUtils.getNodeList(xpath, xmlUtils.getCurrentDoc());
      if (Objects.nonNull(nodes)) {
        for (int i = 0; i < nodes.getLength(); i++) {
          Node node = nodes.item(i);
          if (isNode(node)) {
            if (Objects.nonNull(value)) {
              setTextContent(node, value);
            } else {
              removeContent(node);
            }
          }
        }
      }
    } catch (DOMException expt) {
      System.out.println("Could not set value [" + value + "] for xpath [" + xpath + "]");
      expt.printStackTrace();
    }
  }

  private void setTextContent(Node node, String value) {
    // Set the content of the node
    node.setTextContent(value);
  }

  private void removeContent(Node node) {
    Node parenNode = node.getParentNode();
    // remove node from its parent
    if (Objects.nonNull(parenNode)) {
      parenNode.removeChild(node);
    }
  }

  private boolean isNode(Node node) {
    return Objects.nonNull(node) && Objects.nonNull(node.getNodeName());
  }

}
