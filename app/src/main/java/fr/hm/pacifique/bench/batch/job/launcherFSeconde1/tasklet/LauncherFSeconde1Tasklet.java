package fr.hm.pacifique.bench.batch.job.launcherFSeconde1.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.service.BenchService;
import fr.hm.pacifique.bench.service.DirectoryService;
import fr.hm.pacifique.bench.service.FileBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class LauncherFSeconde1Tasklet implements Tasklet {
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
            benchService.generateTopFSeconde1(new Context(contextName));
        } else {
            benchService.generateTopFSeconde2(new Context());
        }

        return RepeatStatus.FINISHED;
    }
}
