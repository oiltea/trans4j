package io.github.oiltea.trans4j.jackson3;

import tools.jackson.databind.module.SimpleModule;

public class TransJacksonModule extends SimpleModule {

  @Override
  public void setupModule(SetupContext context) {
    context.insertAnnotationIntrospector(new TransAnnotationIntrospector());
    super.setupModule(context);
  }

}