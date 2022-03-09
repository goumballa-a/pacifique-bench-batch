package fr.hm.pacifique.bench.batch.job.launcherFSeconde1;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.launcherFSeconde1.tasklet.LauncherFSeconde1Tasklet;
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
public class LauncherFSeconde1JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public LauncherFSeconde1JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                      CrexFileService crexFileService, FileService fileService,
                                      TechnicalErrorService technicalErrorService) {
        super(stepBuilderFactory, crexFileService, fileService, technicalErrorService);
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job launcherFSeconde1() {
        return jobBuilderFactory.get("launcherFSeconde1")
                .start(initLauncherFSeconde1Step()).on(COMPLETED).to(launcherFSeconde1Step())
                .from(initLauncherFSeconde1Step()).on(ALL).fail()

                .from(launcherFSeconde1Step()).on(COMPLETED).end()
                .from(launcherFSeconde1Step()).on(ERROR).end()
                .from(launcherFSeconde1Step()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initLauncherFSeconde1Step() {
        return stepBuilderFactory.get("initLauncherFSeconde1Step")
                .tasklet(initLauncherFSeconde1Tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherFSeconde1Tasklet(
            DirectoryService directoryService) {
        return new InitTasklet(Application.SAPFC, HalfFlow.F_SECONDE1, directoryService, crexFileService);
    }

    @Bean
    public Step launcherFSeconde1Step() {
        return stepBuilderFactory.get("launcherFSeconde1Step")
                .tasklet(launcherFSeconde1Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public LauncherFSeconde1Tasklet launcherFSeconde1Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            DirectoryService directoryService,
            FileBenchService fileBenchService,
            BenchService benchService) {
        return new LauncherFSeconde1Tasklet(triggerFilename, directoryService, fileBenchService, benchService);
    }
}
