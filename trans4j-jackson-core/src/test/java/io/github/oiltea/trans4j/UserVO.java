package io.github.oiltea.trans4j;

import io.github.oiltea.trans4j.core.Translate;
import lombok.Data;

@Data
public class UserVO {

  private String genger;

  @Translate(key = "GENDER", from = "genger")
  private String gengerText;
}
