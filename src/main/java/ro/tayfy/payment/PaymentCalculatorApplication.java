package ro.tayfy.payment;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PaymentCalculatorApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
