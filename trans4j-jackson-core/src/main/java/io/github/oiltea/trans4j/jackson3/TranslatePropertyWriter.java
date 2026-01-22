package io.github.oiltea.trans4j.jackson3;

import io.github.oiltea.trans4j.core.TranslateService;
import io.github.oiltea.trans4j.core.Translate;
import java.util.Objects;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.BeanPropertyWriter;

public class TranslatePropertyWriter extends BeanPropertyWriter {

  private final TranslateService translateService;
  private final BeanPropertyWriter fromWriter;
  private final Translate translate;

  protected TranslatePropertyWriter(
      TranslateService translateService,
      BeanPropertyWriter writer,
      BeanPropertyWriter fromWriter,
      Translate translate) {
    super(writer);
    this.translateService = translateService;
    this.fromWriter = fromWriter;
    this.translate = translate;
  }

  @Override
  public void serializeAsProperty(Object bean, JsonGenerator g, SerializationContext ctxt)
      throws Exception {
    Object fromValue = fromWriter.get(bean);
    String text = translateService.translate(translate.key(), Objects.toString(fromValue));
    g.writeStringProperty(getName(), text);
  }
}
