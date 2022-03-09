package fr.hm.pacifique.bench.projection.top;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CPrime1TopCsv extends AbstractTopCsv {
    @Mandatory
    @Key(csvKey = "Date reporting", batchKey = "dateReporting")
    private LocalDateTime dateReporting;
    @Mandatory
    @Key(csvKey = "Periode", batchKey = "period")
    private String period;
}
