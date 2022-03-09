package fr.hm.pacifique.bench.projection.top;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QPrime5TopCsv extends AbstractTopCsv {

    @Mandatory
    @Key(csvKey = "Societe", batchKey = "societe")
    private String societe;

    @Mandatory
    @Key(csvKey = "Exercice debut", batchKey = "exerciceDebut")
    private Integer exerciceDebut;

    @Mandatory
    @Key(csvKey = "Exercice fin", batchKey = "exerciceFin")
    private Integer exerciceFin;

    @Mandatory
    @Key(csvKey = "Periode debut", batchKey = "periodeDebut")
    private String periodeDebut;

    @Mandatory
    @Key(csvKey = "Periode fin", batchKey = "periodeFin")
    private String periodeFin;
}