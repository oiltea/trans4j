package io.github.oiltea.trans4j.core;

import java.util.Map;

public interface TranslateProvider {

  Map<String, Map<Object, String>> load();
}
