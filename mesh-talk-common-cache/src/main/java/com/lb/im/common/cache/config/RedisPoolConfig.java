package com.lb.im.common.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.lettuce.core.resource.ClientResources;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class RedisPoolConfig {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.lettuce.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWait;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private int database;

    /**
     * 配置Redis连接池参数
     * @return GenericObjectPoolConfig 连接池配置对象
     */
    @Bean
    public GenericObjectPoolConfig<Object> genericObjectPoolConfig() {
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWait(Duration.ofMillis(maxWait));
        return poolConfig;
    }

    /**
     * 创建Lettuce客户端资源
     * @return DefaultClientResources 客户端资源实例
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(ClientResources.class)
    public DefaultClientResources lettuceClientResources() {
        return DefaultClientResources.create();
    }

    /**
     * 配置Redis单节点连接信息
     * @return RedisStandaloneConfiguration Redis连接配置对象
     */
    @Bean
    public RedisStandaloneConfiguration redisSentinelConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setPort(port);
        return redisStandaloneConfiguration;
    }

    /**
     * 创建Lettuce客户端配置
     * @param genericObjectPoolConfig 连接池配置
     * @param lettuceClientResources 客户端资源
     * @return LettuceClientConfiguration 客户端配置对象
     */
    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration(GenericObjectPoolConfig<Object> genericObjectPoolConfig, ClientResources lettuceClientResources) {
        return LettucePoolingClientConfiguration.builder().clientResources(lettuceClientResources).poolConfig(genericObjectPoolConfig).build();
    }

    /**
     * 创建Lettuce连接工厂
     * @param redisSentinelConfiguration Redis连接配置
     * @param lettuceClientConfiguration 客户端配置
     * @return LettuceConnectionFactory Redis连接工厂实例
     */
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisSentinelConfiguration, LettuceClientConfiguration lettuceClientConfiguration) {
        return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
    }

    /**
     * 配置RedisTemplate对象
     * @param lettuceConnectionFactory Redis连接工厂
     * @return RedisTemplate<String, Object> Redis操作模板
     */
    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        // 配置JSON序列化器
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 注册日期类型序列化/反序列化器
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        objectMapper.registerModule(simpleModule);
        jsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置序列化配置
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置StringRedisTemplate对象
     * @param lettuceConnectionFactory Redis连接工厂
     * @return StringRedisTemplate 字符串操作模板
     */
    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 重新初始化工厂确保配置生效
        lettuceConnectionFactory.afterPropertiesSet();
        return new StringRedisTemplate(lettuceConnectionFactory);
    }

}
