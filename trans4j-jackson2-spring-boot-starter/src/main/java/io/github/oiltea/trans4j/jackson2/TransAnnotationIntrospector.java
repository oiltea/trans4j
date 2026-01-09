package io.github.oiltea.trans4j.jackson2;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.trans.Converter;

public class TransAnnotationIntrospector extends JacksonAnnotationIntrospector {

  private final Converter converter;

  public TransAnnotationIntrospector(Converter converter) {
    this.converter = converter;
  }

  @Override
  public Object findSerializer(Annotated a) {
    Trans trans = a.getAnnotation(Trans.class);
    if (trans != null) {
      return new TransSerializer(converter, trans);
    }
    return super.findSerializer(a);
  }

}