package fr.hm.pacifique.bench.batch.job.simulateSatelliteQPrime3;


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

import static fr.hm.pacifique.common.constant.CommonBatchConstants.ALL;
import static fr.hm.pacifique.common.constant.CommonBatchConstants.COMPLETED;

@Configuration
@EnableBatchProcessing
public class SimulateSatelliteQPrime3JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public SimulateSatelliteQPrime3JobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job simulateSatelliteQPrime3() {
        return jobBuilderFactory.get("simulateSatelliteQPrime3")
                .start(initSimulateSatelliteQPrime3Step()).on(COMPLETED).to(moveTopFileQPrime3Step())
                .from(initSimulateSatelliteQPrime3Step()).on(ALL).fail()

                .from(moveTopFileQPrime3Step()).on(COMPLETED).end()
                .from(moveTopFileQPrime3Step()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteQPrime3Step() {
        return stepBuilderFactory.get("initSimulateSatelliteQPrime3Step")
                .tasklet(initSimulateSatelliteQPrime3Tasklet())
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteQPrime3Tasklet() {
        return new InitTasklet(Application.DENSITY, HalfFlow.Q_PRIME2, directoryBenchService, crexFileService);
    }

    @Bean
    public Step moveTopFileQPrime3Step() {
        return this.stepBuilderFactory.get("moveTopFileQPrime3Step")
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
}
