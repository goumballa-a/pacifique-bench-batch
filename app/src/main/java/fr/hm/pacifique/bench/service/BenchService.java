package fr.hm.pacifique.bench.service;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.common.constant.CommonConstants;
import fr.hm.pacifique.common.data.CsvFileBuilder;
import fr.hm.pacifique.common.exception.FileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BenchService {
    private final DirectoryBenchService directoryBenchService;
    private final FileBenchService fileBenchService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void wait(String directoryPath, String filename) {
        long fileSize;
        do {
            try {
                TimeUnit.SECONDS.sleep(1);
                fileSize = Paths.get(directoryPath, filename).toFile().length();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new FileException("Error during the wait of a timeout", e);
            }
        }
        while (fileSize == 0);
    }

    public void waitFilePatern(String directoryPath,String pattern ) {
        long fileSize = 0L;
        Optional<String> filePatern;
        do {
            try {
               filePatern= fileBenchService.findFilenameByPattern(directoryBenchService.getArcInUserDirectoryQDD(AppType.PACIFIQUE,HalfFlow.Q_SECONDE1), pattern);
                TimeUnit.SECONDS.sleep(1);
                if(filePatern.isPresent())
                fileSize = Paths.get(directoryPath, filePatern.get()).toFile().length();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new FileException("Error during the wait of a timeout", e);
            }
        }
        while (fileSize == 0);
        filePatern.ifPresent(s -> fileBenchService.moveFile(directoryBenchService.getArcInUserDirectoryQDD(AppType.PACIFIQUE,HalfFlow.Q_SECONDE1),directoryBenchService.getArcInUserDirectoryQDD(AppType.BENCH,HalfFlow.Q_SECONDE1),s));

    }
    public void generateTopQPrime1(Context context) {
        Application application = Application.DENSITY;
        HalfFlow halfFlow = HalfFlow.Q_PRIME1;
        LocalDateTime localDateTime = context.getDate();
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Date reporting", localDateTime.format(dateTimeFormatter))
                .row("Periode", context.getContextPeriod().getLabel())
                .create();
    }
    public void generateTopDPrime(Context context) {
        Application application = Application.SAPFC;
        HalfFlow halfFlow = HalfFlow.D_PRIME;
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Année", String.valueOf(context.getYear()))
                .row("Période", context.getContextPeriod().getLabel())
                .create();
    }

    public void generateTopDSeconde1(Context context) {
        Application application = Application.VEGA;
        HalfFlow halfFlow = HalfFlow.D_SECONDE1;
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Année", String.valueOf(context.getYear()))
                .row("Période", context.getContextPeriod().getLabel())
                .create();
    }

    public void generateTopDSeconde2(Context context) {
        Application application = Application.VEGA;
        HalfFlow halfFlow = HalfFlow.D_SECONDE2;
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Année", String.valueOf(context.getYear()))
                .row("Période", context.getContextPeriod().getLabel())
                .create();
    }

    public void generateTopDSeconde3(Context context) {
        Application application = Application.VEGA;
        HalfFlow halfFlow = HalfFlow.D_SECONDE3;
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Année", String.valueOf(context.getYear()))
                .row("Période", context.getContextPeriod().getLabel())
                .create();
    }

    public void generateTopQPrime2(Context context) {
        Application application = Application.DENSITY;
        HalfFlow halfFlow = HalfFlow.Q_PRIME2;
        Optional<String> filename = fileBenchService.findFilenameByPattern(directoryBenchService.getInDataDirectory(AppType.BENCH, context), "*_DHM_Q-PRIME2_EDPVL_*.xlsx");
        filename.ifPresent(s -> fileBenchService.copyFile(directoryBenchService.getInDataDirectory(AppType.BENCH, context), directoryBenchService.getInUserDirectory(AppType.PACIFIQUE), s));
        LocalDateTime localDateTime = context.getDate();
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Date reporting", localDateTime.format(dateTimeFormatter))
                .row("Periode", context.getContextPeriod().getLabel())
                .create();
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), context.toString() + "000000_DHM_Q-PRIME2_BL.csv")
                .row("DHM_Q-PRIME2_TOP.csv")
                .row(filename.get())
                .create();
    }

    public void generateTopQPrime3(Context context) {
        Application application = Application.DENSITY;
        HalfFlow halfFlow = HalfFlow.Q_PRIME3;
        Optional<String> filename = fileBenchService.findFilenameByPattern(directoryBenchService.getInDataDirectory(AppType.BENCH, context), "*_DHM_Q-PRIME2_ALLOCATION_*.csv");
        filename.ifPresent(s -> fileBenchService.copyFile(directoryBenchService.getInDataDirectory(AppType.BENCH, context), directoryBenchService.getInUserDirectory(AppType.PACIFIQUE), s));
    }

    public void generateTopQPrime5(Context context) {
        Application application = Application.CODA;
        HalfFlow halfFlow = HalfFlow.Q_PRIME5;
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Periode fin",context.getContextPeriod().getLabel())
                .row("Exercice debut", String.valueOf(context.getYear()))
                .row("Periode debut", context.getContextPeriod().getLabel())
                .row("Societe", "HARMELLE")
                .row("Exercice fin", String.valueOf(context.getYear()))

                .create();
    }

    public void generateTopQSeconde3(Context context) {
        Application application = Application.DENSITY;
        HalfFlow halfFlow = HalfFlow.Q_SECONDE3;
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Entité", "HM")
                .row("Cible", "RECETTE")
                .row("Mois", context.getContextPeriod().getLabel())
                .row("Année",String.valueOf(context.getYear()))
                .create();
    }


    public void generateTopFPrime3(Context context) {
        Application application = Application.DENSITY;
        HalfFlow halfFlow = HalfFlow.F_PRIME3;
        LocalDateTime localDateTime = context.getDate();
        CsvFileBuilder.build()
                .filepath(generateInUserDirectory(application, halfFlow), generateTopFilename(application, halfFlow))
                .row("Date reporting", localDateTime.format(dateTimeFormatter))
                .row("Periode", context.getContextPeriod().getLabel())
                .create();
    }


    public String generateInUserDirectory(Application application, HalfFlow halfFlow) {
        return directoryBenchService.getInUserDirectory(AppType.PACIFIQUE, application, halfFlow);
    }

    public String generateTopFilename(Application application, HalfFlow halfFlow) {
        return application.getCode()
                .concat(CommonConstants.FILE_SEPARATOR)
                .concat(halfFlow.getCode())
                .concat(CommonConstants.FILE_SEPARATOR)
                .concat("TOP")
                .concat(CommonConstants.CSV_EXTENSION);
    }
}
