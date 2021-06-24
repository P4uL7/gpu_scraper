import crawler.SnapshotParser;
import csv.CsvWriter;

import java.io.File;
import java.io.IOException;

import static utils.Constants.CSV_FILE_TEST;
import static utils.Constants.LOCATION_TEST;

public class Main {
    //Start date = jul 2008

    public static void main(String[] args) {
        downloadSnapshots();
        getMostRecentSnapshots();

        parseSnapshotData();

        writeDataToCSV();
    }

    private static void downloadSnapshots() {
        crawler.SnapshotDownloader.start(LOCATION_TEST);
    }

    private static void getMostRecentSnapshots() {
        crawler.SnapshotManager.getMostRecent(LOCATION_TEST, true);
    }

    private static void parseSnapshotData() {

        crawler.SnapshotParser.parseSnapshot(LOCATION_TEST, "20100531051401.html"); // LAST OLD
        crawler.SnapshotParser.parseSnapshot(LOCATION_TEST, "20100623080726.html"); // FIRST NEW

        File f = new File(LOCATION_TEST);
        String[] list = f.list();

        assert list != null;
        for (String snap : list) {
            SnapshotParser.parseSnapshot(LOCATION_TEST, snap);
        }

//        List<GPU> gpuList = model.GpuList.getGpuList();
//        System.out.println(gpuList);
    }

    private static void writeDataToCSV() {
        try {
            CsvWriter.write(CSV_FILE_TEST);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        List<List<String>> csvData = CsvReader.getCsvData(CSV_FILE_TEST);
//        System.out.println(csvData);
    }
}
