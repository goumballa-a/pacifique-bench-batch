package fr.hm.pacifique.bench.enums;

import fr.hm.pacifique.common.enums.CommonApplication;
import fr.hm.pacifique.common.enums.common.CommonEnumeration;
import fr.hm.pacifique.common.enums.common.EnumNotFoundException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public enum Application implements CommonApplication<Application> {
    DENSITY("DENSITY", "DHM"),
    RI("RI", "MOO"),
    SAPFC("SAPFC", "EPM"),
    CODA("CODAGEN", "COD"),
    DECIBEL("DECIBEL", "DCB"),
    VEGA("VEGA", "VEG"),
    METAUX("METAUX", "MTX");

    private static final Map<String, Application> map = initMap(Application.values());
    private String directory;
    private String code;

    Application(String directory, String code) {
        this.directory = directory;
        this.code = code;
    }

    public static Application parse(String directory) {
        return Optional.ofNullable(map.get(directory))
                .orElseThrow(() -> new EnumNotFoundException("No source application matching with " + directory));
    }

    private static Map<String, Application> initMap(Application[] applications) {
        return Arrays.stream(applications)
                .collect(Collectors.toMap(
                        Application::getDirectory,
                        application -> application
                ));
    }
}
