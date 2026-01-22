package io.github.oiltea.trans4j.core;

public class DafaultTranslateService implements TranslateService {

  private final TranslateProvider translateProvider;

  public DafaultTranslateService(TranslateProvider translateProvider) {
    this.translateProvider = translateProvider;
  }

  @Override
  public String translate(String key, String value) {
    return translateProvider.load().get(key).get(value);
  }
}
