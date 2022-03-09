package fr.hm.pacifique.bench.batch.job.simulateSatelliteQPrime2;


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
public class SimulateSatelliteQPrime2JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public SimulateSatelliteQPrime2JobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job simulateSatelliteQPrime2() {
        return jobBuilderFactory.get("simulateSatelliteQPrime2")
                .start(initSimulateSatelliteQPrime2Step()).on(COMPLETED).to(moveTopFileQPrime2Step())
                .from(initSimulateSatelliteQPrime2Step()).on(ALL).fail()

                .from(moveTopFileQPrime2Step()).on(COMPLETED).to(readTopQPrime2Step())
                .from(moveTopFileQPrime2Step()).on(ALL).fail()

                .from(readTopQPrime2Step()).on(COMPLETED).to(simulateSatelliteQPrime2Step())
                .from(readTopQPrime2Step()).on(ALL).fail()

                .from(simulateSatelliteQPrime2Step()).on(COMPLETED).end()
                .from(simulateSatelliteQPrime2Step()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteQPrime2Step() {
        return stepBuilderFactory.get("initSimulateSatelliteQPrime2Step")
                .tasklet(initSimulateSatelliteQPrime2Tasklet())
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteQPrime2Tasklet() {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_PRIME2, directoryBenchService, crexFileService);
    }

    @Bean
    public Step moveTopFileQPrime2Step() {
        return this.stepBuilderFactory.get("moveTopFileQPrime2Step")
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
    public Step readTopQPrime2Step() {
        return this.stepBuilderFactory.get("readTopQPrime2Step")
                .listener(crexListener("Lecture du fichier TOP"))
                .tasklet(this.readTopQPrime2Tasklet(null, null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public ReadTopTasklet readTopQPrime2Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            CrexFileService crexFileService,
            ReadTopFileService readTopFileService,
            ReflectionService reflectionService,
            DirectoryBenchService directoryBenchService) {
        return new ReadTopTasklet<>(directoryBenchService.getWorkDirectory(AppType.PACIFIQUE), triggerFilename, QPrimeTopCsv.class, readTopFileService, reflectionService, crexFileService);
    }

    @Bean
    public Step simulateSatelliteQPrime2Step() {

        return stepBuilderFactory.get("simulateSatelliteQPrime2Step")
                .tasklet(simulateSatelliteQPrime2Tasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateSatelliteExportTasklet simulateSatelliteQPrime2Tasklet(
            @Value("#{jobExecutionContext['dateExtraction']}") LocalDateTime exercice,
            @Value("#{jobExecutionContext['period']}") String period,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileService) {
        return new SimulateSatelliteExportTasklet(new Context(exercice, period), directoryBenchService, fileService);
    }
}
