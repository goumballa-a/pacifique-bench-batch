package fr.hm.pacifique.bench.batch.common.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.service.DirectoryService;
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
    private final DirectoryService directoryService;
    private final CrexFileService crexFileService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // Initialise le service DirectoryService
        directoryService.init(application, halfFlow);

        crexFileService.initCrexService(directoryService.getLogDirectory(AppType.BENCH),
                application.getCode(), halfFlow.getCode());

        return RepeatStatus.FINISHED;
    }
}
