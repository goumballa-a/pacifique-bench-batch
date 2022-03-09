package fr.hm.pacifique.bench.enums;

import fr.hm.pacifique.common.enums.common.CommonEnumeration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HalfFlow implements CommonEnumeration<HalfFlow> {
    F_PRIME("F-PRIME", "F_PRIME"),
    T1_TIERCE("T1-TIERCE", "T1_TIERCE"),
    T1_PRIME("T1-PRIME", "T1_PRIME"),
    F_SECONDE1("F-SECONDE1", "F_SECONDE1"),
    T3_TIERCE("T3-TIERCE", "T3_TIERCE"),
    T3_PRIME("T3-PRIME", "T3_PRIME"),
    T4_TIERCE("T4-TIERCE", "T4_TIERCE"),
    T4_PRIME("T4-PRIME", "T4_PRIME"),
    F_SECONDE2("F-SECONDE2", "F_SECONDE2");

    private final String code;
    private final String directory;
}
