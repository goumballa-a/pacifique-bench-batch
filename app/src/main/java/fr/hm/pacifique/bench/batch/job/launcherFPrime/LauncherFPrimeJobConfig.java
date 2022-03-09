package fr.hm.pacifique.bench.batch.job.launcherFPrime;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherFPrime.tasklet.LauncherFPrimeTasklet;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.service.BenchService;
import fr.hm.pacifique.bench.service.DirectoryService;
import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.batch.AbstractJobConfig;
import fr.hm.pacifique.common.service.CrexFileService;
import fr.hm.pacifique.common.service.FileService;
import fr.hm.pacifique.common.service.TechnicalErrorService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fr.hm.pacifique.common.constant.CommonBatchConstants.*;

@Configuration
@EnableBatchProcessing
public class LauncherFPrimeJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherFPrimeJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                   CrexFileService crexFileService, FileService fileService,
                                   TechnicalErrorService technicalErrorService) {
        super(stepBuilderFactory, crexFileService, fileService, technicalErrorService);
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherFPrime() {
        return jobBuilderFactory.get("launcherFPrime")
                .start(initLauncherFPrimeStep()).on(COMPLETED).to(launcherFPrimeStep())
                .from(initLauncherFPrimeStep()).on(ALL).fail()

                .from(launcherFPrimeStep()).on(COMPLETED).end()
                .from(launcherFPrimeStep()).on(ERROR).end()
                .from(launcherFPrimeStep()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherFPrimeStep() {
        return stepBuilderFactory.get("initLauncherFPrimeStep")
                .tasklet(initLauncherFPrimeTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherFPrimeTasklet(
            DirectoryService directoryService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.F_PRIME, directoryService, crexFileService);
    }

    @Bean
    public Step launcherFPrimeStep() {
        return stepBuilderFactory.get("launcherFPrimeStep")
                .tasklet(launcherFPrimeTasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherFPrimeTasklet launcherFPrimeTasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryService directoryService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherFPrimeTasklet(triggerFilename, directoryService, fileBenchService, benchService);
    }
}
