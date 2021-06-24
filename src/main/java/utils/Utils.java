package utils;

import model.Month;

public class Utils {

    public static Month month(String string) {
        for (Month m : Month.values()) {
            if (m.toString().equalsIgnoreCase(string)) {
                return m;
            }
        }
        throw new RuntimeException("Invalid month given: " + string);
    }
}
