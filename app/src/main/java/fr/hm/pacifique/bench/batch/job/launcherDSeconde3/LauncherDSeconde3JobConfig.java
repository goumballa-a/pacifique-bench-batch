package fr.hm.pacifique.bench.batch.job.launcherDSeconde3;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherDSeconde3.tasklet.LauncherDSeconde3Tasklet;
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
public class LauncherDSeconde3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherDSeconde3JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherDSeconde3() {
        return jobBuilderFactory.get("launcherDSeconde3")
                .start(initLauncherDSeconde3Step()).on(COMPLETED).to(launcherDSeconde3Step())
                .from(initLauncherDSeconde3Step()).on(ALL).fail()

                .from(launcherDSeconde3Step()).on(COMPLETED).end()
                .from(launcherDSeconde3Step()).on(ERROR).end()
                .from(launcherDSeconde3Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherDSeconde3Step() {
        return stepBuilderFactory.get("initLauncherDSeconde3Step")
                .tasklet(initLauncherDSeconde3Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherDSeconde3Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.VEGA, HalfFlow.D_SECONDE3, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherDSeconde3Step() {
        return stepBuilderFactory.get("launcherDSeconde3Step")
                .tasklet(launcherDSeconde3Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherDSeconde3Tasklet launcherDSeconde3Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherDSeconde3Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
