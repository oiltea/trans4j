package io.github.oiltea.trans4j.jackson2;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.oiltea.trans4j.core.trans.Converter;

public class TransJacksonModule extends SimpleModule {

  private final Converter converter;

  /**
   * Constructors that should only be used for non-reusable convenience modules used by app code:
   * "real" modules should use actual name and version number information.
   */
  public TransJacksonModule(Converter converter) {
    this.converter = converter;
  }

  @Override
  public void setupModule(SetupContext context) {
    context.insertAnnotationIntrospector(new TransAnnotationIntrospector(converter));
    super.setupModule(context);
  }

}