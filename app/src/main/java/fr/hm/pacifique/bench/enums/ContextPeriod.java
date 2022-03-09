package fr.hm.pacifique.bench.enums;

import fr.hm.pacifique.common.enums.common.CommonEnumeration;
import fr.hm.pacifique.common.enums.common.EnumNotFoundException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public enum ContextPeriod implements CommonEnumeration<ContextPeriod> {
    AN("AN", "12"),
    Q1("Q1", "03"),
    Q2("Q2", "06"),
    Q3("Q3", "09"),
    Q4("Q4", "11");

    private static final Map<String, ContextPeriod> map = initMap(ContextPeriod.values());
    private final String label;
    private final String month;

    ContextPeriod(String label, String month) {
        this.label = label;
        this.month = month;
    }

    public static ContextPeriod parse(String label) {
        return Optional.ofNullable(map.get(label))
                .orElseThrow(() -> new EnumNotFoundException("No context period label matching with " + label));
    }

    private static Map<String, ContextPeriod> initMap(ContextPeriod[] contextPeriods) {
        return Arrays.stream(contextPeriods)
                .collect(Collectors.toMap(
                        ContextPeriod::getLabel,
                        contextPeriod -> contextPeriod
                ));
    }
}
