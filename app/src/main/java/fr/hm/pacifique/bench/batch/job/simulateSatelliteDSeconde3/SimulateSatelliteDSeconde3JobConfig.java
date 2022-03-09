package fr.hm.pacifique.bench.batch.job.simulateSatelliteDSeconde3;


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
public class SimulateSatelliteDSeconde3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public SimulateSatelliteDSeconde3JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job simulateSatelliteDSeconde3() {
        return jobBuilderFactory.get("simulateSatelliteDSeconde3")
                .start(initSimulateSatelliteDSeconde3Step()).on(COMPLETED).end()
                .from(initSimulateSatelliteDSeconde3Step()).on(ERROR).end()
                .from(initSimulateSatelliteDSeconde3Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteDSeconde3Step() {
        return stepBuilderFactory.get("initSimulateSatelliteDSeconde3Step")
                .tasklet(initSimulateSatelliteDSeconde3Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteDSeconde3Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.VEGA, HalfFlow.D_SECONDE3, directoryBenchService, crexFileService);
    }

}
