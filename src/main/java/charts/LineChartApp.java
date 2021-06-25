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

import static utils.ChartUtils.*;
import static utils.Constants.*;


public class LineChartApp extends Application {
    private static final List<List<String>> csvData = CsvReader.getCsvData(CSV_FILE);
    public static final List<String> headers = csvData.get(0);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Steam Hardware Survey Data");

        final Set<String> categories = new LinkedHashSet<>();
        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        int startIndex = DATA_2008.getSTART_INDEX();
        int endIndex = DATA_2021.getEND_INDEX();

        // GPU popularity chart
        LinkedHashMap<String, Number> topXGpus = getTopXGpus(csvData, 8, PopularitySort.SUM, false, startIndex, endIndex);
        createGpuSeries(categories, seriesList, startIndex, endIndex, topXGpus);

//        // Brand popularity chart
//        Double[][] brandPopularity = getBrandPopularity(csvData, startIndex, endIndex);
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
        Scene scene = new Scene(lineChart, 1280, 720);
        lineChart.getData().addAll(seriesList);
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/linechart.css")).toExternalForm()); //TODO only for brands
        stage.setScene(scene);
        stage.show();

        // add tooltips
        addTooltips(lineChart);

        // hide data points
        hideDataPoints(lineChart);
    }

    private void createGpuSeries(Set<String> categories, List<XYChart.Series<String, Number>> seriesList, int startIndex, int endIndex, LinkedHashMap<String, Number> topXGpus) {
        for (int g = 1; g < csvData.size(); g++) {
            List<String> gpu = csvData.get(g);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(gpu.get(1));
            boolean addGpu = false;

            for (int i = startIndex; i <= endIndex; i++) {
                if (!topXGpus.containsKey(gpu.get(1))) {
                    continue;
                }
                categories.add(headers.get(i));
                if (!gpu.get(i).equals("-")) {
                    double value = Double.parseDouble(gpu.get(i));
                    XYChart.Data<String, Number> data = new XYChart.Data<>(headers.get(i), value);
                    series.getData().add(data);
                    addGpu = true;
                }
            }
            if (addGpu) {
                seriesList.add(series);
            }
        }
    }

    private void createBrandSeries(Set<String> categories, List<XYChart.Series<String, Number>> seriesList, int startIndex, int endIndex, Double[][] brandPopularity) {
        Map<Integer, String> brandMap = new HashMap<>();
        brandMap.put(0, "NVIDIA");
        brandMap.put(1, "AMD");
        brandMap.put(2, "INTEL");

        for (int brand = 0; brand < brandPopularity.length; brand++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(brandMap.get(brand));

            for (int i = startIndex; i <= endIndex; i++) {
                categories.add(headers.get(i));
                XYChart.Data<String, Number> data = new XYChart.Data<>(headers.get(i), brandPopularity[brand][i - startIndex]);
                series.getData().add(data);
            }
            seriesList.add(series);
        }
    }

}
