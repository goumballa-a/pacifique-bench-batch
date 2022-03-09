package fr.hm.pacifique.bench.batch.job.launcherFPrime3;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherFPrime3.tasklet.LauncherFPrime3Tasklet;
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
public class LauncherFPrime3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherFPrime3JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherFPrime3() {
        return jobBuilderFactory.get("launcherFPrime3")
                .start(initLauncherFPrime3Step()).on(COMPLETED).to(launcherFPrime3Step())
                .from(initLauncherFPrime3Step()).on(ALL).fail()

                .from(launcherFPrime3Step()).on(COMPLETED).end()
                .from(launcherFPrime3Step()).on(ERROR).end()
                .from(launcherFPrime3Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherFPrime3Step() {
        return stepBuilderFactory.get("initLauncherFPrime3Step")
                .tasklet(initLauncherFPrime3Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherFPrime3Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.F_PRIME3, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherFPrime3Step() {
        return stepBuilderFactory.get("launcherFPrime3Step")
                .tasklet(launcherFPrime3Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherFPrime3Tasklet launcherFPrime3Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherFPrime3Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
