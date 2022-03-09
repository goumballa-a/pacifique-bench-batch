package fr.hm.pacifique.bench.projection.top;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OPrimeTopCsv extends AbstractTopCsv {
    @Mandatory
    @Key(csvKey = "Année", batchKey = "year")
    private Integer year;
    @Mandatory
    @Key(csvKey = "Mois", batchKey = "month")
    private String month;
}
