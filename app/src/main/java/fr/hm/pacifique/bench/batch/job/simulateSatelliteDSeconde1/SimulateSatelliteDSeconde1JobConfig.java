package fr.hm.pacifique.bench.batch.job.simulateSatelliteDSeconde1;


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
public class SimulateSatelliteDSeconde1JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public SimulateSatelliteDSeconde1JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job simulateSatelliteDSeconde1() {
        return jobBuilderFactory.get("simulateSatelliteDSeconde1")
                .start(initSimulateSatelliteDSeconde1Step()).on(COMPLETED).end()
                .from(initSimulateSatelliteDSeconde1Step()).on(ERROR).end()
                .from(initSimulateSatelliteDSeconde1Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteDSeconde1Step() {
        return stepBuilderFactory.get("initSimulateSatelliteDSeconde1Step")
                .tasklet(initSimulateSatelliteDSeconde1Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteDSeconde1Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.VEGA, HalfFlow.D_SECONDE1, directoryBenchService, crexFileService);
    }

}
