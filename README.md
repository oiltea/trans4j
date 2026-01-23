# Trans4j

[English](README.md) | [中文](README_zh.md)

---

A lightweight, high-performance translation framework for Java applications with Spring Boot
integration. Automatic field translation during JSON serialization.

## ✨ Features

- **🚀 High Performance**: Built-in caching support with Caffeine and Redis
- **🔧 Easy Integration**: Spring Boot auto-configuration with zero configuration
- **📦 Modular Design**: Choose only the components you need
- **🎯 Annotation-Driven**: Simple `@Translate` annotation for field translation
- **🔄 Jackson Integration**: Automatic translation during JSON serialization
- **⚡ Multiple Cache Strategies**: Support for in-memory, Caffeine, and Redis caching
- **🌐 Spring Boot 2/3/4 Compatible**: Works with all modern Spring Boot versions

## 📋 Requirements

- Java 17+
- Spring Boot 2.x/3.x/4.x
- Maven 3.6+

## 🚀 Quick Start

### 1. Add Dependencies

**Spring Boot Starter (Recommended)**

```xml

<dependency>
  <groupId>io.github.oiltea</groupId>
  <artifactId>trans4j-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 2. Implement Translation Provider

```java

@Component
public class MyTranslationProvider implements TranslationProvider {

  @Override
  public Map<String, String> get(String key) {
    // Implement your translation logic here
    // e.g., load from database, properties file, or external API
    Map<String, String> translations = new HashMap<>();
    translations.put("ACTIVE", "Active");
    translations.put("INACTIVE", "Inactive");
    return translations;
  }
}
```

### 3. Use Translation Annotation

```java
public class UserStatus {

  private String code;

  @Translate(key = "user.status", from = "code")
  private String name;

  // getters and setters
}
```

### 4. Configure Cache (Optional)

Trans4j supports multiple cache strategies. Choose the one that fits your needs:

| Cache Type | Dependency Required      | Use Case                              |
|------------|--------------------------|---------------------------------------|
| `none`     | None                     | No caching, direct translation        |
| `simple`   | None (default)           | In-memory cache, single instance      |
| `caffeine` | `trans4j-cache-caffeine` | High-performance local cache          |
| `redis`    | `trans4j-cache-redis`    | Distributed cache, multiple instances |

**Using In-memory Cache, no additional dependency required (Default)**

Configure:

```properties
trans4j.cache.type=simple
```

**Disable cache (not recommended)**

Configure:

```properties
trans4j.cache.type=none
```

**Using Caffeine Cache**

Add dependency:

```xml

<dependency>
  <groupId>io.github.oiltea</groupId>
  <artifactId>trans4j-cache-caffeine</artifactId>
  <version>1.0.0</version>
</dependency>
```

Configure:

```properties
trans4j.cache.type=caffeine
trans4j.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=30m
```

**Using Redis Cache**

Add dependency:

```xml

<dependency>
  <groupId>io.github.oiltea</groupId>
  <artifactId>trans4j-cache-redis</artifactId>
  <version>1.0.0</version>
</dependency>
```

Configure:

```properties
trans4j.cache.type=redis
trans4j.cache.redis.key-prefix=trans4j:
trans4j.cache.redis.time-to-live=10s
```

## 📚 Modules

| Module                        | Description                                     | Dependencies            |
|-------------------------------|-------------------------------------------------|-------------------------|
| `trans4j-core`                | Core translation interfaces and implementations | Spring Boot (optional)  |
| `trans4j-spring-boot-starter` | Auto-configuration for Spring Boot              | Jackson, Core           |
| `trans4j-cache-caffeine`      | Caffeine cache implementation                   | Caffeine, Core          |
| `trans4j-cache-redis`         | Redis cache implementation                      | Spring Data Redis, Core |
| `trans4j-jackson`             | Jackson serialization integration               | Jackson 2/3, Core       |

## 💡 Usage Examples

### Basic Translation

```java

@RestController
public class UserController {

  @GetMapping("/users/{id}")
  public UserDto getUser(@PathVariable Long id) {
    return userService.findById(id);
    // Translation happens automatically during JSON serialization
  }
}

public class UserDto {

  private Long id;
  private String statusCode;

  @Translate(key = "user.status", from = "statusCode")
  private String statusName;

  // getters and setters
}

```

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Application Layer                        │
├─────────────────────────────────────────────────────────────┤
│  @Translate Annotation  │  Jackson Integration  │  REST API │
├─────────────────────────────────────────────────────────────┤
│                   Translation Service                       │
├─────────────────────────────────────────────────────────────┤
│  Simple Cache  │  Caffeine Cache  │  Redis Cache  │  No Cache│
├─────────────────────────────────────────────────────────────┤
│                  Translation Provider                       │
├─────────────────────────────────────────────────────────────┤
│   Database   │   Properties   │   External API   │   Custom │
└─────────────────────────────────────────────────────────────┘
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **oiltea** - *Initial work* - [d15881156994@gmail](mailto:d15881156994@gmail)

## 🔗 Links

- [GitHub Repository](https://github.com/oiltea/trans4j)
- [Issue Tracker](https://github.com/oiltea/trans4j/issues)
