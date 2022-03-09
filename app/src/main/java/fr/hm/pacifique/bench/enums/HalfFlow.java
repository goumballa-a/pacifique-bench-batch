package fr.hm.pacifique.bench.enums;

import fr.hm.pacifique.common.enums.CommonFlowCode;
import fr.hm.pacifique.common.enums.common.CommonEnumeration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HalfFlow implements CommonFlowCode<HalfFlow> {
    Q_PRIME1("Q-PRIME1", "Q_PRIME1"),
    D_PRIME("D-PRIME", "D_PRIME"),
    Q_PRIME2("Q-PRIME2", "Q_PRIME2"),
    Q_PRIME3("Q-PRIME3", "Q_PRIME3"),
    F_PRIME3("F-PRIME3", "F_PRIME3"),
    Q_PRIME5("Q-PRIME5", "Q_PRIME5"),
    Q_PRIME4("Q-PRIME4", "Q_PRIME4"),
    Q_SECONDE1("Q-SECONDE1", "Q_SECONDE1"),
    D_SECONDE1("D-SECONDE1", "D_SECONDE1"),
    D_SECONDE2("D-SECONDE2", "D_SECONDE2"),
    D_SECONDE3("D-SECONDE3", "D_SECONDE3"),
    D_SECONDE4("D-SECONDE4", "D_SECONDE4"),
    Q_SECONDE2("Q-SECONDE2", "Q_SECONDE2"),
    Q_SECONDE3("Q-SECONDE3", "Q_SECONDE3");

    private final String code;
    private final String directory;
}
