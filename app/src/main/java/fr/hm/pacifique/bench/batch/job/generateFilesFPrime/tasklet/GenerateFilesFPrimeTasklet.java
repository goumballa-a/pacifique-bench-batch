package fr.hm.pacifique.bench.batch.job.generateFilesFPrime.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.service.DirectoryService;
import fr.hm.pacifique.bench.service.FileBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class GenerateFilesFPrimeTasklet implements Tasklet {
    private final String triggerFilename;
    private final DirectoryService directoryService;
    private final FileBenchService fileService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        String period = triggerFilename.substring(8, 10);
        int year = Integer.parseInt(triggerFilename.substring(4, 8));
        Context contextIn = new Context(year, period);
        Context contextOut = contextIn.increase();

        fileService.copyFiles(directoryService.getInDataDirectory(AppType.BENCH, contextIn), directoryService.getInDataDirectory(AppType.BENCH, contextOut));
        fileService.renameFiles(directoryService.getInDataDirectory(AppType.BENCH, contextOut), contextIn);

        return RepeatStatus.FINISHED;
    }
}
