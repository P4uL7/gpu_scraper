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
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static utils.Constants.*;


public class LineChartSample extends Application {
    private static final List<List<String>> csvData = CsvReader.getCsvData(CSV_FILE);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");

        final Set<String> categories = new LinkedHashSet<>();

        List<String> headers = csvData.get(0);

        List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        int startIndex = DATA_2008.getSTART_INDEX();
        int endIndex = DATA_2021.getEND_INDEX();
        LinkedHashMap<String, Number> topXGpus = getTopXGpus(8, PopularitySort.SUM, false, startIndex, endIndex);

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
//                    if (value > 2) {
                    XYChart.Data<String, Number> data = new XYChart.Data<>(headers.get(i), value);
                    series.getData().add(data);
                    addGpu = true;
//                }
                }
            }
            if (addGpu) {
                seriesList.add(series);
            }
        }

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
        stage.setScene(scene);
        stage.show();

        // add tooltips
        addTooltips(lineChart);
    }

    private void addTooltips(LineChart<String, Number> lineChart) {
        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                d.getNode().setVisible(true);
                Tooltip.install(d.getNode(), new Tooltip(
                        s.getName() + "\n" +
                                "Date: " + d.getXValue() + "\n" +
                                "Popularity: " + d.getYValue() + "%"));
            }
        }
    }

    private LinkedHashMap<String, Number> getTopXGpus(final int X, final PopularitySort sort, final boolean includeSeries, final int startDate, final int endDate) {
        HashMap<String, Number> popularityList = new HashMap<>();
        for (int g = 1; g < csvData.size(); g++) {
            List<String> gpu = csvData.get(g);
            if (!includeSeries && gpu.get(1).toLowerCase().contains("series")) {
                continue;
            }

            double totalScore = 0d;
            for (int i = startDate; i <= endDate; i++) {
                String value = gpu.get(i);
                if (!value.equals("-")) {
                    double doubleValue = Double.parseDouble(value);
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
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
