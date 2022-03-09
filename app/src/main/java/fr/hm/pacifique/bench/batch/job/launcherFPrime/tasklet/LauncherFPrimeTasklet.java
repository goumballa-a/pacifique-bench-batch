package fr.hm.pacifique.bench.batch.job.launcherFPrime.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.service.BenchService;
import fr.hm.pacifique.bench.service.DirectoryService;
import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.exception.FileException;
import fr.hm.pacifique.common.service.TimeoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LauncherFPrimeTasklet implements Tasklet {
    private final String triggerFilename;
    private final DirectoryService directoryService;
    private final FileBenchService fileBenchService;
    private final BenchService benchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        benchService.wait(directoryService.getArcInDataDirectory(AppType.PACIFIQUE), triggerFilename);

        fileBenchService.moveFile(directoryService.getArcInDataDirectory(AppType.PACIFIQUE), directoryService.getArcInDataDirectory(AppType.BENCH), triggerFilename);
        String contextName = fileBenchService.getContext(directoryService.getArcInDataDirectory(AppType.BENCH));
        if (contextName != null) {
            benchService.generateTopFPrime(new Context(contextName));
        } else {
            benchService.generateTopFSeconde1(new Context());
        }

        return RepeatStatus.FINISHED;
    }
}
