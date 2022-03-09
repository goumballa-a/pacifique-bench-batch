package fr.hm.pacifique.bench.batch.job.simulateSatelliteDPrime;


import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.MoveTopFileTasklet;
import fr.hm.pacifique.bench.batch.common.tasklet.SimulateSatelliteExportTasklet;
import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.projection.top.DPrimeTopCsv;
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
public class SimulateSatelliteDPrimeJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public SimulateSatelliteDPrimeJobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job simulateSatelliteDPrime() {
        return jobBuilderFactory.get("simulateSatelliteDPrime")
                .start(initSimulateSatelliteDPrimeStep()).on(COMPLETED).to(moveTopFileDPrimeStep())
                .from(initSimulateSatelliteDPrimeStep()).on(ALL).fail()

                .from(moveTopFileDPrimeStep()).on(COMPLETED).to(readTopDPrimeStep())
                .from(moveTopFileDPrimeStep()).on(ALL).fail()

                .from(readTopDPrimeStep()).on(COMPLETED).to(simulateSatelliteDPrimeStep())
                .from(readTopDPrimeStep()).on(ALL).fail()

                .from(simulateSatelliteDPrimeStep()).on(COMPLETED).end()
                .from(simulateSatelliteDPrimeStep()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteDPrimeStep() {
        return stepBuilderFactory.get("initSimulateSatelliteDPrimeStep")
                .tasklet(initSimulateSatelliteDPrimeTasklet())
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteDPrimeTasklet() {
        return new InitTasklet(Application.SAPFC, HalfFlow.D_PRIME, directoryBenchService, crexFileService);
    }

    @Bean
    public Step moveTopFileDPrimeStep() {
        return this.stepBuilderFactory.get("moveTopFileDPrimeStep")
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
    public Step readTopDPrimeStep() {
        return this.stepBuilderFactory.get("readTopDPrimeStep")
                .listener(crexListener("Lecture du fichier TOP"))
                .tasklet(this.readTopDPrimeTasklet(null, null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public ReadTopTasklet readTopDPrimeTasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            CrexFileService crexFileService,
            ReadTopFileService readTopFileService,
            ReflectionService reflectionService,
            DirectoryBenchService directoryBenchService) {
        return new ReadTopTasklet<>(directoryBenchService.getWorkDirectory(AppType.PACIFIQUE), triggerFilename, DPrimeTopCsv.class, readTopFileService, reflectionService, crexFileService);
    }

    @Bean
    public Step simulateSatelliteDPrimeStep() {

        return stepBuilderFactory.get("simulateSatelliteDPrimeStep")
                .tasklet(simulateSatelliteDPrimeTasklet(null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateSatelliteExportTasklet simulateSatelliteDPrimeTasklet(
            @Value("#{jobExecutionContext['annee']}") Integer exercice,
            @Value("#{jobExecutionContext['periode']}") String period,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileService) {
        return new SimulateSatelliteExportTasklet(new Context(exercice, period), directoryBenchService, fileService);
    }
}
