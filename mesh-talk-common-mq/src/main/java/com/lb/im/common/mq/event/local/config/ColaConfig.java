package com.lb.im.common.mq.event.local.config;

import com.alibaba.cola.boot.SpringBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring配置类，用于扫描com.alibaba.cola包下的组件并配置相关Bean。
 * 通过@ComponentScan启用组件扫描，确保相关Spring Cola模块被正确加载。
 */
@Configuration
@ComponentScan(value = {"com.alibaba.cola"})
public class ColaConfig {

    /**
     * 创建并配置SpringBootstrap Bean，指定在初始化时调用init方法。
     * 该Bean负责初始化Spring应用上下文。
     *
     * @return SpringBootstrap Spring Boot启动引导实例，用于初始化应用上下文
     */
    @Bean(initMethod = "init")
    public SpringBootstrap bootstrap() {
        SpringBootstrap bootstrap = new SpringBootstrap();
        return bootstrap;
    }
}
