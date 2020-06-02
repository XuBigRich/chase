package top.piao888.chase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaseApplication.class, args);
    }

}
