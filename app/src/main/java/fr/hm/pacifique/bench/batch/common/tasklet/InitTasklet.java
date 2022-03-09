package fr.hm.pacifique.bench.batch.common.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.common.service.CrexFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class InitTasklet implements Tasklet {
    private final Application application;
    private final HalfFlow halfFlow;
    private final DirectoryBenchService directoryBenchService;
    private final CrexFileService crexFileService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // Initialise le service DirectoryService
        directoryBenchService.init(application, halfFlow);

        crexFileService.initCrexService(directoryBenchService.getLogDirectory(AppType.BENCH), application, halfFlow);

        return RepeatStatus.FINISHED;
    }
}
