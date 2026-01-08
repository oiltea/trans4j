# trans4j

> 一个优雅、高性能的字典翻译框架，支持 JPA 和 MyBatis，让字典翻译变得简单。

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-4.0.1+-green.svg)](https://spring.io/projects/spring-boot)

## ✨ 特性

- 🎯 **零侵入**：通过注解即可实现字典翻译，无需修改业务代码
- 🚀 **高性能**：支持全量字典预加载，智能缓存，减少重复查询
- 🔧 **易扩展**：提供 SPI 接口，使用者只需实现字典数据提供者
- 📦 **开箱即用**：Spring Boot Starter 自动配置，无需手动注册
- 🔄 **多场景支持**：同时支持 JPA 和 MyBatis
- 🎨 **灵活配置**：支持单个字典类型和全量字典两种模式

## 📦 模块说明

- `trans4j-core`：核心模块，提供字典翻译的基础能力
- `trans4j-ext-jpa`：JPA 扩展，支持 JPA 实体自动翻译
- `trans4j-ext-mybatis`：MyBatis 扩展，支持 MyBatis 查询结果自动翻译
- `trans4j-spring-boot-starter`：Spring Boot 自动配置，一键启用

## 🚀 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>io.github.oiltea</groupId>
    <artifactId>trans4j-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 实现字典提供者

```java
@Component
public class DbDictProvider implements DictProvider {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public Map<String, String> getDict(String type) {
        // 从数据库查询字典项
        List<DictItem> items = dictMapper.selectByType(type);
        return items.stream()
            .collect(Collectors.toMap(DictItem::getCode, DictItem::getName));
    }

    @Override
    public Map<String, Map<String, String>> getAllDicts() {
        // 全量加载（可选，用于性能优化）
        List<DictItem> allItems = dictMapper.selectAll();
        return allItems.stream()
            .collect(Collectors.groupingBy(
                DictItem::getType,
                Collectors.toMap(DictItem::getCode, DictItem::getName)
            ));
    }
}
```

### 3. 使用注解

#### JPA 使用方式

```java
@Entity
@EntityListeners(TransJpaEntityListener.class)
public class User {

    @Id
    private Long id;

    @Trans(key = "GENDER")
    private String gender;        // code: "M"

    private String genderName;     // name: "男"（自动填充）

    @Trans(key = "STATUS", refs = {"statusName", "statusText"})
    private String status;

    private String statusName;
    private String statusText;
}
```

#### MyBatis 使用方式

```java
public class UserVO {

    @Trans(key = "GENDER")
    private String gender;

    private String genderName;  // 自动填充

    @Trans(key = "TYPE", suffix = "Label")
    private String type;

    private String typeLabel;  // 自动填充
}
```

MyBatis 拦截器会自动处理查询结果，无需额外配置。

## 📖 详细文档

### @Trans 注解说明

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `key` | String | - | 字典类型（必填） |
| `refs` | String[] | {} | 目标字段名（可选） |
| `suffix` | String | "Name" | 字段名后缀（当未指定 refs 时使用） |
| `ignoreNull` | boolean | true | 是否忽略空值 |

### 使用示例

#### 方式1：使用默认后缀

```java
@Trans(key = "GENDER")
private String gender;
private String genderName;  // 自动填充
```

#### 方式2：指定目标字段

```java
@Trans(key = "STATUS", refs = {"statusName", "statusText"})
private String status;
private String statusName;
private String statusText;
```

#### 方式3：自定义后缀

```java
@Trans(key = "TYPE", suffix = "Label")
private String type;
private String typeLabel;  // 自动填充
```

### 配置属性

```yaml
trans4j:
  mybatis:
    enabled: true  # 是否启用 MyBatis 拦截器（默认：true）
```

## 🏗️ 架构设计

### 核心组件

1. **DictProvider**：字典数据提供者 SPI，使用者实现此接口提供字典数据
2. **TransService**：翻译服务接口，负责执行字典翻译逻辑
3. **DefaultTransService**：默认实现，支持全量字典和按类型加载两种模式
4. **@Trans**：字典翻译注解，标记需要翻译的字段

### 工作流程

```
1. 实体/对象加载
   ↓
2. 检测 @Trans 注解字段
   ↓
3. 收集字典类型
   ↓
4. 加载字典数据（优先全量，否则按类型）
   ↓
5. 执行翻译（code -> name）
   ↓
6. 写入目标字段
```

### 性能优化

- **全量字典预加载**：一次加载所有字典，减少数据库查询
- **智能缓存**：实例级别缓存，避免重复加载
- **按需加载**：仅加载本次翻译需要的字典类型
- **批量处理**：支持集合和数组的批量翻译

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。

## 👤 作者

**oiltea**

- Email: oiltea@qq.com
- GitHub: [@oiltea](https://github.com/oiltea)

## 🙏 致谢

感谢所有为本项目做出贡献的开发者！
