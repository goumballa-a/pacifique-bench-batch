package fr.hm.pacifique.bench.batch.job.launcherDSeconde2;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherDSeconde2.tasklet.LauncherDSeconde2Tasklet;
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
public class LauncherDSeconde2JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherDSeconde2JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherDSeconde2() {
        return jobBuilderFactory.get("launcherDSeconde2")
                .start(initLauncherDSeconde2Step()).on(COMPLETED).to(launcherDSeconde2Step())
                .from(initLauncherDSeconde2Step()).on(ALL).fail()

                .from(launcherDSeconde2Step()).on(COMPLETED).end()
                .from(launcherDSeconde2Step()).on(ERROR).end()
                .from(launcherDSeconde2Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherDSeconde2Step() {
        return stepBuilderFactory.get("initLauncherDSeconde2Step")
                .tasklet(initLauncherDSeconde2Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherDSeconde2Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.VEGA, HalfFlow.D_SECONDE2, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherDSeconde2Step() {
        return stepBuilderFactory.get("launcherDSeconde2Step")
                .tasklet(launcherDSeconde2Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherDSeconde2Tasklet launcherDSeconde2Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherDSeconde2Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
