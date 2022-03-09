package fr.hm.pacifique.bench.service;

import fr.hm.pacifique.bench.config.PacifiqueProperties;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.common.config.directorywatcher.JobDirectoryWatcher;
import fr.hm.pacifique.common.service.JobLaunchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BenchJobLaunchingService implements JobLaunchingService {

    private final Job generateFilesFPrime;
    private final Job simulateExportDensity;
    private final Job launcherFPrime;
    private final Job simulateImportSapfc;
    private final Job launcherFSeconde1;
    private final Job simulateImportRi;
    private final Job launcherFSeconde2;
    private final DirectoryService directoryService;

    @Override
    public JobDirectoryWatcher[] getJobDirectoryWatchers() {
        String patternGenerateFilesFPrime = String.format("TOP_*.csv");
        String patternSimulateExportDensity = String.format("*_%s_%s_BL.csv", Application.DENSITY.getCode(), HalfFlow.F_PRIME.getCode());
        String patternLauncherFPrime = String.format("*_%s.zip", HalfFlow.F_PRIME.getCode());
        String patternSimulateImportSapfc = String.format("*_%s_%s_BL.csv", Application.SAPFC.getCode(), HalfFlow.F_SECONDE1.getCode());
        String patternLauncherFSeconde1 = String.format("*_%s.zip", HalfFlow.F_SECONDE1.getCode());
        String patternSimulateImportRi = String.format("*_%s_%s_BL.csv", Application.RI.getCode(), HalfFlow.F_SECONDE2.getCode());
        String patternLauncherFSeconde2 = String.format("*_%s.zip", HalfFlow.F_SECONDE2.getCode());
        return new JobDirectoryWatcher[]{
                JobDirectoryWatcher.build(directoryService.getInDataDirectory(AppType.BENCH, Application.DENSITY, HalfFlow.F_PRIME),
                        patternGenerateFilesFPrime, generateFilesFPrime),
                JobDirectoryWatcher.build(directoryService.getOutDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.F_PRIME),
                        patternSimulateExportDensity, simulateExportDensity),
                JobDirectoryWatcher.build(directoryService.getArcInDataDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.F_PRIME),
                        patternLauncherFPrime, launcherFPrime),
                JobDirectoryWatcher.build(directoryService.getOutDirectory(AppType.PACIFIQUE, Application.SAPFC, HalfFlow.F_SECONDE1),
                        patternSimulateImportSapfc, simulateImportSapfc),
                JobDirectoryWatcher.build(directoryService.getArcInDataDirectory(AppType.PACIFIQUE, Application.SAPFC, HalfFlow.F_SECONDE1),
                        patternLauncherFSeconde1, launcherFSeconde1),
                JobDirectoryWatcher.build(directoryService.getOutDirectory(AppType.PACIFIQUE, Application.RI, HalfFlow.F_SECONDE2),
                        patternSimulateImportRi, simulateImportRi),
                JobDirectoryWatcher.build(directoryService.getArcInDataDirectory(AppType.PACIFIQUE, Application.RI, HalfFlow.F_SECONDE2),
                        patternLauncherFSeconde2, launcherFSeconde2)
        };
    }
}
