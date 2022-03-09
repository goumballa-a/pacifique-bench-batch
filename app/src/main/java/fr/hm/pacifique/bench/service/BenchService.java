package fr.hm.pacifique.bench.service;

import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
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
    private final DirectoryService directoryService;
    private final FileBenchService fileBenchService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void wait(String directoryPath, String filename) {
        long fileSize = 0L;
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

    public void generateTopFPrime(Context context) {
        Optional<String> filename = fileBenchService.findFilenameByPattern(directoryService.getInDataDirectory(AppType.BENCH, context), "*_DHM_F-PRIME_STACK_*.csv");
        filename.ifPresent(s -> fileBenchService.copyFile(directoryService.getInDataDirectory(AppType.BENCH, context), directoryService.getInUserDirectory(AppType.PACIFIQUE), s));
        LocalDateTime localDateTime = context.getDate();
        CsvFileBuilder.build()
                .filepath(directoryService.getInUserDirectory(AppType.PACIFIQUE), "DHM_F-PRIME_TOP.csv")
                .disableQuote()
                .row(String.format("\"Date reporting\";\"%s\"", localDateTime.format(dateTimeFormatter)))
                .row(String.format("\"Periode\";\"%s\"", context.getContextPeriod().getLabel()))
                .create();
        CsvFileBuilder.build()
                .filepath(directoryService.getInUserDirectory(AppType.PACIFIQUE), context.toString() + "000000_DHM_F-PRIME_BL.csv")
                .disableQuote()
                .row("DHM_F-PRIME_TOP.csv")
                .row(filename.get())
                .create();
    }

    public void generateTopFSeconde1(Context context) {
        LocalDateTime localDateTime = context.getDate();
        CsvFileBuilder.build()
                .filepath(directoryService.getInUserDirectory(AppType.PACIFIQUE, Application.SAPFC, HalfFlow.F_SECONDE1), "EPM_F-SECONDE1_TOP.csv")
                .disableQuote()
                .row(String.format("\"Date reporting\";\"%s\"", localDateTime.format(dateTimeFormatter)))
                .row(String.format("\"Entite\";\"%s\"", "HM"))
                .row(String.format("\"Periode\";\"%s\"", context.getContextPeriod().getLabel()))
                .row(String.format("\"Cible\";\"%s\"", "RECETTE"))
                .create();
    }

    public void generateTopFSeconde2(Context context) {
        LocalDateTime localDateTime = context.getDate();
        CsvFileBuilder.build()
                .filepath(directoryService.getInUserDirectory(AppType.PACIFIQUE, Application.RI, HalfFlow.F_SECONDE2), "MOO_F-SECONDE2_TOP.csv")
                .disableQuote()
                .row(String.format("\"Année reporting\";\"%s\"", localDateTime.getYear()))
                .row(String.format("\"Periode\";\"%s\"", context.getContextPeriod().getLabel()))
                .create();
    }
}
