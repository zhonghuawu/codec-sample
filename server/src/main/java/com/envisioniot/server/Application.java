package com.envisioniot.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhonghua.wu
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        } catch (Throwable t) {
            log.error("fatal error in Application.main: ", t);
            System.exit(1);
        }
    }
}
