package ru.yandex.practicum.ewmmainservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.yandex.practicum.ewmmainservice", "ru.yandex.practicum.statsserviceclient"})
public class EwmMainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EwmMainServiceApplication.class, args);
    }

}
