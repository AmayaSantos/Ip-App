package meli.ipApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableSwagger2
public class IpAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(IpAppApplication.class, args);
  }

}
