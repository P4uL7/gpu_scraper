package charts;

import csv.CsvReader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.*;

import static utils.ChartUtils.getBrandPopularity;
import static utils.ChartUtils.hideDataPoints;
import static utils.Constants.*;


public class AreaChartApp extends Application {

    private static final List<List<String>> csvData = CsvReader.getCsvData(CSV_FILE);
    private static final List<String> headers = csvData.get(0);
    private static final int startIndex = DATA_2008.getSTART_INDEX();
    private static final int endIndex = DATA_2021.getEND_INDEX();

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {

        initUI(stage);
    }

    private void initUI(final Stage stage) {

        final Double[][] brandPopularity = getBrandPopularity(csvData, startIndex, endIndex);

        for (int j = 0; j < brandPopularity[0].length; j++) {
            brandPopularity[1][j] += brandPopularity[2][j];
            brandPopularity[0][j] += brandPopularity[1][j];
        }

        for (int j = 0; j < brandPopularity[0].length; j++) {
            final double scale = 100d / brandPopularity[0][j];
            for (int i = 0; i < brandPopularity.length; i++) {
                brandPopularity[i][j] = brandPopularity[i][j] * scale;
            }
        }

        final Set<String> categories = new LinkedHashSet<>();
        final List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

        final Map<Integer, String> brandMap = new HashMap<>();
        brandMap.put(0, "NVIDIA");
        brandMap.put(1, "AMD");
        brandMap.put(2, "INTEL");

//        final XYChart.Series<String, Number> seriesOther = new XYChart.Series<>();
//        seriesOther.setName("No data");
//        for (int i = startIndex; i <= endIndex; i++) {
//            categories.add(headers.get(i));
//            final XYChart.Data<String, Number> data = new XYChart.Data<>(headers.get(i), 100);
//            seriesOther.getData().add(data);
//        }
//        seriesList.add(seriesOther);


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


        final ObservableList<String> c = FXCollections.observableArrayList(categories);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setCategories(c);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);

        final AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Monthly Popularity by Brand");
        areaChart.verticalGridLinesVisibleProperty().setValue(Boolean.FALSE);
        areaChart.horizontalGridLinesVisibleProperty().setValue(Boolean.FALSE);
        areaChart.getYAxis().setTickLabelsVisible(false);
        areaChart.getYAxis().setOpacity(0);
        areaChart.getData().addAll(seriesList);

        final Scene scene = new Scene(areaChart, 1000, 900);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/areaChart_3colors.css")).toExternalForm()); //fix brandPopularity colors

        stage.setScene(scene);
        stage.show();

        // add tooltips
//        addTooltips(areaChart, true);

        // hide data points
        hideDataPoints(areaChart, true);
    }
}
