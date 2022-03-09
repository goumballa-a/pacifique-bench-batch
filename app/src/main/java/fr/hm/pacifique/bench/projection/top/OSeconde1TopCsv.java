package fr.hm.pacifique.bench.projection.top;

import fr.hm.pacifique.common.projection.common.AbstractTopCsv;
import fr.hm.pacifique.common.projection.common.annotation.Key;
import fr.hm.pacifique.common.projection.common.annotation.Mandatory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OSeconde1TopCsv extends AbstractTopCsv {
    @Mandatory
    @Key(csvKey = "Année", batchKey = "year")
    private String year;
    @Mandatory
    @Key(csvKey = "Mois", batchKey = "month")
    private String month;
}
