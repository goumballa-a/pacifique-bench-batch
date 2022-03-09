package fr.hm.pacifique.bench.batch.job.launcherQPrime3;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherQPrime3.tasklet.LauncherQPrime3Tasklet;
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
public class LauncherQPrime3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherQPrime3JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherQPrime3() {
        return jobBuilderFactory.get("launcherQPrime3")
                .start(initLauncherQPrime3Step()).on(COMPLETED).to(launcherQPrime3Step())
                .from(initLauncherQPrime3Step()).on(ALL).fail()

                .from(launcherQPrime3Step()).on(COMPLETED).end()
                .from(launcherQPrime3Step()).on(ERROR).end()
                .from(launcherQPrime3Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherQPrime3Step() {
        return stepBuilderFactory.get("initLauncherQPrime3Step")
                .tasklet(initLauncherQPrime3Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherQPrime3Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_PRIME3, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherQPrime3Step() {
        return stepBuilderFactory.get("launcherQPrime3Step")
                .tasklet(launcherQPrime3Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherQPrime3Tasklet launcherQPrime3Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherQPrime3Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
