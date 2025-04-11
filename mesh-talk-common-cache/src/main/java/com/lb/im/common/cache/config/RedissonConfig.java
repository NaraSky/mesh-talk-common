package com.lb.im.common.cache.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Redisson配置类，根据不同的Redis部署类型（单节点或集群）创建对应的RedissonClient实例。
 */
@Configuration
@ConditionalOnProperty(name = "distributed.lock.type", havingValue = "redisson")
public class RedissonConfig {

    @Value("${spring.redis.address}")
    private String redisAddress;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    /**
     * 创建单节点模式的Redisson客户端实例。
     *
     * @return RedissonClient 单节点模式的Redisson客户端
     */
    @Bean(name = "redissonClient")
    @ConditionalOnProperty(name = "redis.arrange.type", havingValue = "single")
    public RedissonClient singleRedissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redisAddress).setDatabase(database);
        // 如果密码不为空，则设置密码
        if (!StrUtil.isEmpty(password)){
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    /**
     * 创建集群模式的Redisson客户端实例。
     *
     * @return RedissonClient 集群模式的Redisson客户端
     */
    @Bean(name = "redissonClient")
    @ConditionalOnProperty(name = "redis.arrange.type", havingValue = "cluster")
    public RedissonClient clusterRedissonClient(){
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.setNodeAddresses(Arrays.asList(redisAddress));
        // 如果密码不为空，则设置密码
        if (!StrUtil.isEmpty(password)){
            clusterServersConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}
