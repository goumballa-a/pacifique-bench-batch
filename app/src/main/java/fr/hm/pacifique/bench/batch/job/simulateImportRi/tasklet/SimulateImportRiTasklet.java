package fr.hm.pacifique.bench.batch.job.simulateImportRi.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.service.BenchService;
import fr.hm.pacifique.bench.service.DirectoryService;
import fr.hm.pacifique.bench.service.FileBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class SimulateImportRiTasklet implements Tasklet {
    private final DirectoryService directoryService;
    private final FileBenchService fileBenchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        fileBenchService.purgeDirectory(directoryService.getOutDirectory(AppType.PACIFIQUE));
        fileBenchService.copyFiles(directoryService.getInDataDirectory(AppType.BENCH), directoryService.getInDataDirectory(AppType.PACIFIQUE));

        return RepeatStatus.FINISHED;
    }
}
