package fr.hm.pacifique.bench.batch.job.simulateSatelliteQPrime1;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.MoveTopFileTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.SimulateSatelliteExportTasklet;
import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.projection.top.QPrimeTopCsv;
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
public class SimulateSatelliteQPrime1JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public SimulateSatelliteQPrime1JobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job simulateSatelliteQPrime1() {
        return jobBuilderFactory.get("simulateSatelliteQPrime1")
                .start(initSimulateSatelliteQPrime1Step()).on(COMPLETED).to(moveTopFileQPrime1Step())
                .from(initSimulateSatelliteQPrime1Step()).on(ALL).fail()

                .from(moveTopFileQPrime1Step()).on(COMPLETED).to(readTopQPrime1Step())
                .from(moveTopFileQPrime1Step()).on(ALL).fail()

                .from(readTopQPrime1Step()).on(COMPLETED).to(simulateSatelliteQPrime1Step())
                .from(readTopQPrime1Step()).on(ALL).fail()

                .from(simulateSatelliteQPrime1Step()).on(COMPLETED).end()
                .from(simulateSatelliteQPrime1Step()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteQPrime1Step() {
        return stepBuilderFactory.get("initSimulateSatelliteQPrime1Step")
                .tasklet(initSimulateSatelliteQPrime1Tasklet())
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteQPrime1Tasklet() {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_PRIME1, directoryBenchService, crexFileService);
    }

    @Bean
    public Step moveTopFileQPrime1Step() {
        return this.stepBuilderFactory.get("moveTopFileQPrime1Step")
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
    public Step readTopQPrime1Step() {
        return this.stepBuilderFactory.get("readTopQPrime1Step")
                .listener(crexListener("Lecture du fichier TOP"))
                .tasklet(this.readTopQPrime1Tasklet(null, null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public ReadTopTasklet readTopQPrime1Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            CrexFileService crexFileService,
            ReadTopFileService readTopFileService,
            ReflectionService reflectionService,
            DirectoryBenchService directoryBenchService) {
        return new ReadTopTasklet<>(directoryBenchService.getWorkDirectory(AppType.PACIFIQUE), triggerFilename, QPrimeTopCsv.class, readTopFileService, reflectionService, crexFileService);
    }

    @Bean
    public Step simulateSatelliteQPrime1Step() {

        return stepBuilderFactory.get("simulateSatelliteQPrime1Step")
                .tasklet(simulateSatelliteQPrime1Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateSatelliteExportTasklet simulateSatelliteQPrime1Tasklet(
            @Value("#{jobExecutionContext['dateExtraction']}") LocalDateTime exercice,
            @Value("#{jobExecutionContext['period']}") String period,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileService) {
        return new SimulateSatelliteExportTasklet(new Context(exercice, period), directoryBenchService, fileService);
    }
}
