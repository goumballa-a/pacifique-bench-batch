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

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class FileBenchService extends FileService {
    private final DirectoryBenchService directoryBenchService;
    public FileBenchService(TimeoutService timeoutService, DirectoryBenchService directoryBenchService) {
        super(timeoutService);
        this.directoryBenchService = directoryBenchService;
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

    public String getContext(String directory, Application application, HalfFlow halfFlow) {
        int nbFiles = Paths.get(directory).toFile().listFiles().length;
        int nbRuns = 10;

        File[] files = Paths.get(directoryBenchService.getInDataDirectory(AppType.BENCH, application, halfFlow)).toFile().listFiles();

        if (files.length > nbFiles  && !files[nbFiles -1].getName().contains(".zip"))
            return files[nbFiles -1].getName();
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

    public void renameFiles(String directory, Context contextIn) {
        File dir = new File(directory);
        Context contextOut = contextIn.increase();

        if (!dir.isDirectory())
            return;

        File[] list = dir.listFiles();

        if (list != null)
            for (File f : list) {
                renameFile(f, contextIn, contextOut);
            }

        renameFilesInBl(directory, contextIn, contextOut);
    }

    public void renameFile(String directory, String filename, Context contextIn) {
        renameFile(Paths.get(directory, filename).toFile(), contextIn);
    }

    public void renameFile(File fIn, Context contextIn) {
        String newName = getNewNamefile(fIn.getName(), contextIn, contextIn.increase());
        File fOut = Paths.get(FilenameUtils.getFullPath(fIn.getPath()), newName).toFile();

        fIn.renameTo(fOut);
    }

    public void renameFile(File fIn, Context contextIn, Context contextOut) {
        String newName = getNewNamefile(fIn.getName(), contextIn, contextOut);
        File fTemp = Paths.get(FilenameUtils.getFullPath(fIn.getPath()), "temp.csv").toFile();
        File fOut = Paths.get(FilenameUtils.getFullPath(fIn.getPath()), newName).toFile();

        String oldValue1 = String.valueOf(contextIn.getYear()).concat(contextIn.getContextPeriod().getMonth());
        String oldValue2 = String.valueOf(contextIn.getYear()).concat(contextIn.getContextPeriod().getLabel());
        String newValue1 = String.valueOf(contextOut.getYear()).concat(contextOut.getContextPeriod().getMonth());
        String newValue2 = String.valueOf(contextOut.getYear()).concat(contextOut.getContextPeriod().getLabel());

        try (BufferedReader br = new BufferedReader(new FileReader(fIn)); BufferedWriter bw = new BufferedWriter(new FileWriter(fTemp))) {
            String st;
            while ((st = br.readLine()) != null) {
                bw.write(st.replace(oldValue1, newValue1)
                        .replace(oldValue2, newValue2));
                bw.newLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        fIn.delete();
        fTemp.renameTo(fOut);
        fTemp.delete();
    }

    private void renameFilesInBl(String directory, Context contextIn, Context contextOut) {
        Optional<String> blFilename = findFilenameByPattern(directory, "*_DHM_F-PRIME3_BL.csv");
        if (!blFilename.isPresent()) {
            return;
        }
        List<String> blOld = readFile(directory, blFilename.get());
        Paths.get(directory, blFilename.get()).toFile().delete();
        CsvFileBuilder csvFileBuilder = CsvFileBuilder.build().disableQuote().filepath(directory, blFilename.get());

        for (String s : blOld) {
            csvFileBuilder.row(getNewNamefile(s, contextIn, contextOut));
        }

        csvFileBuilder.create();
    }

    private String getNewNamefile(String filename, Context contextIn, Context contextOut) {
        String oldValue1 = String.valueOf(contextIn.getYear()).concat(contextIn.getContextPeriod().getMonth());
        String oldValue2 = String.valueOf(contextIn.getYear()).concat(contextIn.getContextPeriod().getLabel());
        String newValue1 = String.valueOf(contextOut.getYear()).concat(contextOut.getContextPeriod().getMonth());
        String newValue2 = String.valueOf(contextOut.getYear()).concat(contextOut.getContextPeriod().getLabel());

        return filename
                .replace(oldValue1, newValue1)
                .replace(oldValue2, newValue2)
                .replace("_" + contextIn.getContextPeriod().getLabel(), "_" + contextOut.getContextPeriod().getLabel());
    }

    public void deleteFile(String directory, String filename) {
        File f = Paths.get(directory, filename).toFile();
        f.delete();
    }
}
