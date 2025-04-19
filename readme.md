# 🚀 Mesh-Talk-Common: Superhero Toolkit for Instant Messaging 🦸‍♂️

![GitHub stars](https://img.shields.io/github/stars/NaraSky/mesh-talk-common?style=social) 
![GitHub forks](https://img.shields.io/github/forks/NaraSky/mesh-talk-common?style=social) 
![GitHub issues](https://img.shields.io/github/issues/NaraSky/mesh-talk-common)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

Welcome to **Mesh-Talk-Common**, a powerful component library designed for Instant Messaging (IM) systems! Think of it as a "Swiss Army knife" for IM developers, integrating high-performance caching, distributed locks, unique ID generation, message queue support, and more. It empowers your IM system to effortlessly handle high concurrency and distributed environment challenges! 💬💥

## 📑 Table of Contents

- [Why Choose Mesh-Talk-Common?](#-why-choose-mesh-talk-common)
- [Features Overview](#️-features-overview)
- [Architecture](#-architecture)
- [Quick Start](#-quick-start)
- [Core Features Explained](#-core-features-explained)
- [Contribution Guidelines](#-contribution-guidelines)
- [Getting Help](#-getting-help)
- [Acknowledgements](#-acknowledgements)
- [License](#-license)

---

## 🌟 Why Choose Mesh-Talk-Common?

Want your IM system to be lightning-fast ⚡ and rock-solid 🪨? Mesh-Talk-Common provides these superpowers:

- **Distributed Caching**: Redis-based solution that tackles cache penetration, breakdown, and avalanche issues, protecting your database like a superhero! 🛡️
- **Distributed Locks**: Powered by Redisson, ensuring thread safety and controlled hot data rebuilding! 🔒
- **Global Unique IDs**: Snowflake algorithm generating 64-bit IDs with explosive performance, never repeating! ❄️
- **Message Queuing**: Support for RocketMQ and COLA event bus, delivering asynchronous messages reliably! 📨
- **Local Caching**: Enhanced with Guava Cache, your secret weapon for low-latency scenarios! ⚡
- **Domain Models**: Unified IM message, user, and session models that skyrocket development efficiency! ✈️

> **In a nutshell**: Mesh-Talk-Common makes your IM system development effortless, maximizes performance, and ensures stability!

---

## 🛠️ Features Overview

| Module | Functionality | Superpowers Description |
|--------|--------------|-------------------------|
| **mesh-talk-common-cache** | Distributed caching, local caching, distributed locks, ID generation | Redis + Guava + Redisson + Snowflake, the perfect combination of performance and reliability! |
| **mesh-talk-common-domain** | Domain models, constants, enums, JWT | Unified message models and authentication tools, making IM development organized! |
| **mesh-talk-common-mq** | Message queuing (RocketMQ/COLA) | Asynchronous message delivery, transaction message support, no message loss or duplication! |

## 🏗 Architecture

Mesh-Talk-Common is designed with a modular architecture that allows you to use components independently or together as a complete solution:

```
┌─────────────────────────────────────────────────────────────┐
│                     Your IM Application                      │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                     Mesh-Talk-Common                         │
│                                                              │
│  ┌─────────────────┐   ┌─────────────────┐  ┌──────────────┐ │
│  │      Cache      │   │     Domain      │  │      MQ      │ │
│  │                 │   │                 │  │              │ │
│  │ ┌─────────────┐ │   │ ┌─────────────┐ │  │┌────────────┐│ │
│  │ │Distributed  │ │   │ │IM Models    │ │  ││Message     ││ │
│  │ │Cache (Redis)│ │   │ │(User/Msg)   │ │  ││Sender      ││ │
│  │ └─────────────┘ │   │ └─────────────┘ │  │└────────────┘│ │
│  │                 │   │                 │  │              │ │
│  │ ┌─────────────┐ │   │ ┌─────────────┐ │  │┌────────────┐│ │
│  │ │Local Cache  │ │   │ │Constants    │ │  ││Event       ││ │
│  │ │(Guava)      │ │   │ │& Enums      │ │  ││Handling    ││ │
│  │ └─────────────┘ │   │ └─────────────┘ │  │└────────────┘│ │
│  │                 │   │                 │  │              │ │
│  │ ┌─────────────┐ │   │ ┌─────────────┐ │  │┌────────────┐│ │
│  │ │Distributed  │ │   │ │JWT Utils    │ │  ││RocketMQ/   ││ │
│  │ │Lock         │ │   │ │             │ │  ││COLA Support││ │
│  │ └─────────────┘ │   │ └─────────────┘ │  │└────────────┘│ │
│  │                 │   │                 │  │              │ │
│  │ ┌─────────────┐ │   │ ┌─────────────┐ │  │              │ │
│  │ │ID Generator │ │   │ │Exceptions   │ │  │              │ │
│  │ │(Snowflake)  │ │   │ │             │ │  │              │ │
│  │ └─────────────┘ │   │ └─────────────┘ │  │              │ │
│  └─────────────────┘   └─────────────────┘  └──────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

---

## 🎉 Quick Start

### Prerequisites
- Java 8 or above
- Maven 3.6+
- Redis (for distributed caching and locks)
- RocketMQ (optional, for message queuing)
- Spring Boot 2.x

### Installation and Usage

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/mesh-talk-common.git
   ```

2. **Add as Maven dependency**:
   Include `mesh-talk-common` as a Maven dependency in your project:
   ```xml
   <dependency>
       <groupId>com.lb.im</groupId>
       <artifactId>mesh-talk-common</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

3. **Configure environment**:
   Set up Redis, RocketMQ, and other parameters in your `application.yml`:
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

4. **Start using**:
    - **Cache operations**:
      ```java
      @Autowired
      private DistributedCacheService cacheService;

      // Set cache
      cacheService.set("user:1", new User("Alice"), 3600L, TimeUnit.SECONDS);

      // Get from cache
      User user = cacheService.getObject("user:1", User.class);
      ```

    - **Generate unique IDs**:
      ```java
      SnowFlake snowFlake = SnowFlakeFactory.getSnowFlakeFromCache();
      long uniqueId = snowFlake.nextId();
      ```

    - **Send messages**:
      ```java
      @Autowired
      private MessageSenderService messageSender;

      TopicMessage message = new TopicMessage("im_message_private");
      messageSender.send(message);
      ```

---

## 📖 Core Features Explained

### 1. Distributed Caching 🗄️
- **Redis-powered**: Supports efficient cache operations with Jackson serialization for complex objects.
- **Cache problem prevention**:
    - **Penetration**: Caches null values with short TTL to protect the database.
    - **Breakdown**: Distributed locks + logical expiration for safe hot data rebuilding.
    - **Avalanche**: Asynchronous cache rebuilding, with old data continuing to serve.
- Example:
  ```java
  User user = cacheService.queryWithPassThrough("user:", 1L, User.class, id -> dbService.findUser(id), 3600L, TimeUnit.SECONDS);
  ```

### 2. Distributed Locks 🔐
- Implemented with Redisson, supporting both single-node and cluster modes.
- Provides various locking methods (tryLock, lock) to ensure thread safety.
- Example:
  ```java
  DistributedLock lock = distributedLockFactory.getDistributedLock("lock:user:1");
  if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
      try {
          // Execute business logic
      } finally {
          lock.unlock();
      }
  }
  ```

### 3. Snowflake ID Generation ❄️
- 64-bit unique IDs containing timestamp, data center, machine ID, and sequence number.
- Prevents time rollback issues, high performance, suitable for distributed environments.
- Example:
  ```java
  SnowFlake snowFlake = SnowFlakeFactory.getSnowFlakeByDataCenterIdAndMachineIdFromCache(1L, 1L);
  long id = snowFlake.nextId();
  ```

### 4. Message Queuing 📨
- Supports RocketMQ (distributed) and COLA (local event bus).
- Provides transaction message support, ensuring message delivery consistency.
- Example:
  ```java
  TransactionSendResult result = messageSender.sendMessageTransaction(new TopicMessage("im_result_private"), null);
  ```

### 5. Local Caching ⚡
- Based on Guava Cache, supporting custom capacity and expiration time.
- Ideal for low-latency, high-frequency access scenarios.
- Example:
  ```java
  @Autowired
  private LocalCacheService<String, String> localCache;

  localCache.put("key", "value");
  String value = localCache.getIfPresent("key");
  ```

---

## 🧑‍💻 Contribution Guidelines

We welcome contributions of all kinds! Whether fixing bugs, adding new features, or improving documentation, you're helping make Mesh-Talk-Common better! 🌈

1. Fork this repository.
2. Create your feature branch (`git checkout -b feature/awesome-feature`).
3. Commit your changes (`git commit -m 'Add awesome feature'`).
4. Push to the branch (`git push origin feature/awesome-feature`).
5. Submit a Pull Request, and we'll review it as soon as possible! 🚀

---

## 🙋‍♂️ Getting Help

Need assistance? We've got you covered:

- **GitHub Issues**: For bug reports and feature requests
- **Discussions**: For general questions and community discussions
- **Documentation**: Check our [Wiki](https://github.com/your-username/mesh-talk-common/wiki) for detailed guides

---

## 🙌 Acknowledgements

Thanks to all developers who contribute to instant messaging system development! Special thanks to these open-source projects:
- [Redis](https://redis.io/)
- [Redisson](https://redisson.org/)
- [RocketMQ](https://rocketmq.apache.org/)
- [Guava](https://github.com/google/guava)
- [Protostuff](https://github.com/protostuff/protostuff)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

> **Let's build more powerful instant messaging systems together!** 🚀
