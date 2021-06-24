package csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CsvReader {
    public static List<List<String>> getCsvData(String location) {
        List<List<String>> data = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(location))) {
            sc.useDelimiter("\\n");
            while (sc.hasNext()) {
                String next = sc.next();
                ArrayList<String> line = new ArrayList<>(Arrays.asList(next.split(",")));
                data.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }
}
