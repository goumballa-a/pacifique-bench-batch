package fr.hm.pacifique.bench.batch.common.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.bench.service.FileBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class SimulateSatelliteExportTasklet implements Tasklet {
    private final Context context;
    private final DirectoryBenchService directoryBenchService;
    private final FileBenchService fileBenchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        fileBenchService.deleteFiles(directoryBenchService.getWorkDirectory(AppType.PACIFIQUE));
        fileBenchService.copyFiles(directoryBenchService.getInDataDirectory(AppType.BENCH, context), directoryBenchService.getInDataDirectory(AppType.PACIFIQUE));

        return RepeatStatus.FINISHED;
    }
}
