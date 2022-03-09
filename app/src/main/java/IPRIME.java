import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.exception.ReportException;
import fr.hm.pacifique.common.service.FileService;
import fr.hm.pacifique.common.service.TimeoutService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class IPRIME {

    public static void main(String[] args) throws IOException, InterruptedException, ReportException {
        TimeoutService timeoutService = new TimeoutService();
        FileService fileService = new FileService(timeoutService);
        FileBenchService benchService = new FileBenchService(null, null);
        for (int i = 9; i <= 60; i++) {
            //Unzip I-PRIME input
            String reception = "D:/pacifique_ged/pacifique-socle-etl-batch/I-PRIME/ms/sas/reception";

            benchService.unzipFile(reception, String.format("prime%s", i));
            Process proc = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-PRIME -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            reader.lines().forEach(System.out::println);

            BufferedReader reader2 = new BufferedReader(
                    new InputStreamReader(proc.getErrorStream()));
            reader2.lines().forEach(System.out::println);

            proc.waitFor();

            benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "*DCB_I-PRIME_COTISATIONS*"));
            benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "*DCB_I-PRIME_PRESTATIONS*"));
            benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "*DCB_I-PRIME_CREX*"));

            //        MOV File
            String inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-PRIME/out";

            if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-SECONDE3_TOP.csv").isPresent()) {
                String outS3 = "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE3/ms/sas/reception/";

                String topIS3 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-SECONDE3_TOP.csv");
                fileService.moveFile(inDirectory, outS3, topIS3);

                Process proc1 = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-SECONDE3 -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
                BufferedReader reader11 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
                reader11.lines().forEach(System.out::println);
                BufferedReader reader3 = new BufferedReader(new InputStreamReader(proc1.getErrorStream()));
                reader3.lines().forEach(System.out::println);

                proc1.waitFor();
                //   Mov Top pour reprise
                benchService.moveFile(outS3, "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE3/ms/sas/emission", topIS3);
                //        MOV TOP IS4
                inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE3/out";
                String outS4 = "D:/pacifique_ged/ADDACTIS/IN_DATA/I_SECONDE4";
                if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-SECONDE4_TOP.csv").isPresent()) {
                    String topIS4 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-SECONDE4_TOP.csv");
                    fileService.moveFile(inDirectory, outS4, topIS4);
                }
            }
            inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-PRIME/out";
            if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-QUARTE3_TOP.csv").isPresent()) {

                String outQ3 = "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE3/ms/sas/reception";
                String topIQ3 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-QUARTE3_TOP.csv");

                fileService.moveFile(inDirectory, outQ3, topIQ3);
                Process proc2 = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-QUARTE3 -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
                //        attendre la fin des IQ3 et IS3);
                BufferedReader reader22 = new BufferedReader(new InputStreamReader(proc2.getInputStream()));

                reader22.lines().forEach(System.out::println);

                //Read Error
                BufferedReader reader4 = new BufferedReader(new InputStreamReader(proc2.getErrorStream()));
                reader4.lines().forEach(System.out::println);
                proc2.waitFor();
                // mov top Source pour reprise
                benchService.moveFile(outQ3, "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE3/ms/sas/emission", topIQ3);

                //        MOV TOP IQ4
                inDirectory = "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE3/out";
                if (fileService.findFilenameByPattern(inDirectory, "*_OMP_I-QUARTE4_TOP.csv").isPresent()) {
                    String outQ4 = "D:/pacifique_ged/ADDACTIS/IN_DATA/I_QUARTE4";
                    String topIQ4 = fileService.getFilenameByPattern(inDirectory, "*_OMP_I-QUARTE4_TOP.csv");
                    fileService.moveFile(inDirectory, outQ4, topIQ4);
                    Thread.sleep(480000);
                }
            }
        }
    }
}
