package fr.hm.pacifique.bench.batch.job.launcherFPrime3.tasklet;

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

import java.lang.reflect.GenericDeclaration;

@RequiredArgsConstructor
public class LauncherFPrime3Tasklet implements Tasklet {
    private final String triggerFilename;
    private final DirectoryBenchService directoryBenchService;
    private final FileBenchService fileBenchService;
    private final BenchService benchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        benchService.wait(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE), triggerFilename);

        fileBenchService.moveFile(directoryBenchService.getArcInDataDirectory(AppType.PACIFIQUE), directoryBenchService.getArcInDataDirectory(AppType.BENCH), triggerFilename);

        String contextName = fileBenchService.getContext(directoryBenchService.getArcInDataDirectory(AppType.BENCH), Application.DENSITY, HalfFlow.F_PRIME3);
        if (contextName != null) {
            benchService.generateTopFPrime3(new Context(contextName));
        } else {
//            end
        }

        return RepeatStatus.FINISHED;
    }
}
