package fr.hm.pacifique.bench.batch.job.simulateImportSapfc;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.simulateImportSapfc.tasklet.SimulateImportSapfcTasklet;
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
public class SimulateImportSapfcJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public SimulateImportSapfcJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                        CrexFileService crexFileService, FileService fileService,
                                        TechnicalErrorService technicalErrorService) {
        super(stepBuilderFactory, crexFileService, fileService, technicalErrorService);
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job simulateImportSapfc() {
        return jobBuilderFactory.get("simulateImportSapfc")
                .start(initSimulateImportSapfcStep()).on(COMPLETED).to(simulateImportSapfcStep())
                .from(initSimulateImportSapfcStep()).on(ALL).fail()

                .from(simulateImportSapfcStep()).on(COMPLETED).end()
                .from(simulateImportSapfcStep()).on(ERROR).end()
                .from(simulateImportSapfcStep()).on(ALL).end()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateImportSapfcStep() {
        return stepBuilderFactory.get("initSimulateImportSapfcStep")
                .tasklet(initSimulateImportSapfcTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateImportSapfcTasklet(
            DirectoryService directoryService) {
        return new InitTasklet(Application.SAPFC, HalfFlow.F_SECONDE1, directoryService, crexFileService);
    }

    /**
     * Consume BL file and move input files into work directory
     */
    @Bean
    public Step simulateImportSapfcStep() {
        return stepBuilderFactory.get("simulateImportSapfcStep")
                .tasklet(simulateImportSapfcTasklet(null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateImportSapfcTasklet simulateImportSapfcTasklet(
            DirectoryService directoryService,
            FileBenchService fileBenchService) {
        return new SimulateImportSapfcTasklet(directoryService, fileBenchService);
    }
}
