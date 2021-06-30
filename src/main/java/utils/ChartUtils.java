package utils;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static utils.Constants.PopularitySort;
import static utils.Utils.roundDouble;

public abstract class ChartUtils {

    public static Set<String> getTopXGpus(final List<List<String>> csvData, final int X, final PopularitySort sort, final boolean includeSeries, final int startDate, final int endDate) {
        final HashMap<String, Number> popularityList = new HashMap<>();
        for (int g = 1; g < csvData.size(); g++) {
            final List<String> gpu = csvData.get(g);

            // TODO: uncomment for mobile GPUS
//            if (!gpu.get(1).endsWith("M") && !gpu.get(1).endsWith("MX")) {
//                continue;
//            }

            // TODO: uncomment to see AMD GPUS only
//            if (!gpu.get(2).equals("AMD")) {
//                continue;
//            }

            if (!includeSeries && gpu.get(1).toLowerCase().contains("series")) {
                continue;
            }

            double totalScore = 0d;
            for (int i = startDate; i <= endDate; i++) {
                final String value = gpu.get(i);
                if (!value.equals("-")) {
                    final double doubleValue = Double.parseDouble(value);
                    if (sort == PopularitySort.SUM) {
                        totalScore += doubleValue;
                    } else if (sort == PopularitySort.MAX) {
                        if (totalScore < doubleValue) {
                            totalScore = doubleValue;
                        }
                    }
                }
            }

            popularityList.put(gpu.get(1), totalScore);
        }

        return popularityList.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> -(new BigDecimal(o1.toString()).compareTo(new BigDecimal(o2.toString())))))
                .limit(X)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)).keySet();
    }

    public static Double[][] getBrandPopularity(final List<List<String>> csvData, final int startIndex, final int endIndex) {
        final Double[][] brandPopularity = new Double[3][endIndex - startIndex + 1];
        for (final Double[] doubles : brandPopularity) {
            Arrays.fill(doubles, 0d);
        }
        final Map<String, Integer> brandMap = new HashMap<>();
        brandMap.put("NVIDIA", 0);
        brandMap.put("AMD", 1);
        brandMap.put("INTEL", 2);
        for (int g = 1; g < csvData.size(); g++) {
            final List<String> gpu = csvData.get(g);
            final String brand = gpu.get(2);
            for (int i = startIndex; i <= endIndex; i++) {
                if (!gpu.get(i).contains("-")) {
                    brandPopularity[brandMap.get(brand)][i - startIndex] += Double.parseDouble(gpu.get(i));
                }
            }
        }

        for (int i = 0; i < brandPopularity.length; i++) {
            for (int j = 0; j < brandPopularity[i].length; j++) {
                brandPopularity[i][j] = roundDouble(brandPopularity[i][j], 2);
            }
        }
        return brandPopularity;
    }

    public static void addTooltips(final XYChart<String, Number> lineChart, final boolean roundValues) {
        for (final XYChart.Series<String, Number> s : lineChart.getData()) {
            for (final XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        s.getName() + "\n" +
                                "Date: " + d.getXValue() + "\n" +
                                "Popularity: " + (roundValues ? roundDouble(d.getYValue().doubleValue(), 2) : d.getYValue().doubleValue()) + "%"));
            }
        }
    }

    public static void hideDataPoints(final XYChart<String, Number> lineChart, final boolean hide) {
        if (!hide) {
            return;
        }
        for (final XYChart.Series<String, Number> s : lineChart.getData()) {
            for (final XYChart.Data<String, Number> d : s.getData()) {
                d.getNode().setVisible(false);
            }
        }
    }
}
