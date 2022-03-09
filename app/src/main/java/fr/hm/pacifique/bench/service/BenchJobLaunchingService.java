package fr.hm.pacifique.bench.service;

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

    private final Job simulateSatelliteDPrime;
    private final Job launcherDPrime;
    private final Job simulateSatelliteDSeconde1;
    private final Job launcherDSeconde1;

    private final Job simulateSatelliteDSeconde2;
    private final Job launcherDSeconde2;

    private final Job simulateSatelliteDSeconde3;
    private final Job launcherDSeconde3;
    private final Job generateFilesFPrime3;

    private final Job simulateSatelliteFPrime3;
    private final Job launcherFPrime3;

    private final Job simulateSatelliteQPrime1;
    private final Job launcherQPrime1;
    private final Job simulateSatelliteQPrime2;
    private final Job launcherQPrime2;
    private final Job simulateSatelliteQPrime3;
    private final Job launcherQPrime3;
    private final Job simulateSatelliteQPrime5;
    private final Job launcherQPrime5;
    private final Job simulateSatelliteQSeconde3;
    private final Job launcherQSeconde3;

    private final DirectoryBenchService directoryBenchService;

    @Override
    public JobDirectoryWatcher[] getJobDirectoryWatchers() {

        String patternSimulateSatelliteQPrime1 = String.format("%s_%s_TOP.csv", Application.DENSITY.getCode(), HalfFlow.Q_PRIME1.getCode());
        String patternLauncherQPrime1 = String.format("*_%s.zip", HalfFlow.Q_PRIME1.getCode());
        String patternSimulateSatelliteQprime2 = String.format("%s_%s_TOP.csv", Application.DENSITY.getCode(), HalfFlow.Q_PRIME2.getCode());
        String patternLauncherQPrime2 = String.format("*_%s.zip", HalfFlow.Q_PRIME2.getCode());

        String patternSimulateSatelliteQPrime3 = String.format("%s_%s_TOP.csv", Application.DENSITY.getCode(), HalfFlow.Q_PRIME3.getCode());
        String patternLauncherQPrime3 = String.format("*_%s.zip", HalfFlow.Q_PRIME3.getCode());

        String patternSimulateSatelliteQPrime5 = String.format("%s_%s_TOP.csv", Application.CODA.getCode(), HalfFlow.Q_PRIME5.getCode());
        String patternLauncherQPrime5 = String.format("*_%s.zip", HalfFlow.Q_PRIME5.getCode());


        String patternSimulateSatelliteQSeconde3 = String.format("*_%s_%s_BL.csv", Application.DENSITY.getCode(), HalfFlow.Q_SECONDE3.getCode());
        String patternLauncherQSeconde3 = String.format("*_%s.zip", HalfFlow.Q_SECONDE3.getCode());

        String patternSimulateSatelliteDPrime = String.format("%s_%s_TOP.csv", Application.SAPFC.getCode(), HalfFlow.D_PRIME.getCode());
        String patternLauncherDPrime = String.format("*_%s.zip", HalfFlow.D_PRIME.getCode());
        String patternSimulateSatelliteDSeconde1 = String.format("*_%s_%s_BL.csv", Application.VEGA.getCode(), HalfFlow.D_SECONDE1.getCode());
        String patternLauncherDSeconde1 = String.format("*_%s.zip", HalfFlow.D_SECONDE1.getCode());


        String patternSimulateSatelliteDSeconde2 = String.format("*_%s_%s_BL.csv", Application.VEGA.getCode(), HalfFlow.D_SECONDE2.getCode());
        String patternLauncherDSeconde2 = String.format("*_%s.zip", HalfFlow.D_SECONDE2.getCode());

        String patternSimulateSatelliteDSeconde3 = String.format("*_%s_%s_BL.csv", Application.VEGA.getCode(), HalfFlow.D_SECONDE3.getCode());
        String patternLauncherDSeconde3 = String.format("*_%s.zip", HalfFlow.D_SECONDE3.getCode());
        String patternGenerateFiles = String.format("TOP_*.csv");

        String patternSimulateSatelliteFPrime3 = String.format("%s_%s_TOP.csv", Application.DENSITY.getCode(), HalfFlow.F_PRIME3.getCode());
        String patternLauncherFPrime3 = String.format("*_%s.zip", HalfFlow.F_PRIME3.getCode());



        return new JobDirectoryWatcher[]{
//                // Extracteurs
//                // Q-Prime1
                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.Q_PRIME1), patternSimulateSatelliteQPrime1, simulateSatelliteQPrime1),
                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.Q_PRIME1), patternLauncherQPrime1, launcherQPrime1),
//                // Q-Prime2
                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.Q_PRIME2), patternSimulateSatelliteQprime2, simulateSatelliteQPrime2),
                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.CODA, HalfFlow.Q_PRIME2), patternLauncherQPrime2, launcherQPrime2),
//
//                // Q-Prime3
//                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.Q_PRIME3), patternSimulateSatelliteQPrime3, simulateSatelliteQPrime3),
//                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.Q_PRIME3), patternLauncherQPrime3,launcherQPrime3),
//
//                // Q-Prime5
//                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.CODA, HalfFlow.Q_PRIME5),patternSimulateSatelliteQPrime5, simulateSatelliteQPrime5),
//                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.CODA, HalfFlow.Q_PRIME5), patternLauncherQPrime5, launcherQPrime5),
//                // Q-Seconde3
//                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.Q_SECONDE3), patternSimulateSatelliteQSeconde3,simulateSatelliteQSeconde3),
//                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.SAPFC, HalfFlow.Q_SECONDE3), patternLauncherQSeconde3, launcherQSeconde3)

                // D-Prime
//                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.SAPFC, HalfFlow.D_PRIME), patternSimulateSatelliteDPrime, simulateSatelliteDPrime),
//                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.SAPFC, HalfFlow.D_PRIME), patternLauncherDPrime, launcherDPrime),
//
//                // D-Seconde1
//                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.VEGA, HalfFlow.D_SECONDE1), patternSimulateSatelliteDSeconde1,simulateSatelliteDSeconde1),
//                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.VEGA, HalfFlow.D_SECONDE1), patternLauncherDSeconde1, launcherDSeconde1),
//
//                // D-Seconde2
//                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.VEGA, HalfFlow.D_SECONDE2), patternSimulateSatelliteDSeconde2,simulateSatelliteDSeconde2),
//                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.VEGA, HalfFlow.D_SECONDE2), patternLauncherDSeconde2, launcherDSeconde2),
//
//                // D-Seconde3
//                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.VEGA, HalfFlow.D_SECONDE3), patternSimulateSatelliteDSeconde3,simulateSatelliteDSeconde3),
//                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.VEGA, HalfFlow.D_SECONDE3), patternLauncherDSeconde3, launcherDSeconde3),
                // Generate files
                JobDirectoryWatcher.build(directoryBenchService.getInDataDirectory(AppType.BENCH, Application.DENSITY, HalfFlow.F_PRIME3), patternGenerateFiles, generateFilesFPrime3),
                // Extracteurs
                // F-Prime
                JobDirectoryWatcher.build(directoryBenchService.getOutDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.F_PRIME3), patternSimulateSatelliteFPrime3, simulateSatelliteFPrime3),
                JobDirectoryWatcher.build(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE, Application.DENSITY, HalfFlow.F_PRIME3), patternLauncherFPrime3, launcherFPrime3),
        };
    }
}
