package com.adaptris.templatetester;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

public class FileType {

  private FileType() {
  }

  private static final String XML_PATTERN = "glob:**.xml";

  public static boolean isXml(Path path, LinkOption... options) {
    PathMatcher matcher = path.getFileSystem().getPathMatcher(XML_PATTERN);
    return matcher.matches(path);
  }

}
