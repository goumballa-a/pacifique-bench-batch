package fr.hm.pacifique.bench.batch.job.launcherQPrime5;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherQPrime5.tasklet.LauncherQPrime5Tasklet;
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
public class LauncherQPrime5JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherQPrime5JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherQPrime5() {
        return jobBuilderFactory.get("launcherQPrime5")
                .start(initLauncherQPrime5Step()).on(COMPLETED).to(launcherQPrime5Step())
                .from(initLauncherQPrime5Step()).on(ALL).fail()

                .from(launcherQPrime5Step()).on(COMPLETED).end()
                .from(launcherQPrime5Step()).on(ERROR).end()
                .from(launcherQPrime5Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherQPrime5Step() {
        return stepBuilderFactory.get("initLauncherQPrime5Step")
                .tasklet(initLauncherQPrime5Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherQPrime5Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.CODA, HalfFlow.Q_PRIME5, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherQPrime5Step() {
        return stepBuilderFactory.get("launcherQPrime5Step")
                .tasklet(launcherQPrime5Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherQPrime5Tasklet launcherQPrime5Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherQPrime5Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
