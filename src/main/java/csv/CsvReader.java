package csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CsvReader {
    public static List<List<String>> getCsvData(final String location) {
        final List<List<String>> data = new ArrayList<>();

        try (final Scanner sc = new Scanner(new File(location))) {
            sc.useDelimiter("\\n");
            while (sc.hasNext()) {
                final String next = sc.next();
                final ArrayList<String> line = new ArrayList<>(Arrays.asList(next.split(",")));
                data.add(line);
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }
}
