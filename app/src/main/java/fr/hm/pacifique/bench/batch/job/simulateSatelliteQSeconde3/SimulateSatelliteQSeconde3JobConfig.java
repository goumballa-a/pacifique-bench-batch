package fr.hm.pacifique.bench.batch.job.simulateSatelliteQSeconde3;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.common.batch.AbstractJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fr.hm.pacifique.common.constant.CommonBatchConstants.*;

@Configuration
@EnableBatchProcessing
public class SimulateSatelliteQSeconde3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public SimulateSatelliteQSeconde3JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job simulateSatelliteQSeconde3() {
        return jobBuilderFactory.get("simulateSatelliteQSeconde3")
                .start(initSimulateSatelliteQSeconde3Step()).on(COMPLETED).end()
                .from(initSimulateSatelliteQSeconde3Step()).on(ERROR).end()
                .from(initSimulateSatelliteQSeconde3Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteQSeconde3Step() {
        return stepBuilderFactory.get("initSimulateSatelliteQSeconde3Step")
                .tasklet(initSimulateSatelliteQSeconde3Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteQSeconde3Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_SECONDE3, directoryBenchService, crexFileService);
    }

}
