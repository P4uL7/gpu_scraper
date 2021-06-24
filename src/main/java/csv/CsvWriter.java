package csv;

import model.GPU;
import model.GpuList;
import model.Month;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import utils.Constants;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CsvWriter {

    public static void write(String location) throws IOException {

        List<String> HEADER = new ArrayList<>();
        HEADER.add("id");
        HEADER.add("Name");
        HEADER.add("model.Brand");
        for (String year : Constants.YEAR) {
            for (Month month : Month.values()) {
                HEADER.add(month.toString() + "-" + year);
            }
        }

        String[] head = new String[HEADER.size()];
        int tmp = 0;
        for (String item : HEADER) {
            head[tmp++] = item;
        }


        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(location));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader(head));

        ) {

            int i = 0;
            for (GPU gpu : GpuList.getGpuList()) {
                List<String> gpuRecord = new ArrayList<>();
                gpuRecord.add("" + i++);
                gpuRecord.add(gpu.getName());
                gpuRecord.add(gpu.getBrand().toString());
                for (Map.Entry<String, TreeMap<Month, String>> gpuYear :
                        gpu.getPopularityMap().entrySet()) {

                    for (Map.Entry<Month, String> gpuMonth : gpuYear.getValue().entrySet()) {
                        gpuRecord.add(gpuMonth.getValue().replace("%", "")); // percentage
                    }
                }

                csvPrinter.printRecord(gpuRecord);
            }

            csvPrinter.flush();
        }
    }
}
