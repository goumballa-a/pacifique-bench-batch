package fr.hm.pacifique.bench.batch.job.launcherQPrime1.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.service.BenchService;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.bench.service.FileBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LauncherQPrime1Tasklet implements Tasklet {
    private final String triggerFilename;
    private final DirectoryBenchService directoryBenchService;
    private final FileBenchService fileBenchService;
    private final BenchService benchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws InterruptedException {

        benchService.wait(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE), triggerFilename);

        fileBenchService.moveFile(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE), directoryBenchService.getArcInDataDirectory(AppType.BENCH), triggerFilename);

         TimeUnit.SECONDS.sleep(90);
        String contextName = fileBenchService.getContext(directoryBenchService.getArcInDataDirectory(AppType.BENCH), Application.DENSITY, HalfFlow.Q_PRIME1);
        if (contextName != null) {
            benchService.generateTopQPrime1(new Context(contextName));
        } else {
            benchService.generateTopQPrime2(new Context());
        }

        return RepeatStatus.FINISHED;
    }
}
