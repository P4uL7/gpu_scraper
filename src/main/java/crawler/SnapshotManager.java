package crawler;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SnapshotManager {

    public static void getMostRecent(String location, boolean copyToNewFolder) {

        HashMap<String, String> latestSnapshots = new HashMap<>();

        File f = new File(location);
        List<String> list = Arrays.asList(f.list());
        Collections.sort(list);

        System.out.println("Initial snapshot count: " + list.size());
        for (String snapshot : list) {
            latestSnapshots.put(snapshot.substring(0, 6), snapshot);
        }

        System.out.println("Latest snapshot count: " + latestSnapshots.size());

        if (copyToNewFolder) {
            File targetDirectory = new File(location + File.separator + "latest");
            for (String snap : latestSnapshots.values()) {
                try {
                    FileUtils.copyFileToDirectory(new File(location + File.separator + snap), targetDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
