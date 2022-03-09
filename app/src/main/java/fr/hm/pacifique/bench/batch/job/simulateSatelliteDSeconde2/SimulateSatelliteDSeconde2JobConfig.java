package fr.hm.pacifique.bench.batch.job.simulateSatelliteDSeconde2;


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
public class SimulateSatelliteDSeconde2JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public SimulateSatelliteDSeconde2JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job simulateSatelliteDSeconde2() {
        return jobBuilderFactory.get("simulateSatelliteDSeconde2")
                .start(initSimulateSatelliteDSeconde2Step()).on(COMPLETED).end()
                .from(initSimulateSatelliteDSeconde2Step()).on(ERROR).end()
                .from(initSimulateSatelliteDSeconde2Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteDSeconde2Step() {
        return stepBuilderFactory.get("initSimulateSatelliteDSeconde2Step")
                .tasklet(initSimulateSatelliteDSeconde2Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteDSeconde2Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.VEGA, HalfFlow.D_SECONDE2, directoryBenchService, crexFileService);
    }

}
