package fr.hm.pacifique.bench.batch.job.simulateImportRi;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.simulateImportRi.tasklet.SimulateImportRiTasklet;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fr.hm.pacifique.common.constant.CommonBatchConstants.*;

@Configuration
@EnableBatchProcessing
public class SimulateImportRiJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public SimulateImportRiJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                     CrexFileService crexFileService, FileService fileService,
                                     TechnicalErrorService technicalErrorService) {
        super(stepBuilderFactory, crexFileService, fileService, technicalErrorService);
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job simulateImportRi() {
        return jobBuilderFactory.get("simulateImportRi")
                .start(initSimulateImportRiStep()).on(COMPLETED).to(simulateImportRiStep())
                .from(initSimulateImportRiStep()).on(ALL).fail()

                .from(simulateImportRiStep()).on(COMPLETED).end()
                .from(simulateImportRiStep()).on(ERROR).end()
                .from(simulateImportRiStep()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateImportRiStep() {
        return stepBuilderFactory.get("initSimulateImportRiStep")
                .tasklet(initSimulateImportRiTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateImportRiTasklet(
            DirectoryService directoryService) {
        return new InitTasklet(Application.RI, HalfFlow.F_SECONDE2, directoryService, crexFileService);
    }

    /**
     * Consume BL file and move input files into work directory
     */
    @Bean
    public Step simulateImportRiStep() {
        return stepBuilderFactory.get("simulateImportRiStep")
                .tasklet(simulateImportRiTasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateImportRiTasklet simulateImportRiTasklet(
            DirectoryService directoryService,
            FileBenchService fileBenchService) {
        return new SimulateImportRiTasklet(directoryService, fileBenchService);
    }
}
