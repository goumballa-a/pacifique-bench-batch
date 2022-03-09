package fr.hm.pacifique.bench.batch.job.simulateSatelliteFPrime3;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.MoveTopFileTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.SimulateSatelliteExportTasklet;
import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.projection.top.TopFPrimeCSV;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.batch.AbstractJobConfig;
import fr.hm.pacifique.common.batch.tasklet.ReadTopTasklet;
import fr.hm.pacifique.common.service.CrexFileService;
import fr.hm.pacifique.common.service.FileService;
import fr.hm.pacifique.common.service.ReadTopFileService;
import fr.hm.pacifique.common.service.ReflectionService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static fr.hm.pacifique.common.constant.CommonBatchConstants.ALL;
import static fr.hm.pacifique.common.constant.CommonBatchConstants.COMPLETED;

@Configuration
@EnableBatchProcessing
public class SimulateSatelliteFPrime3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public SimulateSatelliteFPrime3JobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job simulateSatelliteFPrime3() {
        return jobBuilderFactory.get("simulateSatelliteFPrime3")
                .start(initSimulateSatelliteFPrime3Step()).on(COMPLETED).to(moveTopFileFPrime3Step())
                .from(initSimulateSatelliteFPrime3Step()).on(ALL).fail()

                .from(moveTopFileFPrime3Step()).on(COMPLETED).to(readTopFPrime3Step())
                .from(moveTopFileFPrime3Step()).on(ALL).fail()

                .from(readTopFPrime3Step()).on(COMPLETED).to(simulateSatelliteFPrime3Step())
                .from(readTopFPrime3Step()).on(ALL).fail()

                .from(simulateSatelliteFPrime3Step()).on(COMPLETED).end()
                .from(simulateSatelliteFPrime3Step()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteFPrime3Step() {
        return stepBuilderFactory.get("initSimulateSatelliteFPrime3Step")
                .tasklet(initSimulateSatelliteFPrime3Tasklet())
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteFPrime3Tasklet() {
        return new InitTasklet(Application.DENSITY, HalfFlow.F_PRIME3, directoryBenchService, crexFileService);
    }

    @Bean
    public Step moveTopFileFPrime3Step() {
        return this.stepBuilderFactory.get("moveTopFileFPrime3Step")
                .listener(crexListener("Consommation du fichier TOP"))
                .tasklet(moveTopFileTasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public MoveTopFileTasklet moveTopFileTasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            FileService fileService,
            CrexFileService crexFileService,
            DirectoryBenchService directoryBenchService) {
        return new MoveTopFileTasklet(triggerFilename, fileService, crexFileService, directoryBenchService);
    }

    @Bean
    public Step readTopFPrime3Step() {
        return this.stepBuilderFactory.get("readTopFPrime3Step")
                .listener(crexListener("Lecture du fichier TOP"))
                .tasklet(this.readTopFPrime3Tasklet(null, null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public ReadTopTasklet readTopFPrime3Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            CrexFileService crexFileService,
            ReadTopFileService readTopFileService,
            ReflectionService reflectionService,
            DirectoryBenchService directoryBenchService) {
        return new ReadTopTasklet<>(directoryBenchService.getWorkDirectory(AppType.PACIFIQUE), triggerFilename, TopFPrimeCSV.class, readTopFileService, reflectionService, crexFileService);
    }

    @Bean
    public Step simulateSatelliteFPrime3Step() {

        return stepBuilderFactory.get("simulateSatelliteFPrime3Step")
                .tasklet(simulateSatelliteFPrime3Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateSatelliteExportTasklet simulateSatelliteFPrime3Tasklet(
            @Value("#{jobExecutionContext['dateExtraction']}") LocalDateTime dateExtraction,
            @Value("#{jobExecutionContext['period']}") String period,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileService) {
        return new SimulateSatelliteExportTasklet(new Context(dateExtraction, period), directoryBenchService, fileService);
    }
}
