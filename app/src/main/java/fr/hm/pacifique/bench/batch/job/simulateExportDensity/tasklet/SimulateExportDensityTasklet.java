package fr.hm.pacifique.bench.batch.job.simulateExportDensity.tasklet;

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
public class SimulateExportDensityTasklet implements Tasklet {
    private final Context context;
    private final DirectoryService directoryService;
    private final FileBenchService fileBenchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        fileBenchService.deleteFiles(directoryService.getWorkDirectory(AppType.PACIFIQUE));
        fileBenchService.copyFiles(directoryService.getInDataDirectory(AppType.BENCH, context), directoryService.getInDataDirectory(AppType.PACIFIQUE));

        return RepeatStatus.FINISHED;
    }
}
