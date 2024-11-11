package com.example;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FitnessPlotter {

    public static void plotFitnessOverTime(List<Integer> fitnessOverTime) throws IOException {
        XYSeries series = new XYSeries("Best Fitness");
        for (int i = 0; i < fitnessOverTime.size(); i++) {
            series.add(i, fitnessOverTime.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Best Fitness Over Generations",
                "Generation",
                "Best Fitness (Total Distance)",
                dataset
        );

        File imageFile = new File("fitness_over_time.png");
        ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);

        System.out.println("Fitness over time plot saved as fitness_over_time.png");
    }
}
