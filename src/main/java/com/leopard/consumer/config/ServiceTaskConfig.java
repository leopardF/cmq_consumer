package com.leopard.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author leopard
 */
@Configuration
public class ServiceTaskConfig {

    /**
     * 核心线程数（默认线程数）
     */
    private static final int corePoolSize = 2;
    /**
     * 最大线程数
     */
    private static final int maxPoolSize = 5;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int keepAliveTime = 5;
    /**
     * 缓冲队列大小
     */
    private static final int queueCapacity = 200;
    /**
     * 线程池名前缀
     */
    private static final String threadNamePrefix = "Async-business-Service";

    @Bean("serviceTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix + "-");
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

}
