import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.exception.ReportException;
import fr.hm.pacifique.common.service.FileService;
import fr.hm.pacifique.common.service.TimeoutService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class IPRIME3 {

    public static void main(String[] args) throws IOException, InterruptedException, ReportException {
        TimeoutService timeoutService = new TimeoutService();
        FileService fileService = new FileService(timeoutService);
        FileBenchService benchService = new FileBenchService(null, null);
//        for (int i = 1; i <= 60; i++) {
        //Unzip I-PRIME3 input
        String reception = "D:/pacifique_ged/pacifique-socle-etl-batch/I-PRIME3/ms/sas/reception";

//        benchService.unzipFile(reception, String.format("prime%s.zip", i));
        benchService.unzipFile(reception, String.format("prime59.zip"));
        Process proc = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-PRIME3 -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        reader.lines().forEach(System.out::println);

        BufferedReader reader2 = new BufferedReader(
                new InputStreamReader(proc.getErrorStream()));
        reader2.lines().forEach(System.out::println);

        proc.waitFor();

        benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "*HFP_I-PRIME3_COTISATIONS*"));
        benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "*HFP_I-PRIME3_PRESTATIONS*"));
        benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "*HFP_I-PRIME3_CREX*"));

        //        MOV File
        String inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-PRIME3/out";

        if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-SECONDE3A_TOP.csv").isPresent()) {
            String outS3 = "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE3A/ms/sas/reception/";

            String topIS3 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-SECONDE3A_TOP.csv");
            fileService.moveFile(inDirectory, outS3, topIS3);

            Process proc1 = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-SECONDE3A -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
            BufferedReader reader11 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
            reader11.lines().forEach(System.out::println);
            BufferedReader reader3 = new BufferedReader(new InputStreamReader(proc1.getErrorStream()));
            reader3.lines().forEach(System.out::println);

            proc1.waitFor();
            //   Mov Top pour reprise
            benchService.moveFile(outS3, "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE3A/ms/sas/emission", topIS3);
            //        MOV TOP IS4
            inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE3A/out";
            String outS4 = "D:/pacifique_ged/ADDACTIS/IN_DATA/I_SECONDE4A";
            if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-SECONDE4A_TOP.csv").isPresent()) {
                String topIS4 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-SECONDE4A_TOP.csv");
                fileService.moveFile(inDirectory, outS4, topIS4);
            }
        }
        inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-PRIME3/out";
        if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-QUARTE3A_TOP.csv").isPresent()) {

            String outQ3 = "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE3A/ms/sas/reception";
            String topIQ3 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-QUARTE3A_TOP.csv");

            fileService.moveFile(inDirectory, outQ3, topIQ3);
            Process proc2 = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-QUARTE3A -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
            //        attendre la fin des IQ3 et IS3);
            BufferedReader reader22 = new BufferedReader(new InputStreamReader(proc2.getInputStream()));

            reader22.lines().forEach(System.out::println);

            //Read Error
            BufferedReader reader4 = new BufferedReader(new InputStreamReader(proc2.getErrorStream()));
            reader4.lines().forEach(System.out::println);
            proc2.waitFor();
            // mov top Source pour reprise
            benchService.moveFile(outQ3, "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE3A/ms/sas/emission", topIQ3);

            //        MOV TOP IQ4
            inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE3A/out";
            if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-QUARTE4A_TOP.csv").isPresent()) {
                String outQ4 = "D:/pacifique_ged/ADDACTIS/IN_DATA/I_QUARTE4A";
                String topIQ4 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-QUARTE4A_TOP.csv");
                fileService.moveFile(inDirectory, outQ4, topIQ4);
            }
        }
//        Thread.sleep(90000);
//        }
    }
}
