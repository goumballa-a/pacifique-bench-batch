package fr.hm.pacifique.bench.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "pacifique")
@Component
@Getter
@Setter
public class PacifiqueProperties {
    private String env;
    private String rootDirectory;
    private int timeout;
    private int chunkNumber;
}
