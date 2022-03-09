package fr.hm.pacifique.bench.enums;

import fr.hm.pacifique.bench.constant.BatchConstant;
import fr.hm.pacifique.common.enums.common.CommonEnumeration;
import lombok.Getter;

@Getter
public enum AppType implements CommonEnumeration<AppType> {
    BENCH(BatchConstant.BENCH_DIRECTORY),
    PACIFIQUE("");

    private String directory;

    AppType(String directory) {
        this.directory = directory;
    }
}
