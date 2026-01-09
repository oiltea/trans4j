package io.github.oiltea.trans4j.jackson3;

import io.github.oiltea.trans4j.core.annotation.Trans;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class TransSerializer extends ValueSerializer<String> {

  private final Trans trans;

  public TransSerializer(Trans trans) {
    this.trans = trans;
  }

  @Override
  public void serialize(String value, JsonGenerator gen, SerializationContext ctxt)
      throws JacksonException {
    if (value == null) {
      gen.writeNull();
      return;
    }
    System.err.println(trans);
    gen.writeString(value + "***********");
  }

}