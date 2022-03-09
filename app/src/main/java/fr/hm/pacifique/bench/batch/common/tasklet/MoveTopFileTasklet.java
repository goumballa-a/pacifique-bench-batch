package fr.hm.pacifique.bench.batch.common.tasklet;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.service.DirectoryBenchService;
import fr.hm.pacifique.common.service.CrexFileService;
import fr.hm.pacifique.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class MoveTopFileTasklet implements Tasklet {

    private final String triggerFilename;
    private final FileService fileService;
    private final CrexFileService crexFileService;
    private final DirectoryBenchService directoryBenchService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        // Move trigger files in working directory
        crexFileService.loadInfoCrexDetailCsv("Déplacement du fichier TOP");
        fileService.moveFile(directoryBenchService.getTriggerDirectory(AppType.PACIFIQUE), directoryBenchService.getWorkDirectory(AppType.PACIFIQUE), triggerFilename);
        return RepeatStatus.FINISHED;
    }
}
