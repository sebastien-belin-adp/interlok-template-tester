package com.adaptris.templatetester;

import java.io.File;
import java.util.Objects;

import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.DefaultMarshaller;
import com.adaptris.core.util.Args;

public class MarshallerHelper {

  private final AdaptrisMarshaller defaultMarshaller = DefaultMarshaller.getDefaultMarshaller();

  public final String validateXml(File file) {
    String errorMessage = null;
    try {
      Args.notNull(file, "file");
      Object obj = defaultMarshaller.unmarshal(file);
      if (Objects.isNull(obj)) {
        errorMessage = "Unmarshalled object is null";
      }
    } catch (Exception expt) {
      errorMessage = expt.getLocalizedMessage();
    }
    return errorMessage;
  }

}
