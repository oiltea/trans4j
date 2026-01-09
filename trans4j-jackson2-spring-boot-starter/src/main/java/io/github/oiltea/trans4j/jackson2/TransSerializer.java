package io.github.oiltea.trans4j.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.oiltea.trans4j.core.annotation.Trans;
import io.github.oiltea.trans4j.core.trans.Converter;
import java.io.IOException;

public class TransSerializer extends JsonSerializer<String> {

  private final Converter converter;

  private final Trans trans;

  public TransSerializer(Converter converter, Trans trans) {
    this.converter = converter;
    this.trans = trans;
  }

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider serializerProvider)
      throws IOException {
    if (value == null) {
      gen.writeNull();
      return;
    }
    System.err.println(trans);
    gen.writeString(value + "***********");
  }

}