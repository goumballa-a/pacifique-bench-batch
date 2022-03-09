package fr.hm.pacifique.bench.service;

import fr.hm.pacifique.bench.enums.AppType;
import fr.hm.pacifique.bench.enums.Application;
import fr.hm.pacifique.bench.enums.HalfFlow;
import fr.hm.pacifique.bench.projection.common.Context;
import fr.hm.pacifique.common.data.CsvFileBuilder;
import fr.hm.pacifique.common.exception.FileException;
import fr.hm.pacifique.common.service.FileService;
import fr.hm.pacifique.common.service.TimeoutService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class FileBenchService extends FileService {
    private final DirectoryService directoryService;

    public FileBenchService(TimeoutService timeoutService, DirectoryService directoryService) {
        super(timeoutService);
        this.directoryService = directoryService;
    }

    public void copyFiles(String inDirectory, String outDirectory) {
        File in = Paths.get(inDirectory).toFile();
        File out = Paths.get(outDirectory).toFile();

        try {
            copyFullRecursive(in, out);
        } catch (FileAlreadyExistsException var7) {
            throw new FileException(MessageFormat.format("Erreur technique lors de la copie du fichier {0} dans le répertoire {1}. Le fichier existe déjà dans le répertoire {1}.", inDirectory, outDirectory), var7);
        } catch (IOException var8) {
            throw new FileException(MessageFormat.format("Erreur technique lors de la copie du fichier {0} dans le répertoire {1}.", outDirectory, outDirectory), var8);
        }
    }

    public String getContext(String directory) {
        int nbFiles = Paths.get(directory).toFile().listFiles().length;
        int nbRuns = 10;

        File[] files = Paths.get(directoryService.getInDataDirectory(AppType.BENCH, Application.DENSITY, HalfFlow.F_PRIME)).toFile().listFiles();

        if (files.length > nbFiles / 10 && !files[nbFiles / nbRuns].getName().contains(".zip"))
            return files[nbFiles / nbRuns].getName();
        else
            return null;
    }

    private void copyFullRecursive(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            File dir = dest;
            dir.mkdirs();

            File[] list = src.listFiles();
            if (list != null)
                for (File fic : list)
                    copyFullRecursive(fic, dir);
        } else {
            Files.copy(src.toPath(), new File(dest, src.getName()).toPath());
        }
    }

    public void renameFiles(String directory, Context context) {
        File dir = new File(directory);

        if (!dir.isDirectory())
            return;

        File[] list = dir.listFiles();

        if (list != null)
            for (File f : list) {
                renameFile(f, context);
            }

        renameFilesInBl(directory, context);
    }

    public void renameFile(String directory, String filename, Context context) {
        renameFile(Paths.get(directory, filename).toFile(), context);
    }

    public void renameFile(File f, Context context) {
        String newName = getNewNamefile(f.getName(), context);
        File f2 = Paths.get(FilenameUtils.getFullPath(f.getPath()), newName).toFile();
        f.renameTo(f2);
    }

    private void renameFilesInBl(String directory, Context context) {
        Optional<String> blFilename = findFilenameByPattern(directory, "*_DPA_F-PRIME_BL.csv");
        if (!blFilename.isPresent()) {
            return;
        }
        List<String> blOld = readFile(directory, blFilename.get());
        Paths.get(directory, blFilename.get()).toFile().delete();
        CsvFileBuilder csvFileBuilder = CsvFileBuilder.build().disableQuote().filepath(directory, blFilename.get());

        for (String s : blOld) {
            csvFileBuilder.row(getNewNamefile(s, context));
        }

        csvFileBuilder.create();
    }

    private String getNewNamefile(String filename, Context contextIn) {
        Context contextOut = contextIn.increase();

        String oldValue1 = String.valueOf(contextIn.getYear()).concat(contextIn.getContextPeriod().getMonth());
        String oldValue2 = String.valueOf(contextIn.getYear()).concat(contextIn.getContextPeriod().getLabel());
        String newValue1 = String.valueOf(contextOut.getYear()).concat(contextOut.getContextPeriod().getMonth());
        String newValue2 = String.valueOf(contextOut.getYear()).concat(contextOut.getContextPeriod().getLabel());

        return filename
                .replace(oldValue1, newValue1)
                .replace(oldValue2, newValue2)
                .replace(contextIn.getContextPeriod().getLabel(), contextOut.getContextPeriod().getLabel());
    }

    public void deleteFile(String directory, String filename) {
        File f = Paths.get(directory, filename).toFile();
        f.delete();
    }
}
