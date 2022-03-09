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
public class GenerateFilesQPrime4JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public GenerateFilesQPrime4JobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job generateFilesQPrime4() {
        return jobBuilderFactory.get("generateFilesQPrime4")
                .start(initLauncherQPrime4Step()).on(COMPLETED).to(generateFilesQPrime4Step())
                .from(initLauncherQPrime4Step()).on(ALL).fail()

                .from(generateFilesQPrime4Step()).on(COMPLETED).to(changeTopQPrime4Step())
                .from(generateFilesQPrime4Step()).on(ALL).fail()

                .from(changeTopQPrime4Step()).on(COMPLETED).end()
                .from(changeTopQPrime4Step()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherQPrime4Step() {
        return stepBuilderFactory.get("initLauncherQPrime4Step")
                .tasklet(initLauncherQPrime4Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherQPrime4Tasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_PRIME4, directoryBenchService, crexFileService);
    }

    @Bean
    public Step generateFilesQPrime4Step() {
        return stepBuilderFactory.get("moveAndReadQPrime4BlExtractorStep")
                .tasklet(generateFilesQPrime4Tasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public GenerateFilesTasklet generateFilesQPrime4Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            FileBenchService fileService) {
        return new GenerateFilesTasklet(triggerFilename, directoryBenchService, fileService);
    }

    @Bean
    public Step changeTopQPrime4Step() {
        return stepBuilderFactory.get("changeTopQPrime4Step")
                .tasklet(changeTopQPrime4Tasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public ChangeTopTasklet changeTopQPrime4Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            FileBenchService fileService) {
        return new ChangeTopTasklet(triggerFilename, directoryBenchService, fileService);
    }
}
