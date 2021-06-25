package utils;

import model.Month;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public static Month month(String string) {
        for (Month m : Month.values()) {
            if (m.toString().equalsIgnoreCase(string)) {
                return m;
            }
        }
        throw new RuntimeException("Invalid month given: " + string);
    }

    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
