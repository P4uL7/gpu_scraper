package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Month;

import java.io.File;
import java.util.HashMap;

public class Constants {

    public static final String LOCATION = "." + File.separator + "gpu_data";
    public static final String LATEST = LOCATION + File.separator + "latest";

    public static final String CSV_FILE = "." + File.separator + "gpu_data_v4_final.csv";
    public static final String CSV_FILE_TEST = "." + File.separator + "gpu_data_test.csv";
    public static final String[] YEAR = {"2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021"};
    public static final HashMap<String, Integer> monthIndex = new HashMap<>();
    public static final YearData DATA_ALL = new YearData(3, 147);
    public static final YearData DATA_2008 = new YearData(3, 8);
    public static final YearData DATA_2009 = new YearData(9, 20);
    public static final YearData DATA_2010 = new YearData(21, 32);
    public static final YearData DATA_2011 = new YearData(33, 44);
    public static final YearData DATA_2012 = new YearData(45, 56);
    public static final YearData DATA_2013 = new YearData(57, 68);
    public static final YearData DATA_2014 = new YearData(69, 79);
    public static final YearData DATA_2015 = new YearData(80, 88);
    public static final YearData DATA_2016 = new YearData(89, 100);
    public static final YearData DATA_2017 = new YearData(101, 107);
    public static final YearData DATA_2018 = new YearData(108, 119);
    public static final YearData DATA_2019 = new YearData(120, 131);
    public static final YearData DATA_2020 = new YearData(132, 143);
    public static final YearData DATA_2021 = new YearData(144, 147);

    static {
        int i = 1;
        for (Month m : Month.values()) {
            monthIndex.put(m.toString(), i++);
        }
    }
    public enum PopularitySort {
        SUM, MAX
    }

    @Getter
    @AllArgsConstructor
    public static class YearData {
        private final int START_INDEX;
        private final int END_INDEX;
    }
}
