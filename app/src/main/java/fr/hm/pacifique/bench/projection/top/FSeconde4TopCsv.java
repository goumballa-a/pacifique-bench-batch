package fr.hm.pacifique.bench.projection.top;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FSeconde4TopCsv extends AbstractTopCsv {
    @Mandatory
    @Key(csvKey = "Année reporting", batchKey = "contextYear")
    private String contextYear;
    @Mandatory
    @Key(csvKey = "Periode", batchKey = "periodicity")
    private String periodicity;
}
