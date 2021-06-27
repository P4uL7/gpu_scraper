package charts;

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
import model.Brand;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PieChartApp extends Application {

    private final Map<Brand, Double> marketShare = new HashMap<>();

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        stage.setTitle("GPU market share");

        marketShare.put(Brand.NVIDIA, 7.5);
        marketShare.put(Brand.AMD, 18d);
        marketShare.put(Brand.INTEL, 5.1);
        final ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data(getNameWithPerc(Brand.NVIDIA), marketShare.get(Brand.NVIDIA)),
                        new PieChart.Data(getNameWithPerc(Brand.AMD), marketShare.get(Brand.AMD)),
                        new PieChart.Data(getNameWithPerc(Brand.INTEL), marketShare.get(Brand.INTEL)));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("GPU market share");
        chart.setLegendSide(Side.LEFT);
        chart.setPrefSize(650, 450);
        final Scene scene = new Scene(chart);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/pieChart.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();

        final Timeline timeline = new Timeline();
        final Random random = new Random();
        final double rangeMin = 1d;
        final double rangeMax = 50d;
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000), (ActionEvent actionEvent) -> {
                    final double newValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
                    chart.setTitle("GPU " + newValue);
                    marketShare.put(Brand.NVIDIA, newValue);

                    pieChartData.get(0).setName(getNameWithPerc(Brand.NVIDIA));
                    pieChartData.get(0).setPieValue(marketShare.get(Brand.NVIDIA));
                    pieChartData.get(1).setName(getNameWithPerc(Brand.AMD));
                    pieChartData.get(1).setPieValue(marketShare.get(Brand.AMD));
                    pieChartData.get(2).setName(getNameWithPerc(Brand.INTEL));
                    pieChartData.get(2).setPieValue(marketShare.get(Brand.INTEL));
                }));
        timeline.setCycleCount(1000);
        timeline.setAutoReverse(true);  //!?
        timeline.play();

    }

    private String getNameWithPerc(final Brand brand) {
        final double total = marketShare.values().stream().mapToDouble(Double::doubleValue).sum();
        final double realPercentage = (100 * marketShare.get(brand)) / total;
        return brand.toString() + " - " + round(realPercentage, 2) + "%";
    }

    private double round(final double value, final int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
