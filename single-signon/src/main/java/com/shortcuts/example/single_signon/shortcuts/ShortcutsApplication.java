package com.shortcuts.example.single_signon.shortcuts;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ShortcutsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortcutsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
        return args -> {
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            System.out.println(String.format("Context has loaded beans: %s", String.join(", ", beanNames)));
        };
    }

}