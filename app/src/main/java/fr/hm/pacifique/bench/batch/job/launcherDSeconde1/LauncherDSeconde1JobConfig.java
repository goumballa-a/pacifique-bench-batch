package fr.hm.pacifique.bench.batch.job.launcherDSeconde1;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherDSeconde1.tasklet.LauncherDSeconde1Tasklet;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.service.BenchService;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.batch.AbstractJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fr.hm.pacifique.common.constant.CommonBatchConstants.*;

@Configuration
@EnableBatchProcessing
public class LauncherDSeconde1JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherDSeconde1JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherDSeconde1() {
        return jobBuilderFactory.get("launcherDSeconde1")
                .start(initLauncherDSeconde1Step()).on(COMPLETED).to(launcherDSeconde1Step())
                .from(initLauncherDSeconde1Step()).on(ALL).fail()

                .from(launcherDSeconde1Step()).on(COMPLETED).end()
                .from(launcherDSeconde1Step()).on(ERROR).end()
                .from(launcherDSeconde1Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherDSeconde1Step() {
        return stepBuilderFactory.get("initLauncherDSeconde1Step")
                .tasklet(initLauncherDSeconde1Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherDSeconde1Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.VEGA, HalfFlow.D_SECONDE1, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherDSeconde1Step() {
        return stepBuilderFactory.get("launcherDSeconde1Step")
                .tasklet(launcherDSeconde1Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherDSeconde1Tasklet launcherDSeconde1Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherDSeconde1Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
