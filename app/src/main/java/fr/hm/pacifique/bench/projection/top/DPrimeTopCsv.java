package fr.hm.pacifique.bench.projection.top;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DPrimeTopCsv extends AbstractTopCsv {
    @Mandatory
    @Key(csvKey = "Année", batchKey = "annee")
    private Integer annee;

    @Mandatory
    @Key(csvKey = "Période", batchKey = "periode")
    private String periode;
}
