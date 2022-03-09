package fr.hm.pacifique.bench.batch.job.launcherQPrime2;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherQPrime2.tasklet.LauncherQPrime2Tasklet;
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
public class LauncherQPrime2JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherQPrime2JobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherQPrime2() {
        return jobBuilderFactory.get("launcherQPrime2")
                .start(initLauncherQPrime2Step()).on(COMPLETED).to(launcherQPrime2Step())
                .from(initLauncherQPrime2Step()).on(ALL).fail()

                .from(launcherQPrime2Step()).on(COMPLETED).end()
                .from(launcherQPrime2Step()).on(ERROR).end()
                .from(launcherQPrime2Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherQPrime2Step() {
        return stepBuilderFactory.get("initLauncherQPrime2Step")
                .tasklet(initLauncherQPrime2Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherQPrime2Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_PRIME2, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherQPrime2Step() {
        return stepBuilderFactory.get("launcherQPrime2Step")
                .tasklet(launcherQPrime2Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherQPrime2Tasklet launcherQPrime2Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherQPrime2Tasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
