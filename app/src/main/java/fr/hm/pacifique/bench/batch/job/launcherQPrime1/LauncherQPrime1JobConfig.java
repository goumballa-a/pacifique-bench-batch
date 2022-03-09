package fr.hm.pacifique.bench.batch.job.launcherQPrime1;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherQPrime1.tasklet.LauncherQPrime1Tasklet;
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
public class LauncherQPrime1JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherQPrime1JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherQPrime1() {
        return jobBuilderFactory.get("launcherQPrime1")
                .start(initLauncherQPrime1Step()).on(COMPLETED).to(launcherQPrime1Step())
                .from(initLauncherQPrime1Step()).on(ALL).fail()

                .from(launcherQPrime1Step()).on(COMPLETED).end()
                .from(launcherQPrime1Step()).on(ERROR).end()
                .from(launcherQPrime1Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherQPrime1Step() {
        return stepBuilderFactory.get("initLauncherQPrime1Step")
                .tasklet(initLauncherQPrime1Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherQPrime1Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_PRIME1, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherQPrime1Step() {
        return stepBuilderFactory.get("launcherQPrime1Step")
                .tasklet(launcherQPrime1Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherQPrime1Tasklet launcherQPrime1Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherQPrime1Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
