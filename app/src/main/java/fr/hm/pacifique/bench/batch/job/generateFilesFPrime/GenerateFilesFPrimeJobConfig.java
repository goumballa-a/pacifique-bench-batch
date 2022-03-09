package fr.hm.pacifique.bench.batch.job.generateFilesFPrime;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.generateFilesFPrime.tasklet.ChangeTopTasklet;
import fr.hm.pacifique.bench.batch.job.generateFilesFPrime.tasklet.GenerateFilesFPrimeTasklet;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
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

import static fr.hm.pacifique.common.constant.CommonBatchConstants.ALL;
import static fr.hm.pacifique.common.constant.CommonBatchConstants.COMPLETED;

@Configuration
@EnableBatchProcessing
public class GenerateFilesFPrimeJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryService directoryService;

    public GenerateFilesFPrimeJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                        CrexFileService crexFileService, FileService fileService,
                                        TechnicalErrorService technicalErrorService, DirectoryService directoryService) {
        super(stepBuilderFactory, crexFileService, fileService, technicalErrorService);
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryService = directoryService;
    }

    @Bean
    public Job generateFilesFPrime() {
        return jobBuilderFactory.get("generateFilesFPrime")
                .start(initLauncherFPrimeStep()).on(COMPLETED).to(generateFilesFPrimeStep())
                .from(initLauncherFPrimeStep()).on(ALL).fail()

                .from(generateFilesFPrimeStep()).on(COMPLETED).to(changeTopStep())
                .from(generateFilesFPrimeStep()).on(ALL).fail()

                .from(changeTopStep()).on(COMPLETED).end()
                .from(changeTopStep()).on(ALL).fail()

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
    public Step generateFilesFPrimeStep() {
        return stepBuilderFactory.get("moveAndReadFPrimeBlExtractorStep")
                .tasklet(generateFilesFPrimeTasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public GenerateFilesFPrimeTasklet generateFilesFPrimeTasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            FileBenchService fileService) {
        return new GenerateFilesFPrimeTasklet(triggerFilename, directoryService, fileService);
    }

    @Bean
    public Step changeTopStep() {
        return stepBuilderFactory.get("changeTopStep")
                .tasklet(changeTopTasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public ChangeTopTasklet changeTopTasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            FileBenchService fileService) {
        return new ChangeTopTasklet(triggerFilename, directoryService, fileService);
    }
}
