package com.lydiasystems.challenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Value("${config.async-config.core-pool-size}")
    private int corePoolSize;
    @Value("${config.async-config.max-pool-size}")
    private int maxPoolSize;
    @Value("${config.async-config.queue-capacity}")
    private int queueCapacity;
    @Value("${config.async-config.thread-name-prefix}")
    private String threadNamePrefix;
    @Value("${config.async-config.await-termination-seconds}")
    private int awaitTerminationSeconds;
    @Value("${config.async-config.wait-for-tasks-to-complete-on-shutdown}")
    private boolean waitForTasksToCompleteOnShutdown;

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
