package fr.hm.pacifique.bench.batch.job.launcherQSeconde3;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherQSeconde3.tasklet.LauncherQSeconde3Tasklet;
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
public class LauncherQSeconde3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherQSeconde3JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherQSeconde3() {
        return jobBuilderFactory.get("launcherQSeconde3")
                .start(initLauncherQSeconde3Step()).on(COMPLETED).to(launcherQSeconde3Step())
                .from(initLauncherQSeconde3Step()).on(ALL).fail()

                .from(launcherQSeconde3Step()).on(COMPLETED).end()
                .from(launcherQSeconde3Step()).on(ERROR).end()
                .from(launcherQSeconde3Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherQSeconde3Step() {
        return stepBuilderFactory.get("initLauncherQSeconde3Step")
                .tasklet(initLauncherQSeconde3Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherQSeconde3Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_SECONDE3, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherQSeconde3Step() {
        return stepBuilderFactory.get("launcherQSeconde3Step")
                .tasklet(launcherQSeconde3Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherQSeconde3Tasklet launcherQSeconde3Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherQSeconde3Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
