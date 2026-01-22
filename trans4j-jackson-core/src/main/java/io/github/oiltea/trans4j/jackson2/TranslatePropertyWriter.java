package io.github.oiltea.trans4j.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import io.github.oiltea.trans4j.core.TranslateService;
import io.github.oiltea.trans4j.core.Translate;
import java.util.Objects;

public class TranslatePropertyWriter extends BeanPropertyWriter {

  private final TranslateService translateService;
  private final BeanPropertyWriter fromWriter;
  private final Translate translate;

  protected TranslatePropertyWriter(
      TranslateService translateService, BeanPropertyWriter fromWriter, Translate translate) {
    this.translateService = translateService;
    this.fromWriter = fromWriter;
    this.translate = translate;
  }

  public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
      throws Exception {
    Object value = fromWriter.get(bean);
    String text = translateService.translate(translate.key(), Objects.toString(value));
    gen.writeStringField(getName(), text);
  }
}
