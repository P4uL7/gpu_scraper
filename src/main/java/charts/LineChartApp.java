package charts;

import csv.CsvReader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.*;

import static utils.ChartUtils.addTooltips;
import static utils.ChartUtils.hideDataPoints;
import static utils.Constants.*;


public class LineChartApp extends Application {
    private static final List<List<String>> csvData = CsvReader.getCsvData(CSV_FILE);
    public static final List<String> headers = csvData.get(0);

    private static final int startIndex = DATA_2019.getSTART_INDEX();
    private static final int endIndex = DATA_2021.getEND_INDEX();

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Steam Hardware Survey Data");

        final Set<String> categories = new LinkedHashSet<>();
        final List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        final boolean isBrandPopularityChart = false;

        // ----------------------------  GPU popularity chart for user defined gpus  ----------------------------
        final Set<String> series8000 = new HashSet<>(Arrays.asList("GT 8", "GeForce 8")); // with and without "M"
        final Set<String> series9000 = new HashSet<>(Arrays.asList("GT 9", "GeForce 9")); // with and without "M"
        final Set<String> series200 = new HashSet<>(Arrays.asList("GTX 2", "GT 2", "GeForce 2")); // with and without "M"
        final Set<String> series400 = new HashSet<>(Arrays.asList("GTX 4", "GT 4", "GeForce 4")); // with and without "M"
        final Set<String> series500 = new HashSet<>(Arrays.asList("GTX 5", "GT 5", "GeForce 5")); // with and without "M"
        final Set<String> series600 = new HashSet<>(Arrays.asList("GTX 6", "GT 6", "GeForce 6")); // with and without "M"
        final Set<String> series700 = new HashSet<>(Arrays.asList("GTX 7", "GT 7", "GeForce 7")); // with and without "M"
        final Set<String> series800M = new HashSet<>(Arrays.asList("GTX 8", "GT 8", "GeForce 8")); // only M
        final Set<String> series900 = new HashSet<>(Arrays.asList("GTX 9", "GT 9", "GeForce 9")); // with and without "M"
        final Set<String> gtx10series = new HashSet<>(Arrays.asList("GTX 10", "GT 10"));
        final Set<String> gtx16series = new HashSet<>(Arrays.asList("GTX 16"));
        final Set<String> rtx20series = new HashSet<>(Arrays.asList("RTX 20"));
        final Set<String> rtx30series = new HashSet<>(Arrays.asList("RTX 30"));

        final Set<String> topPopularityPerSeries = new HashSet<>(Arrays.asList("Nvidia GeForce 8800", "Nvidia GeForce 9800", "Nvidia GeForce GTX 260",
                "Nvidia GeForce GTX 460", "Nvidia GeForce GTX 560", "Nvidia GeForce GTX 660", "Nvidia GeForce GTX 750 Ti", "Nvidia GeForce GTX 760",
                "Nvidia GeForce GTX 960", "Nvidia GeForce GTX 970", "Nvidia GeForce GTX 1050 Ti", "Nvidia GeForce GTX 1060", "Nvidia GeForce GTX 1650",
                "Nvidia GeForce RTX 2060", "Nvidia GeForce RTX 3070"));

        final Set<String> topPopularityOld = new HashSet<>(Arrays.asList("Nvidia GeForce 8800", "Nvidia GeForce 9800", "Nvidia GeForce GTX 260",
                "Nvidia GeForce GTX 460", "Nvidia GeForce GTX 560", "Nvidia GeForce GTX 660", "Nvidia GeForce GTX 760"));

        final Set<String> topPopularityCurrent = new HashSet<>(Arrays.asList("Nvidia GeForce GTX 960", "Nvidia GeForce GTX 970", "Nvidia GeForce GTX 1050 Ti", "Nvidia GeForce GTX 1060", "Nvidia GeForce GTX 1650",
                "Nvidia GeForce RTX 2060", "Nvidia GeForce RTX 3070", "Nvidia GeForce GTX 750 Ti"));
        createGpuSeries(categories, seriesList, startIndex, endIndex, gtx16series, true, false);
        // ------------------------------------------------------------------------------------------------------

        // ---------------------------------------  GPU popularity chart  ---------------------------------------
//        final Set<String> gpuList = getTopXGpus(csvData, 8, PopularitySort.SUM, false, startIndex, endIndex);
//        createGpuSeries(categories, seriesList, startIndex, endIndex, gpuList, false, false);
        // ------------------------------------------------------------------------------------------------------

        // --------------------------------------  Brand popularity chart  --------------------------------------
//        isBrandPopularityChart = true;
//        final Double[][] brandPopularity = getBrandPopularity(csvData, startIndex, endIndex);
//        createBrandSeries(categories, seriesList, startIndex, endIndex, brandPopularity);
        // ------------------------------------------------------------------------------------------------------

        final ObservableList<String> c = FXCollections.observableArrayList(categories);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        xAxis.setCategories(c);
        yAxis.setLabel("Percentage");

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("GPU Popularity");
        lineChart.verticalGridLinesVisibleProperty().setValue(Boolean.FALSE);
        lineChart.getData().addAll(seriesList);

        final Scene scene = new Scene(lineChart, 950, 900);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource(isBrandPopularityChart ? "/lineChart.css" : "/whiteBackground.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();

        // add tooltips
        addTooltips(lineChart, false);

        // hide data points
        hideDataPoints(lineChart, false);
    }

    private void createGpuSeries(final Set<String> categories, final List<XYChart.Series<String, Number>> seriesList, final int startIndex, final int endIndex, final Set<String> gpuList, final boolean userDefinedList, final boolean strict) {
        categories.clear();
        seriesList.clear();
        for (int g = 1; g < csvData.size(); g++) {
            final List<String> gpu = csvData.get(g);
            final XYChart.Series<String, Number> series = new XYChart.Series<>();
            final String gpuName = gpu.get(1);
            series.setName(gpuName);
            boolean addGpu = false;

            for (int i = startIndex; i <= endIndex; i++) {
                if (userDefinedList) {
                    boolean found = false;
                    if (strict) {
                        for (final String iterGpuName : gpuList) {
                            if (gpuName.equalsIgnoreCase(iterGpuName) /*&& !gpuName.contains("00")*/ /* && !gpuName.endsWith("M")*/) {
                                found = true;
                                break;
                            }
                        }
                    } else {
                        for (final String iterGpuName : gpuList) {
                            if (gpuName.toLowerCase().contains(iterGpuName.toLowerCase()) /*&& !gpuName.contains("00")*/ /* && !gpuName.endsWith("M")*/) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        continue;
                    }
                } else if (!gpuList.contains(gpuName)) {
                    continue;
                }
                categories.add(headers.get(i));
                if (!gpu.get(i).equals("-")) {
                    final double value = Double.parseDouble(gpu.get(i));
                    final XYChart.Data<String, Number> data = new XYChart.Data<>(headers.get(i), value);
                    series.getData().add(data);
                    addGpu = true;
                }
            }
            if (addGpu) {
                seriesList.add(series);
            }
        }
    }

    private void createBrandSeries(final Set<String> categories, final List<XYChart.Series<String, Number>> seriesList, final int startIndex, final int endIndex, final Double[][] brandPopularity) {
        final Map<Integer, String> brandMap = new HashMap<>();
        brandMap.put(0, "NVIDIA");
        brandMap.put(1, "AMD");
        brandMap.put(2, "INTEL");

        for (int brand = 0; brand < brandPopularity.length; brand++) {
            final XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(brandMap.get(brand));

            for (int i = startIndex; i <= endIndex; i++) {
                categories.add(headers.get(i));
                final XYChart.Data<String, Number> data = new XYChart.Data<>(headers.get(i), brandPopularity[brand][i - startIndex]);
                series.getData().add(data);
            }
            seriesList.add(series);
        }
    }

}
