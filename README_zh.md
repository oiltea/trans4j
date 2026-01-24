# Trans4j

[![CI](https://img.shields.io/github/actions/workflow/status/oiltea/trans4j/ci.yml?logo=github&label=CI)](https://github.com/oiltea/trans4j/actions)
[![Codecov](https://img.shields.io/codecov/c/github/oiltea/trans4j?logo=codecov&label=Coverage)](https://codecov.io/gh/oiltea/trans4j)
[![Java Version](https://img.shields.io/badge/java-17-brightgreen.svg?logo=openjdk&label=Java)](https://openjdk.org/)
[![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-2.x%20%7C%203.x%20%7C%204.x-6DB33F?logo=springboot&label=Spring%20Boot)](https://spring.io/projects/spring-boot)
[![GitHub Release](https://img.shields.io/github/v/release/oiltea/trans4j?logo=github&label=GitHub%20Release)](https://github.com/oiltea/trans4j/releases)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.oiltea/trans4j?logo=apachemaven&label=Maven%20Central)](https://search.maven.org/artifact/io.github.oiltea/trans4j)

[English](README.md) | [中文](README_zh.md)

---

一个轻量级、高性能的 Java 应用程序翻译框架，支持 Spring Boot 集成。在 JSON 序列化过程中自动进行字段翻译。

## ✨ 特性

- **🚀 高性能**: 内置 Caffeine 和 Redis 缓存支持
- **🔧 易于集成**: Spring Boot 自动配置，零配置启动
- **📦 模块化设计**: 只选择您需要的组件
- **🎯 注解驱动**: 简单的 `@Translate` 注解实现字段翻译
- **🔄 Jackson 集成**: JSON 序列化过程中自动翻译
- **⚡ 多种缓存策略**: 支持内存、Caffeine 和 Redis 缓存
- **🌐 Spring Boot 2/3/4 兼容**: 支持所有现代 Spring Boot 版本

## 📋 系统要求

- Java 17+
- Spring Boot 2.x/3.x/4.x

## 🚀 快速开始

### 1. 添加依赖

**Spring Boot Starter（推荐）**

```xml

<dependency>
  <groupId>io.github.oiltea</groupId>
  <artifactId>trans4j-spring-boot-starter</artifactId>
  <version>1.0.5</version>
</dependency>
```

### 2. 实现翻译提供者

```java

@Component
public class MyTranslationProvider implements TranslationProvider {

  @Override
  public Map<String, String> get(String key) {
    // 在这里实现您的翻译逻辑
    // 例如：从数据库、属性文件或外部 API 加载
    Map<String, String> translations = new HashMap<>();
    translations.put("ACTIVE", "激活");
    translations.put("INACTIVE", "未激活");
    return translations;
  }
}
```

### 3. 使用翻译注解

```java
public class UserStatus {

  private String code;

  @Translate(key = "user.status", from = "code")
  private String name;

  // getter 和 setter 方法
}
```

### 4. 配置缓存（可选）

Trans4j 支持多种缓存策略，选择适合您需求的方案：

| 缓存类型       | 需要的依赖                    | 使用场景        |
|------------|--------------------------|-------------|
| `none`     | 无                        | 无缓存，直接翻译    |
| `simple`   | 无（默认）                    | 内存缓存，单实例应用  |
| `caffeine` | `trans4j-cache-caffeine` | 高性能本地缓存     |
| `redis`    | `trans4j-cache-redis`    | 分布式缓存，多实例应用 |

**使用内存缓存，无需额外依赖（默认）**

配置：

```properties
trans4j.cache.type=simple
```

**禁用缓存（不推荐）**

配置：

```properties
trans4j.cache.type=none
```

**使用 Caffeine 缓存**

添加依赖：

```xml

<dependency>
  <groupId>io.github.oiltea</groupId>
  <artifactId>trans4j-cache-caffeine</artifactId>
  <version>1.0.5</version>
</dependency>
```

配置：

```properties
trans4j.cache.type=caffeine
trans4j.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=30m
```

**使用 Redis 缓存**

添加依赖：

```xml

<dependency>
  <groupId>io.github.oiltea</groupId>
  <artifactId>trans4j-cache-redis</artifactId>
  <version>1.0.5</version>
</dependency>
```

配置：

```properties
trans4j.cache.type=redis
trans4j.cache.redis.key-prefix=trans4j:
trans4j.cache.redis.time-to-live=10s
```

## 📚 模块说明

| 模块                            | 描述               | 依赖                      |
|-------------------------------|------------------|-------------------------|
| `trans4j-core`                | 核心翻译接口和实现        | Spring Boot（可选）         |
| `trans4j-spring-boot-starter` | Spring Boot 自动配置 | Jackson, Core           |
| `trans4j-cache-caffeine`      | Caffeine 缓存实现    | Caffeine, Core          |
| `trans4j-cache-redis`         | Redis 缓存实现       | Spring Data Redis, Core |
| `trans4j-jackson`             | Jackson 序列化集成    | Jackson 2/3, Core       |

## 💡 使用示例

### 基础翻译

```java

@RestController
public class UserController {

  @GetMapping("/users/{id}")
  public UserDto getUser(@PathVariable Long id) {
    return userService.findById(id);
    // 翻译在 JSON 序列化过程中自动进行
  }
}

public class UserDto {

  private Long id;
  private String statusCode;

  @Translate(key = "user.status", from = "statusCode")
  private String statusName;

  // getter 和 setter 方法
}
```

## 🏗️ 架构图

```
┌─────────────────────────────────────────────────────────────┐
│                      应用层                                 │
├─────────────────────────────────────────────────────────────┤
│  @Translate 注解   │  Jackson 集成   │  REST API          │
├─────────────────────────────────────────────────────────────┤
│                     翻译服务                                │
├─────────────────────────────────────────────────────────────┤
│  简单缓存  │  Caffeine 缓存  │  Redis 缓存  │  无缓存      │
├─────────────────────────────────────────────────────────────┤
│                    翻译提供者                               │
├─────────────────────────────────────────────────────────────┤
│   数据库   │   属性文件   │   外部 API   │   自定义        │
└─────────────────────────────────────────────────────────────┘
```

## 🤝 贡献

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交您的更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开一个 Pull Request

## 📄 许可证

本项目基于 Apache License 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 👥 作者

- **oiltea** - *初始工作* - [d15881156994@gmail](mailto:d15881156994@gmail.com)

## 🔗 链接

- [GitHub 仓库](https://github.com/oiltea/trans4j)
- [问题追踪](https://github.com/oiltea/trans4j/issues)
