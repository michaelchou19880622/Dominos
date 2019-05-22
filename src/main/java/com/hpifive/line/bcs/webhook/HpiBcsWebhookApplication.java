package com.hpifive.line.bcs.webhook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan
@EnableJpaRepositories("com.hpifive.line.bcs.webhook.repository")
@EntityScan("com.hpifive.line.bcs.webhook.entities")
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public class HpiBcsWebhookApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HpiBcsWebhookApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(HpiBcsWebhookApplication.class, args);
	}
}
