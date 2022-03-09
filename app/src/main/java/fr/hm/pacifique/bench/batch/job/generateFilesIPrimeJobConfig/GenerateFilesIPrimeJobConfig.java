package fr.hm.pacifique.bench.batch.job.generateFilesIPrimeJobConfig;

import fr.hm.pacifique.bench.batch.common.tasklet.InitTasklet;
import fr.hm.pacifique.bench.batch.job.generateFilesIPrimeJobConfig.processor.GenerateFilesIPrimeCotisationProcessor;
import fr.hm.pacifique.bench.batch.job.generateFilesIPrimeJobConfig.processor.GenerateFilesIPrimePrestationProcessor;
import fr.hm.pacifique.bench.batch.job.generateFilesIPrimeJobConfig.tasklet.DeleteTasklet;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.dcb.iprime.CotisationsRwtCsv;
import fr.hm.pacifique.bench.projection.dcb.iprime.PrestationsRwtCsv;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.common.batch.AbstractJobConfig;
import fr.hm.pacifique.common.batch.reader.CommonCsvReader;
import fr.hm.pacifique.common.batch.writer.CommonCsvWriter;
import fr.hm.pacifique.common.service.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fr.hm.pacifique.common.constant.CommonBatchConstants.ALL;

@Configuration
@EnableBatchProcessing
public class GenerateFilesIPrimeJobConfig extends AbstractJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    public GenerateFilesIPrimeJobConfig(JobBuilderFactory jobBuilderFactory) {
        super();
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job generateFilesIPrime() {
        return jobBuilderFactory.get("generateFilesIPrime")
                .start(initLauncherIPrimeStep()).on(ALL).to(deleteIPrimeStep())
                .from(deleteIPrimeStep()).on(ALL).to(generateFilesIPrimeCotisationsStep())
                .from(generateFilesIPrimeCotisationsStep()).on(ALL).to(generateFilesIPrimePrestationsStep())
                .from(generateFilesIPrimePrestationsStep()).on(ALL).fail()
                .end()
                .build();
    }

    @Bean
    public Step initLauncherIPrimeStep() {
        return stepBuilderFactory.get("initLauncherIPrimeStep")
                .tasklet(initLauncherIPrimeTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public InitTasklet initLauncherIPrimeTasklet(
            DirectoryBenchService directoryBenchService) {
        return new InitTasklet(Application.DECIBEL, HalfFlow.I_PRIME, directoryBenchService, crexFileService);
    }

    @Bean
    public Step deleteIPrimeStep() {
        return stepBuilderFactory.get("deleteIPrimeStep")
                .tasklet(deleteIPrimeTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public DeleteTasklet deleteIPrimeTasklet(
            FileService fileService) {
        return new DeleteTasklet(fileService);
    }

    @Bean
    public Step generateFilesIPrimeCotisationsStep() {
        return stepBuilderFactory.get("generateFilesIPrimeCotisationsStep")
                .<CotisationsRwtCsv, CotisationsRwtCsv>chunk(500)
                .reader(generateFilesIPrimeCotisationsReader(null, null, null))
                .processor(generateFilesIPrimeCotisationsProcessor())
                .writer(generateFilesIPrimeCotisationsWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public CommonCsvReader<CotisationsRwtCsv> generateFilesIPrimeCotisationsReader(
            ReadCsvFileService readCsvFileService,
            MandatoryService mandatoryService,
            ReflectionService reflectionService) {
        String directory = "D:\\pacifique_ged\\pacifique-socle-etl-batch\\I-PRIME\\ms\\sas\\reception\\201601";
        String filename = "20200219153716_DCB_I-PRIME_COTISATIONS.csv";
        return new CommonCsvReader<>(directory, filename, CotisationsRwtCsv.class, readCsvFileService, mandatoryService, reflectionService)
                .setNbLineHeader(1);
    }

    @Bean
    @StepScope
    public GenerateFilesIPrimeCotisationProcessor generateFilesIPrimeCotisationsProcessor() {
        return new GenerateFilesIPrimeCotisationProcessor();
    }

    @Bean
    @StepScope
    public CommonCsvWriter<CotisationsRwtCsv> generateFilesIPrimeCotisationsWriter(
            WriteCsvFileService writeCsvFileService) {
        String directory = "D:\\pacifique_ged\\pacifique-socle-etl-batch\\i-prime\\ms\\sas\\201909";
        String filename = "20200219153716_DCB_I-PRIME_COTISATIONS.csv";
        return new CommonCsvWriter<>(directory, filename, CotisationsRwtCsv.class, writeCsvFileService);
    }

    @Bean
    public Step generateFilesIPrimePrestationsStep() {
        return stepBuilderFactory.get("generateFilesIPrimePrestationsStep")
                .<PrestationsRwtCsv, PrestationsRwtCsv>chunk(500)
                .reader(generateFilesIPrimePrestationsReader(null, null, null))
                .processor(generateFilesIPrimePrestationsProcessor())
                .writer(generateFilesIPrimePrestationsWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public CommonCsvReader<PrestationsRwtCsv> generateFilesIPrimePrestationsReader(
            ReadCsvFileService readCsvFileService,
            MandatoryService mandatoryService,
            ReflectionService reflectionService) {
        String directory = "D:\\pacifique_ged\\pacifique-socle-etl-batch\\i-prime\\ms\\sas\\201908";
        String filename = "20200219154349_DCB_I-PRIME_PRESTATIONS.csv";
        return new CommonCsvReader<>(directory, filename, PrestationsRwtCsv.class, readCsvFileService, mandatoryService, reflectionService)
                .setNbLineHeader(1);
    }

    @Bean
    @StepScope
    public GenerateFilesIPrimePrestationProcessor generateFilesIPrimePrestationsProcessor() {
        return new GenerateFilesIPrimePrestationProcessor();
    }

    @Bean
    @StepScope
    public CommonCsvWriter<PrestationsRwtCsv> generateFilesIPrimePrestationsWriter(
            WriteCsvFileService writeCsvFileService) {
        String directory = "D:\\pacifique_ged\\pacifique-socle-etl-batch\\i-prime\\ms\\sas\\201909";
        String filename = "20200219154349_DCB_I-PRIME_PRESTATIONS.csv";
        return new CommonCsvWriter<>(directory, filename, PrestationsRwtCsv.class, writeCsvFileService);
    }
}
