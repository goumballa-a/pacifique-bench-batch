package fr.hm.pacifique.bench.batch.job.generateFilesPrime;


import fr.hm.pacifique.bench.batch.common.tasklet.ChangeTopTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.GenerateFilesTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
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

import static fr.hm.pacifique.common.constant.CommonBatchConstants.ALL;
import static fr.hm.pacifique.common.constant.CommonBatchConstants.COMPLETED;

@Configuration
@EnableBatchProcessing
public class GenerateFilesFPrime3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public GenerateFilesFPrime3JobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job generateFilesFPrime3() {
        return jobBuilderFactory.get("generateFilesFPrime3")
                .start(initLauncherFPrime3Step()).on(COMPLETED).to(generateFilesFPrime3Step())
                .from(initLauncherFPrime3Step()).on(ALL).fail()

                .from(generateFilesFPrime3Step()).on(COMPLETED).to(changeTopFPrime3Step())
                .from(generateFilesFPrime3Step()).on(ALL).fail()

                .from(changeTopFPrime3Step()).on(COMPLETED).end()
                .from(changeTopFPrime3Step()).on(ALL).fail()

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
    public Step generateFilesFPrime3Step() {
        return stepBuilderFactory.get("moveAndReadFPrime3BlExtractorStep")
                .tasklet(generateFilesFPrime3Tasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public GenerateFilesTasklet generateFilesFPrime3Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            FileBenchService fileService) {
        return new GenerateFilesTasklet(triggerFilename, directoryBenchService, fileService);
    }

    @Bean
    public Step changeTopFPrime3Step() {
        return stepBuilderFactory.get("changeTopFPrime3Step")
                .tasklet(changeTopFPrime3Tasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public ChangeTopTasklet changeTopFPrime3Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            FileBenchService fileService) {
        return new ChangeTopTasklet(triggerFilename, directoryBenchService, fileService);
    }
}
