package fr.hm.pacifique.bench.enums.fprime;

import fr.hm.pacifique.common.enums.common.BLFileEnumeration;

public enum BlFPrimePacifique implements BLFileEnumeration<BlFPrimePacifique> {
    TOP("DHM_F-PRIME_TOP.csv"),
    STACK("*_DHM_F-PRIME_STACK_*.csv");

    private String filenamePattern;

    BlFPrimePacifique(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }

    @Override
    public String getFilenamePattern() {
        return filenamePattern;
    }
}
