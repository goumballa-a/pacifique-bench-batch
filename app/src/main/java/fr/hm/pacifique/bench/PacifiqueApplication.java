package fr.hm.pacifique.bench;

import fr.hm.fwk.web.EnableHmWeb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableHmWeb
@SpringBootApplication(scanBasePackages = "fr.hm.pacifique")
public class PacifiqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacifiqueApplication.class, args);
    }
}
