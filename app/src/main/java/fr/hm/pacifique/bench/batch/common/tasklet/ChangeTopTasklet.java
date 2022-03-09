package fr.hm.pacifique.bench.batch.common.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.ContextPeriod;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.bench.service.FileBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class ChangeTopTasklet implements Tasklet {
    private final String triggerFilename;
    private final DirectoryBenchService directoryBenchService;
    private final FileBenchService fileService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String period = triggerFilename.substring(8, 10);
        int year = Integer.parseInt(triggerFilename.substring(4, 8));
        Context context = new Context(year, period);

        if (context.getYear() == 2021 && context.getContextPeriod().equals(ContextPeriod.M12)) {
            fileService.deleteFile(directoryBenchService.getInDataDirectory(AppType.BENCH), triggerFilename);
        } else {
            fileService.renameFile(directoryBenchService.getInDataDirectory(AppType.BENCH), triggerFilename, context);
        }

        return RepeatStatus.FINISHED;
    }
}
