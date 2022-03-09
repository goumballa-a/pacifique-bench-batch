import fr.hm.pacifique.bench.service.FileBenchService;
import fr.hm.pacifique.common.exception.ReportException;
import fr.hm.pacifique.common.service.FileService;
import fr.hm.pacifique.common.service.TimeoutService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class I58 {

    public static void main(String[] args) throws IOException, InterruptedException, ReportException {
//        TimeoutService timeoutService = new TimeoutService();
//        FileService fileService = new FileService(timeoutService);
//        FileBenchService benchService = new FileBenchService(null, null);
//        for (int i = 0; i < 2; i++) {
//            //Unzip I input
//            String reception = "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE5/ms/sas/reception";
//
//            benchService.unzipFile(reception, String.format("iseconde5%s.zip", i));
//            Process proc = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-SECONDE5 -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            reader.lines().forEach(System.out::println);
//
//            BufferedReader reader2 = new BufferedReader(
//                    new InputStreamReader(proc.getErrorStream()));
//            reader2.lines().forEach(System.out::println);
//
//            proc.waitFor();
//            benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "OMP_I-SECONDE5_TOP.csv"));
//
//
//            reception = "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE5/ms/sas/reception";
//
//            benchService.unzipFile(reception, String.format("iquarte5%s.zip", i));
//            Process proc2 = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-QUARTE5 -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
//            BufferedReader reader3 = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
//            reader3.lines().forEach(System.out::println);
//
//            BufferedReader reader4 = new BufferedReader(
//                    new InputStreamReader(proc2.getErrorStream()));
//            reader4.lines().forEach(System.out::println);
//
//            proc2.waitFor();
//            benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "OMP_I-QUARTE5_TOP.csv"));
//
//            //  MOV File T5 Seconde1
//            if (fileService.findFilenameByPattern("D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE5/out", "*DPA_T5-SECONDE1-PREST-COT_TOP.csv").isPresent()) {
//                String out = "D:/pacifique_ged/PACIFIQUE/IN_USER";
//
//                String top = fileService.getFilenameByPattern("D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE5/out", "*DPA_T5-SECONDE1-PREST-COT_TOP.csv");
//                fileService.moveFile("D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE5/out", out, top);
//            }
//
//            if (fileService.findFilenameByPattern("D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE5/out", "*DPA_T5-SECONDE1-PREST-COT_TOP.csv").isPresent()) {
//                String out = "D:/pacifique_ged/PACIFIQUE/IN_USER";
//
//                String top = fileService.getFilenameByPattern("D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE5/out", "*DPA_T5-SECONDE1-PREST-COT_TOP.csv");
//                fileService.moveFile("D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE5/out", out, top);
//            }
//
//            //Unzip I input
//            reception = "D:/pacifique_ged/pacifique-socle-etl-batch/I-SECONDE8/ms/sas/reception";
//
//            benchService.unzipFile(reception, String.format("iseconde8%s.zip", i));
//            Process proc3 = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-SECONDE8 -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
//            BufferedReader reader5 = new BufferedReader(new InputStreamReader(proc3.getInputStream()));
//            reader5.lines().forEach(System.out::println);
//
//            BufferedReader reader6 = new BufferedReader(
//                    new InputStreamReader(proc3.getErrorStream()));
//            reader6.lines().forEach(System.out::println);
//
//            proc3.waitFor();
//            benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "OMP_I-SECONDE8_TOP.csv"));
//
//            reception = "D:/pacifique_ged/pacifique-socle-etl-batch/I-QUARTE8/ms/sas/reception";
//
//            benchService.unzipFile(reception, String.format("iquarte8%s.zip", i));
//            Process proc4 = Runtime.getRuntime().exec("java -Dserver.exec.jobName=I-QUARTE8 -jar D:\\PACIFIQUE\\BENCH2\\BENCH.jar");
//            BufferedReader reader7 = new BufferedReader(new InputStreamReader(proc4.getInputStream()));
//            reader7.lines().forEach(System.out::println);
//
//            BufferedReader reader8 = new BufferedReader(
//                    new InputStreamReader(proc4.getErrorStream()));
//            reader8.lines().forEach(System.out::println);
//
//            proc4.waitFor();
//            benchService.deleteFile(reception, fileService.getFilenameByPattern(reception, "OMP_I-QUARTE8_TOP.csv"));
//        }
    }
}
