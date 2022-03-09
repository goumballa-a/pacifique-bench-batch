package fr.hm.pacifique.bench.batch.job.generateFilesFPrime.processor;

import fr.hm.pacifique.common.batch.processor.SimpleConvertProcessor;
import fr.hm.pacifique.common.projection.common.AbstractCsv;
import fr.hm.pacifique.common.service.ConverterService;

public class BenchSimpleConverterProcessor<S, D extends AbstractCsv> extends SimpleConvertProcessor<S, D> {

    public BenchSimpleConverterProcessor(Class<D> destinationClass, ConverterService converterService) {
        super(destinationClass, converterService);
    }

    @Override
    public D process(S item) {
        D target = super.process(item);
        return target;
    }
}
