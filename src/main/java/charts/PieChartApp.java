package charts;

import csv.CsvReader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static utils.ChartUtils.getBrandPopularity;
import static utils.Constants.*;

public class PieChartApp extends Application {

    private final static int startIndex = DATA_2008.getSTART_INDEX();
    private final static int endIndex = DATA_2021.getEND_INDEX();
    private final static List<List<String>> csvData = CsvReader.getCsvData(CSV_FILE);
    private final static List<String> headers = csvData.get(0);
    private final static Double[][] brandPopularity = getBrandPopularity(csvData, startIndex, endIndex);

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        stage.setTitle("GPU market share " + headers.get(3));

        final ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("NVIDIA - " + brandPopularity[0][0] + "%", brandPopularity[0][0]),
                        new PieChart.Data("AMD - " + brandPopularity[1][0] + "%", brandPopularity[1][0]),
                        new PieChart.Data("Intel - " + brandPopularity[2][0] + "%", brandPopularity[2][0]));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("GPU market share");
        chart.setLegendSide(Side.RIGHT);
        chart.setPrefSize(850, 650);
        chart.setMinSize(850, 650);
        chart.setMaxSize(850, 650);
        final Scene scene = new Scene(chart);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/pieChart.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

        final Timeline timeline = new Timeline();
        final AtomicInteger index = new AtomicInteger(1);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), (ActionEvent actionEvent) -> {
                    try {
                        chart.setTitle(headers.get(index.get() + 3));

                        pieChartData.get(0).setName("NVIDIA - " + formatAsString(brandPopularity[0][index.get()]) + "%");
                        pieChartData.get(0).setPieValue(brandPopularity[0][index.get()]);
                        pieChartData.get(1).setName("AMD - " + formatAsString(brandPopularity[1][index.get()]) + "%");
                        pieChartData.get(1).setPieValue(brandPopularity[1][index.get()]);
                        pieChartData.get(2).setName("INTEL - " + formatAsString(brandPopularity[2][index.get()]) + "%");
                        pieChartData.get(2).setPieValue(brandPopularity[2][index.get()]);
                    } catch (final Exception e) {
                        System.err.println("Done");
                    }
                    if (index.incrementAndGet() == endIndex) {
                        System.err.println("Done");
                        timeline.stop();
                    }

                }));
        timeline.setCycleCount(1000);
        timeline.setAutoReverse(true);  //!?

        timeline.play();

    }

    private String formatAsString(final double number) {
        return String.format("%05.2f", number);
    }
}
