package crawler;

import model.GPU;
import model.GpuList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Constants;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnapshotParser {

    public static final String LAST_OLD_SNAPSHOT = "20100531051401.html";

    public static void parseSnapshot(String location, String snapshot) {
        String snapshotLocation = location + File.separator + snapshot;
        boolean oldFormat = snapshot.compareTo(LAST_OLD_SNAPSHOT) <= 0;
        File snap = new File(snapshotLocation);
        Document doc = null;
        try {
            doc = Jsoup.parse(snap, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert doc != null;

        Element h1 = doc.select("h1").get(0);
        String date = h1.text().split(":")[1].trim();
        String month = date.split(" ")[0].trim();
        String year = date.split(" ")[1].trim();

//        System.out.println(month + " " + year);

        Element elem = doc.select("div[id=sub_stats]").get(0);

        Element allGpuTable = elem.select("div[class=substats_col_left col_header]").get(1);

        List<String> headers = new ArrayList<>();
        while (!allGpuTable.text().isEmpty()) {
            headers.add(allGpuTable.text());
            allGpuTable = allGpuTable.nextElementSibling();
        }

        allGpuTable = allGpuTable.nextElementSibling();
        if (oldFormat) {
            allGpuTable = allGpuTable.nextElementSibling();
        }
//        System.out.println(headers);

        List<String[]> gpus = new ArrayList<>();
        String[] gpu = new String[headers.size()];
        int index = 0;
        if (oldFormat) { // OLD format parser
            while (!allGpuTable.text().contains("Other")) {
                if (allGpuTable.text().isEmpty()) {
                    index = 0;
                    gpus.add(gpu);
                    gpu = new String[headers.size()];
                } else {
                    if (index != headers.size()) {
                        gpu[index++] = allGpuTable.text();
                    }
                }
                allGpuTable = allGpuTable.nextElementSibling();
            }
        } else { // new format parser
            gpus = new ArrayList<>();
            while (!allGpuTable.text().contains("Other")) {
                gpu = new String[headers.size()];
                Elements children = allGpuTable.children();
                index = 0;
                for (Element child : children) {
                    if (index == headers.size()) {
                        continue;
                    }
                    gpu[index++] = child.text();
                }

                gpus.add(gpu);

                allGpuTable = allGpuTable.nextElementSibling();
            }
        }

//        for (String[] s : gpus) {
//            System.out.println(Arrays.toString(s));
//        }

        loadGpusIntoList(headers, gpus, month, year);
    }

    private static void loadGpusIntoList(List<String> headers, List<String[]> gpus, String month, String year) {
        String currentHeaderMonth = headers.get(headers.size() - 1).toUpperCase();
        for (String[] gpu : gpus) {
            GPU currentGPU = null;
            if (GpuList.getGpu(gpu[0]) == null) {
                currentGPU = new GPU(gpu[0]);
                GpuList.addGpu(currentGPU);
            } else {
                currentGPU = GpuList.getGpu(gpu[0]);
            }

            for (int i = 1; i < headers.size(); i++) {
                String mon = headers.get(i);
                String tempYear = year;
                if (Constants.monthIndex.get(mon.toUpperCase()) > Constants.monthIndex.get(currentHeaderMonth)) {
                    tempYear = "" + (Integer.parseInt(year) - 1);
                }
                if (!gpu[i].equals("-"))
                    currentGPU.addInfo(tempYear, Utils.month(mon), gpu[i]);
            }
        }
    }
}
