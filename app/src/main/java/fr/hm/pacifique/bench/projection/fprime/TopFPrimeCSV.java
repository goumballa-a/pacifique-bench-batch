package fr.hm.pacifique.bench.projection.fprime;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TopFPrimeCSV extends AbstractTopCsv {
    @Mandatory
    @Key(csvKey = "Date reporting", batchKey = "dateExtraction")
    private LocalDateTime dateExtraction;
    @Mandatory
    @Key(csvKey = "Periode", batchKey = "period")
    private String period;
}
