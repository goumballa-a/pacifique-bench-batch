package fr.hm.pacifique.bench.batch.job.launcherDPrime;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherDPrime.tasklet.LauncherDPrimeTasklet;
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
public class LauncherDPrimeJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherDPrimeJobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherDPrime() {
        return jobBuilderFactory.get("launcherDPrime")
                .start(initLauncherDPrimeStep()).on(COMPLETED).to(launcherDPrimeStep())
                .from(initLauncherDPrimeStep()).on(ALL).fail()

                .from(launcherDPrimeStep()).on(COMPLETED).end()
                .from(launcherDPrimeStep()).on(ERROR).end()
                .from(launcherDPrimeStep()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherDPrimeStep() {
        return stepBuilderFactory.get("initLauncherDPrimeStep")
                .tasklet(initLauncherDPrimeTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherDPrimeTasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.SAPFC, HalfFlow.D_PRIME, directoryBenchService, crexFileService);
    }

    @Bean
    public Step launcherDPrimeStep() {
        return stepBuilderFactory.get("launcherDPrimeStep")
                .tasklet(launcherDPrimeTasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherDPrimeTasklet launcherDPrimeTasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherDPrimeTasklet(triggerFilename, directoryBenchService, fileBenchService, benchService);
    }
}
