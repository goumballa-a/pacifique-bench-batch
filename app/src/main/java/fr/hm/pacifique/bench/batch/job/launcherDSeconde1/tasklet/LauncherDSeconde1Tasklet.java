package fr.hm.pacifique.bench.batch.job.launcherASeconde1.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.service.BenchService;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.bench.service.FileBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class LauncherASeconde1Tasklet implements Tasklet {
    private final String triggerFilename;
    private final DirectoryBenchService directoryBenchService;
    private final FileBenchService fileBenchService;
    private final BenchService benchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        benchService.wait(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE), triggerFilename);

        fileBenchService.moveFile(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE), directoryBenchService.getArcInDataDirectory(AppType.BENCH), triggerFilename);
        //String contextName = fileBenchService.getContext(directoryBenchService.getArcInDataDirectory(AppType.BENCH), Application.CODA, HalfFlow.A_PRIME);
        String contextName = "2019AN";
        if (contextName != null) {
            benchService.generateTopASeconde1(new Context(contextName));
        } else {
            benchService.generateTopCSeconde1(new Context());
        }

        return RepeatStatus.FINISHED;
    }
}
