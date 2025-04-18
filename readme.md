# 🚀 Mesh-Talk-Common: 即时通讯的超级英雄工具箱 🦸‍♂️

![GitHub stars](https://img.shields.io/github/stars/NaraSky/mesh-talk-common?style=social) 
![GitHub forks](https://img.shields.io/github/forks/NaraSky/mesh-talk-common?style=social) 
![GitHub issues](https://img.shields.io/github/issues/NaraSky/mesh-talk-common)

欢迎体验 **Mesh-Talk-Common**，一个为即时通讯（IM）系统打造的公共组件库！它就像是 IM 开发者的“瑞士军刀”，集成了高性能缓存、分布式锁、唯一 ID 生成器、消息队列支持等功能，让你的 IM 系统如虎添翼，轻松应对高并发、分布式环境下的各种挑战！💬💥

---

## 🌟 为什么选择 Mesh-Talk-Common？

想让你的 IM 系统快如闪电 ⚡、稳如磐石 🪨？Mesh-Talk-Common 提供了以下超能力：

- **分布式缓存**：基于 Redis，解决缓存击穿、穿透、雪崩问题，像超级英雄一样守护你的数据库！🛡️
- **分布式锁**：Redisson 驱动，线程安全无压力，确保热点数据重建不打架！🔒
- **全局唯一 ID**：雪花算法生成 64 位 ID，性能炸裂，永不重复！❄️
- **消息队列**：支持 RocketMQ 和 COLA 事件总线，异步消息投递稳准狠！📨
- **本地缓存**：Guava Cache 加持，低延迟场景的秘密武器！⚡
- **领域模型**：统一的 IM 消息、用户、会话模型，开发效率直接起飞！✈️

> **一句话总结**：Mesh-Talk-Common 让你的 IM 系统开发省心省力，性能拉满，稳定在线！

---

## 🛠️ 功能一览

| 模块 | 功能 | 超能力描述 |
|------|------|-------------|
| **mesh-talk-common-cache** | 分布式缓存、本地缓存、分布式锁、ID生成 | Redis + Guava + Redisson + Snowflake，性能与可靠性的完美组合！ |
| **mesh-talk-common-domain** | 领域模型、常量、枚举、JWT | 统一的消息模型和认证工具，IM 开发从此井井有条！ |
| **mesh-talk-common-mq** | 消息队列（RocketMQ/COLA） | 异步消息投递，事务消息支持，消息不丢不重！ |

---

## 🎉 快速开始

### 前置条件
- Java 8 或以上
- Maven 3.6+
- Redis（用于分布式缓存和锁）
- RocketMQ（可选，用于消息队列）
- Spring Boot 2.x

### 安装与使用

1. **克隆仓库**：
   ```bash
   git clone https://github.com/your-username/mesh-talk-common.git
   ```

2. **引入依赖**：
   将 `mesh-talk-common` 作为 Maven 依赖引入你的项目：
   ```xml
   <dependency>
       <groupId>com.lb.im</groupId>
       <artifactId>mesh-talk-common</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

3. **配置环境**：
   在 `application.yml` 中配置 Redis、RocketMQ 等参数：
   ```yaml
   spring:
     redis:
       host: localhost
       port: 6379
       database: 0
       lettuce:
         pool:
           max-active: 8
           max-idle: 8
           min-idle: 0
           max-wait: 3000
   message:
     mq:
       type: rocketmq
       event:
         type: rocketmq
   ```

4. **开始使用**：
    - **缓存操作**：
      ```java
      @Autowired
      private DistributedCacheService cacheService;
 
      // 设置缓存
      cacheService.set("user:1", new User("Alice"), 3600L, TimeUnit.SECONDS);
 
      // 获取缓存
      User user = cacheService.getObject("user:1", User.class);
      ```

    - **生成唯一 ID**：
      ```java
      SnowFlake snowFlake = SnowFlakeFactory.getSnowFlakeFromCache();
      long uniqueId = snowFlake.nextId();
      ```

    - **发送消息**：
      ```java
      @Autowired
      private MessageSenderService messageSender;
 
      TopicMessage message = new TopicMessage("im_message_private");
      messageSender.send(message);
      ```

---

## 📖 核心功能详解

### 1. 分布式缓存 🗄️
- **Redis 驱动**：支持高效的缓存操作，结合 Jackson 序列化支持复杂对象。
- **防缓存问题**：
    - **穿透**：缓存空值，短 TTL 保护数据库。
    - **击穿**：分布式锁 + 逻辑过期，热点数据安全重建。
    - **雪崩**：异步缓存重建，旧数据继续服务。
- 示例：
  ```java
  User user = cacheService.queryWithPassThrough("user:", 1L, User.class, id -> dbService.findUser(id), 3600L, TimeUnit.SECONDS);
  ```

### 2. 分布式锁 🔐
- 使用 Redisson 实现，支持单节点和集群模式。
- 提供多种加锁方式（tryLock、lock），确保线程安全。
- 示例：
  ```java
  DistributedLock lock = distributedLockFactory.getDistributedLock("lock:user:1");
  if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
      try {
          // 执行业务逻辑
      } finally {
          lock.unlock();
      }
  }
  ```

### 3. 雪花算法 ID 生成 ❄️
- 64 位唯一 ID，包含时间戳、数据中心、机器 ID 和序列号。
- 防时间回拨，性能高，适合分布式环境。
- 示例：
  ```java
  SnowFlake snowFlake = SnowFlakeFactory.getSnowFlakeByDataCenterIdAndMachineIdFromCache(1L, 1L);
  long id = snowFlake.nextId();
  ```

### 4. 消息队列 📨
- 支持 RocketMQ（分布式）和 COLA（本地事件总线）。
- 提供事务消息支持，确保消息投递一致性。
- 示例：
  ```java
  TransactionSendResult result = messageSender.sendMessageTransaction(new TopicMessage("im_result_private"), null);
  ```

### 5. 本地缓存 ⚡
- 基于 Guava Cache，支持自定义容量和过期时间。
- 适合低延迟、高频访问场景。
- 示例：
  ```java
  @Autowired
  private LocalCacheService<String, String> localCache;

  localCache.put("key", "value");
  String value = localCache.getIfPresent("key");
  ```

---

## 🧑‍💻 贡献指南

我们欢迎任何形式的贡献！无论是修复 Bug、添加新功能还是改进文档，都让 Mesh-Talk-Common 变得更好！🌈

1. Fork 本仓库。
2. 创建你的功能分支（`git checkout -b feature/awesome-feature`）。
3. 提交你的更改（`git commit -m 'Add awesome feature'`）。
4. 推送到远程分支（`git push origin feature/awesome-feature`）。
5. 提交 Pull Request，我们会尽快审核！🚀

---

## 🙌 致谢

感谢所有为即时通讯系统开发默默付出的开发者！特别感谢以下开源项目：
- [Redis](https://redis.io/)
- [Redisson](https://redisson.org/)
- [RocketMQ](https://rocketmq.apache.org/)
- [Guava](https://github.com/google/guava)
- [Protostuff](https://github.com/protostuff/protostuff)

> **让我们一起打造更强大的即时通讯系统！** 🚀

