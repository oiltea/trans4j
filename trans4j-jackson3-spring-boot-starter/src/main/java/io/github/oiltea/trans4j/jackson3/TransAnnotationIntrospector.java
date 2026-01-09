package io.github.oiltea.trans4j.jackson3;

import io.github.oiltea.trans4j.core.annotation.Trans;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.Annotated;
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class TransAnnotationIntrospector extends JacksonAnnotationIntrospector {

  @Override
  public Object findSerializer(MapperConfig<?> config, Annotated a) {
    Trans trans = a.getAnnotation(Trans.class);
    if (trans != null) {
      return new TransSerializer(trans);
    }
    return super.findSerializer(config, a);
  }

}