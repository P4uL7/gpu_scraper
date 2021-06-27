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
import static utils.Constants.*;


public class LineChartApp extends Application {
    private static final List<List<String>> csvData = CsvReader.getCsvData(CSV_FILE);
    public static final List<String> headers = csvData.get(0);

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Steam Hardware Survey Data");

        final Set<String> categories = new LinkedHashSet<>();
        final List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        final int startIndex = DATA_2020.getSTART_INDEX() + 10;
        final int endIndex = DATA_2021.getEND_INDEX();

        // GPU popularity chart for user defined gpus
        final Set<String> gpuList = new HashSet<>(Arrays.asList("3050", "3060", "3070", "3080", "3090"));
        createGpuSeries(categories, seriesList, startIndex, endIndex, gpuList, true);

//        // GPU popularity chart
//        final Set<String> gpuList = getTopXGpus(csvData, 8, PopularitySort.SUM, false, startIndex, endIndex);
//        createGpuSeries(categories, seriesList, startIndex, endIndex, gpuList, false);

//        // Brand popularity chart
//        final Double[][] brandPopularity = getBrandPopularity(csvData, startIndex, endIndex);
//        createBrandSeries(categories, seriesList, startIndex, endIndex, brandPopularity);

        final ObservableList<String> c = FXCollections.observableArrayList(categories);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Percentage");
        xAxis.setCategories(c);
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("GPU Popularity");
        lineChart.verticalGridLinesVisibleProperty().setValue(Boolean.FALSE);
        final Scene scene = new Scene(lineChart, 1280, 720);
        lineChart.getData().addAll(seriesList);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/linechart.css")).toExternalForm()); //fix brandPopularity colors
        stage.setScene(scene);
        stage.show();

        // add tooltips
        addTooltips(lineChart);

        // hide data points
//        hideDataPoints(lineChart);
    }

    private void createGpuSeries(final Set<String> categories, final List<XYChart.Series<String, Number>> seriesList, final int startIndex, final int endIndex, final Set<String> gpuList, final boolean userDefinedList) {
        for (int g = 1; g < csvData.size(); g++) {
            final List<String> gpu = csvData.get(g);
            final XYChart.Series<String, Number> series = new XYChart.Series<>();
            final String gpuName = gpu.get(1);
            series.setName(gpuName);
            boolean addGpu = false;

            for (int i = startIndex; i <= endIndex; i++) {
                if (userDefinedList) {
                    boolean found = false;
                    for (final String iterGpuName : gpuList) {
                        if (gpuName.toLowerCase().contains(iterGpuName.toLowerCase())) {
                            found = true;
                            break;
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
