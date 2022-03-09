package fr.hm.pacifique.bench.batch.job.launcherFSeconde2;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherFSeconde2.tasklet.LauncherFSeconde2Tasklet;
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
public class LauncherFSeconde2JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherFSeconde2JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                      CrexFileService crexFileService, FileService fileService,
                                      TechnicalErrorService technicalErrorService) {
        super(stepBuilderFactory, crexFileService, fileService, technicalErrorService);
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherFSeconde2() {
        return jobBuilderFactory.get("launcherFSeconde2")
                .start(initLauncherFSeconde2Step()).on(COMPLETED).to(launcherFSeconde2Step())
                .from(initLauncherFSeconde2Step()).on(ALL).fail()

                .from(launcherFSeconde2Step()).on(COMPLETED).end()
                .from(launcherFSeconde2Step()).on(ERROR).end()
                .from(launcherFSeconde2Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherFSeconde2Step() {
        return stepBuilderFactory.get("initLauncherFSeconde2Step")
                .tasklet(initLauncherFSeconde2Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherFSeconde2Tasklet(
            DirectoryService directoryService) {
        return new InitTasklet(Application.RI, HalfFlow.F_SECONDE2, directoryService, crexFileService);
    }

    @Bean
    public Step launcherFSeconde2Step() {
        return stepBuilderFactory.get("launcherFSeconde2Step")
                .tasklet(launcherFSeconde2Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherFSeconde2Tasklet launcherFSeconde2Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryService directoryService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherFSeconde2Tasklet(triggerFilename, directoryService, fileBenchService, benchService);
    }
}
