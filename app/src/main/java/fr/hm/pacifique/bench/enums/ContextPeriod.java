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
     M1("01", "01"),
    M2("02", "02"),
    M3("03", "03"),
    M4("04", "04"),
    M5("05", "05"),
    M6("06", "06"),
    M7("07", "07"),
    M8("08", "08"),
    M9("09", "09"),
    M10("10", "10"),
    M11("11", "11"),
    M12("12", "12"),
    AN("AN", "12"),
    Q1("Q1", "03"),
    Q2("Q2", "06"),
    Q3("Q3", "09"),
    Q4("Q4", "11");;

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
