package fr.hm.pacifique.bench.projection.top;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APrimeTopCsv extends AbstractTopCsv {
    @Mandatory
    @Key(csvKey = "Societe", batchKey = "societe")
    private String societe;

    @Mandatory
    @Key(csvKey = "Exercice", batchKey = "exercice")
    private Integer exercice;

    @Mandatory
    @Key(csvKey = "Periode", batchKey = "periode")
    private String periode;
}
