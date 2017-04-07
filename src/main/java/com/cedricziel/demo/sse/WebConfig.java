package com.cedricziel.demo.sse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	private final AsyncTaskExecutor executor = new DefaultManagedTaskExecutor();

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

        configurer.setDefaultTimeout(1000000);
 		configurer.setTaskExecutor(executor);
    }
}
