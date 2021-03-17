package io.cloudmobility.tiago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication
public class PrivateHospitalApplication {
    public static void main(final String[] args) {
        SpringApplication.run(PrivateHospitalApplication.class, args);
    }
}
