package fr.hm.pacifique.bench.batch.job.simulateSatelliteQPrime5;


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
public class SimulateSatelliteQPrime5JobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final DirectoryBenchService directoryBenchService;

    public SimulateSatelliteQPrime5JobConfig(JobBuilderFactory jobBuilderFactory, DirectoryBenchService directoryBenchService) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
        this.directoryBenchService = directoryBenchService;
    }

    @Bean
    public Job simulateSatelliteQPrime5() {
        return jobBuilderFactory.get("simulateSatelliteQPrime5")
                .start(initSimulateSatelliteQPrime5Step()).on(COMPLETED).to(moveTopFileQPrime5Step())
                .from(initSimulateSatelliteQPrime5Step()).on(ALL).fail()

                .from(moveTopFileQPrime5Step()).on(COMPLETED).to(readTopQPrime5Step())
                .from(moveTopFileQPrime5Step()).on(ALL).fail()

                .from(readTopQPrime5Step()).on(COMPLETED).to(simulateSatelliteQPrime5Step())
                .from(readTopQPrime5Step()).on(ALL).fail()

                .from(simulateSatelliteQPrime5Step()).on(COMPLETED).end()
                .from(simulateSatelliteQPrime5Step()).on(ALL).fail()

                .end()
                .build();
    }

    @Bean
    public Step initSimulateSatelliteQPrime5Step() {
        return stepBuilderFactory.get("initSimulateSatelliteQPrime5Step")
                .tasklet(initSimulateSatelliteQPrime5Tasklet())
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initSimulateSatelliteQPrime5Tasklet() {
        return new InitTasklet(Application.CODA, HalfFlow.Q_PRIME5, directoryBenchService, crexFileService);
    }

    @Bean
    public Step moveTopFileQPrime5Step() {
        return this.stepBuilderFactory.get("moveTopFileQPrime5Step")
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
    public Step readTopQPrime5Step() {
        return this.stepBuilderFactory.get("readTopQPrime5Step")
                .listener(crexListener("Lecture du fichier TOP"))
                .tasklet(this.readTopQPrime5Tasklet(null, null, null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public ReadTopTasklet readTopQPrime5Tasklet(
            @Value("#{jobParameters['triggerFilename']}") String triggerFilename,
            CrexFileService crexFileService,
            ReadTopFileService readTopFileService,
            ReflectionService reflectionService,
            DirectoryBenchService directoryBenchService) {
        return new ReadTopTasklet<>(directoryBenchService.getWorkDirectory(AppType.PACIFIQUE), triggerFilename, QPrimeTopCsv.class, readTopFileService, reflectionService, crexFileService);
    }

    @Bean
    public Step simulateSatelliteQPrime5Step() {

        return stepBuilderFactory.get("simulateSatelliteQPrime5Step")
                .tasklet(simulateSatelliteQPrime5Tasklet(null, null,null,null,null, null, null))
                .build();
    }

    @Bean
    @StepScope
    public SimulateSatelliteExportTasklet simulateSatelliteQPrime5Tasklet(
            @Value("#{jobExecutionContext['exerciceDebut']}") Integer exerciceDeb,
            @Value("#{jobExecutionContext['exerciceFin']}") Integer exerciceFin,
            @Value("#{jobExecutionContext['periodeDebut']}") String periodeDebut,
            @Value("#{jobExecutionContext['periodeFin']}") String periodeFin,
            @Value("#{jobExecutionContext['societe']}") String societe,
            DirectoryBenchService directoryBenchService,
            FileBenchService fileService) {
        return new SimulateSatelliteExportTasklet(new Context(exerciceDeb,exerciceFin,periodeDebut,periodeFin,societe), directoryBenchService, fileService);
    }
}
