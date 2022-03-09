package fr.hm.pacifique.bench.batch.job.simulateExportDensity;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.simulateExportDensity.tasklet.SimulateExportDensityTasklet;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.enums.fprime.BlFPrimePacifique;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.projection.fprime.TopFPrimeCSV;
import fr.hm.pacifique.bench.service.DirectoryService;
import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.batch.AbstractJobConfig;
import fr.hm.pacifique.common.batch.tasklet.ReadBlTasklet;
import fr.hm.pacifique.common.batch.tasklet.ReadTopTasklet;
import fr.hm.pacifique.common.service.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static fr.hm.pacifique.common.constant.CommonBatchConstants.ALL;
import static fr.hm.pacifique.common.constant.CommonBatchConstants.COMPLETED;

@Configuration
@EnableBatchProcessing
public class SimulateExportDensityJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryService directoryService;

    public SimulateExportDensityJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                          CrexFileService crexFileService, FileService fileService,
                                          TechnicalErrorService technicalErrorService, DirectoryService directoryService) {
        super(stepBuilderFactory, crexFileService, fileService, technicalErrorService);
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryService = directoryService;
    }

    @Bean
    public Job simulateExportDensity() {
        return jobBuilderFactory.get("simulateExportDensity")
                .start(initSimulateExportDensityStep()).on(COMPLETED).to(moveAndReadFPrimeBlStep())
                .from(initSimulateExportDensityStep()).on(ALL).fail()

                .from(moveAndReadFPrimeBlStep()).on(COMPLETED).to(readTopStep())
                .from(moveAndReadFPrimeBlStep()).on(ALL).fail()

                .from(readTopStep()).on(COMPLETED).to(simulateExportDensityStep())
                .from(readTopStep()).on(ALL).fail()

                .from(simulateExportDensityStep()).on(COMPLETED).end()
                .from(simulateExportDensityStep()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateExportDensityStep() {
        return stepBuilderFactory.get("initSimulateExportDensityStep")
                .tasklet(initSimulateExportDensityTasklet())
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateExportDensityTasklet() {
        return new InitTasklet(Application.DENSITY, HalfFlow.F_PRIME, directoryService, crexFileService);
    }

    @Bean
    public Step moveAndReadFPrimeBlStep() {
        return stepBuilderFactory.get("moveAndReadFPrimeBlStep")
                .listener(crexListener("Lecture du BL"))
                .tasklet(readFPrimeBlTasklet(null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public ReadBlTasklet readFPrimeBlTasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            ReadBlFileService readBlFileService,
            CrexFileService crexFileService) {
        return new ReadBlTasklet(directoryService.getOutDirectory(AppType.PACIFIQUE), directoryService.getWorkDirectory(AppType.PACIFIQUE), triggerFilename, BlFPrimePacifique.values(), readBlFileService, crexFileService);
    }

    @Bean
    public Step readTopStep() {
        return stepBuilderFactory.get("readTopStep")
                .listener(crexListener("Vérification du fichier TOP"))
                .tasklet(readTopTasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public ReadTopTasklet<TopFPrimeCSV> readTopTasklet(
            ReadTopFileService readTopFileService,
            ReadBlFileService readBlFileService,
            ReflectionService reflectionService,
            CrexFileService crexFileService) {
        return new ReadTopTasklet<>(directoryService.getWorkDirectory(AppType.PACIFIQUE), readBlFileService.getFilenameByEnum(BlFPrimePacifique.TOP), TopFPrimeCSV.class, readTopFileService, reflectionService, crexFileService);
    }

    @Bean
    public Step simulateExportDensityStep() {

        return stepBuilderFactory.get("simulateExportDensityStep")
                .tasklet(simulateExportDensityTasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateExportDensityTasklet simulateExportDensityTasklet(
            @Value("#{jobExecutionContext['dateExtraction']}") LocalDateTime dateExtraction,
            @Value("#{jobExecutionContext['period']}") String period,
            DirectoryService directoryService,
            FileBenchService fileService) {
        return new SimulateExportDensityTasklet(new Context(dateExtraction, period), directoryService, fileService);
    }
}
