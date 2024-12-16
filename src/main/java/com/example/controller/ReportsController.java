package com.example.controller;

import com.example.components.dialog.MessageDialog;
import com.example.AppStructure.AppStage;
import com.example.service.ReportService;
import com.example.components.general.CustomComboBox;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;
import java.util.*;

/**
 * Controller for handling report generation and interactions.
 */
public class ReportsController {

    private final ReportService reportService;

    // Containers to hold charts for easy removal and replacement
    private final VBox serviceFrequencyChartContainer;
    private final VBox revenueChartContainer;

    /**
     * Constructs a ReportsController and initializes the ReportService.
     */
    public ReportsController() {
        this.reportService = new ReportService();
        this.serviceFrequencyChartContainer = new VBox();
        this.revenueChartContainer = new VBox();
    }

    public Node generateServiceFrequencyReport() {
        VBox reportBox = new VBox(10);
        reportBox.setPadding(new Insets(10));
        reportBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Frequency of Services");
        titleLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 16));

        HBox selectionBox = new HBox(10);
        selectionBox.setAlignment(Pos.CENTER_LEFT);

        CustomComboBox<String> modelComboBox = new CustomComboBox<>("Select Car Model:");

        try {
            List<String> models = reportService.getAllCarModels();
            List<String> allModels = new ArrayList<>();
            allModels.add("All Models"); // Adding the static item
            allModels.addAll(models);
            modelComboBox.setItems(allModels.toArray(new String[0]));
        } catch (SQLException e) {
            e.printStackTrace();
            AppStage.getInstance().showMessage("Error",
                    "Failed to load car models:\n" + e.getMessage(),
                    MessageDialog.MessageType.ERROR);
        }

        selectionBox.getChildren().addAll(modelComboBox);

        serviceFrequencyChartContainer.setAlignment(Pos.CENTER);

        BarChart<String, Number> initialBarChart = createServiceFrequencyChart(null);
        serviceFrequencyChartContainer.getChildren().add(initialBarChart);

        modelComboBox.setOnAction(event -> {
            String selectedModel = modelComboBox.getValue();
            String modelFilter = "All Models".equals(selectedModel) ? null : selectedModel;
            updateServiceFrequencyChart(modelFilter);
        });

        reportBox.getChildren().addAll(titleLabel, selectionBox, serviceFrequencyChartContainer);
        return reportBox;
    }

    private BarChart<String, Number> createServiceFrequencyChart(String modelFilter) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Service Type");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        String chartTitle = (modelFilter == null) ? "Service Frequency for All Models"
                : "Service Frequency for Model: " + modelFilter;
        barChart.setTitle(chartTitle);
        barChart.setLegendVisible(false);

        try {
            Map<String, Integer> serviceFrequency = reportService.getServiceFrequency(modelFilter);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Services");

            int maxFrequency = serviceFrequency.values().stream().max(Integer::compareTo).orElse(1);
            maxFrequency = Math.max(maxFrequency, 5); // Ensure a minimum upper bound for better visuals

            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(maxFrequency + 1);
            yAxis.setTickUnit(1);
            yAxis.setMinorTickCount(0);

            yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
                @Override
                public String toString(Number object) {
                    return String.valueOf(object.intValue());
                }
            });

            yAxis.setTickLabelFont(Font.font("Poppins", FontWeight.NORMAL, 12));

            for (Map.Entry<String, Integer> entry : serviceFrequency.entrySet()) {
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(entry.getKey(), entry.getValue());
                series.getData().add(dataPoint);

                Tooltip tooltip = new Tooltip("Service: " + entry.getKey() + "\nFrequency: " + entry.getValue());
                dataPoint.nodeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Tooltip.install(newValue, tooltip);
                    }
                });
            }

            barChart.getData().add(series);
            barChart.lookupAll(".default-color0.chart-bar")
                    .forEach(node -> node.setStyle("-fx-bar-fill:#2954ED"));

        } catch (SQLException e) {
            e.printStackTrace();
            AppStage.getInstance().showMessage("Error",
                    "Failed to load service frequency data:\n" + e.getMessage(),
                    MessageDialog.MessageType.ERROR);
        }

        return barChart;
    }

    private void updateServiceFrequencyChart(String modelFilter) {
        // Remove the existing chart
        serviceFrequencyChartContainer.getChildren().clear();

        // Create a new chart with updated data
        BarChart<String, Number> newBarChart = createServiceFrequencyChart(modelFilter);

        // Add the new chart to the container
        serviceFrequencyChartContainer.getChildren().add(newBarChart);
    }

    public Node generateRevenueReport() {
        VBox reportBox = new VBox(10);
        reportBox.setPadding(new Insets(10));
        reportBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Revenue from Services");
        titleLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 16));

        HBox selectionBox = new HBox(10);
        selectionBox.setAlignment(Pos.CENTER_LEFT);

        CustomComboBox<Integer> yearComboBox = new CustomComboBox<>("Select Year:");
        CustomComboBox<String> aggregationComboBox = new CustomComboBox<>("Aggregation:");

        // Populate Year ComboBox with distinct years from services data
        try {
            List<Integer> years = reportService.getDistinctServiceYears();
            List<Integer> allYears = new ArrayList<>(years);
            yearComboBox.setItems(allYears.toArray(new Integer[0]));
            if (!allYears.isEmpty()) {
                yearComboBox.getComboBox().getSelectionModel().selectFirst(); // Default to the most recent year
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AppStage.getInstance().showMessage("Error",
                    "Failed to load available years:\n" + e.getMessage(),
                    MessageDialog.MessageType.ERROR);
        }

        // Set Aggregation options
        aggregationComboBox.setItems("Monthly", "Quarterly");
        aggregationComboBox.getComboBox().getSelectionModel().selectFirst(); // Default to Monthly

        selectionBox.getChildren().addAll(yearComboBox, aggregationComboBox);

        // Initialize the chart container
        revenueChartContainer.setAlignment(Pos.CENTER);

        // Create initial chart based on default selections
        Integer defaultYear = yearComboBox.getValue();
        String defaultAggregation = aggregationComboBox.getValue();
        if (defaultYear != null && defaultAggregation != null) {
            StackedBarChart<String, Number> initialStackedBarChart = createRevenueChart(defaultYear,
                    defaultAggregation);
            revenueChartContainer.getChildren().add(initialStackedBarChart);
        }

        // Add listeners to ComboBoxes for live updates
        yearComboBox.setOnAction(event -> {
            Integer selectedYear = yearComboBox.getValue();
            String aggregation = aggregationComboBox.getValue();
            if (selectedYear != null && aggregation != null) {
                updateRevenueChart(selectedYear, aggregation);
            }
        });

        aggregationComboBox.setOnAction(event -> {
            Integer selectedYear = yearComboBox.getValue();
            String aggregation = aggregationComboBox.getValue();
            if (selectedYear != null && aggregation != null) {
                updateRevenueChart(selectedYear, aggregation);
            }
        });

        reportBox.getChildren().addAll(titleLabel, selectionBox, revenueChartContainer);
        return reportBox;
    }

    /**
     * Creates a StackedBarChart for the revenue report based on the selected year
     * and aggregation.
     *
     * @param year        The selected year.
     * @param aggregation "Monthly" or "Quarterly".
     * @return A configured StackedBarChart.
     */
    private StackedBarChart<String, Number> createRevenueChart(int year, String aggregation) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(aggregation + " Period");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue ($)");

        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        String chartTitle = aggregation + " Revenue from Each Service Type in " + year;
        stackedBarChart.setTitle(chartTitle);

        try {
            // Map aggregation to DAO expected values
            String aggregationParam;
            if ("Monthly".equalsIgnoreCase(aggregation)) {
                aggregationParam = "MONTH";
            } else if ("Quarterly".equalsIgnoreCase(aggregation)) {
                aggregationParam = "QUARTER";
            } else {
                // Default or invalid aggregation
                AppStage.getInstance().showMessage("Invalid Aggregation",
                        "Aggregation must be either 'Monthly' or 'Quarterly'.",
                        MessageDialog.MessageType.ERROR);
                return stackedBarChart;
            }

            Map<String, Map<String, Double>> revenueData = reportService.getRevenueByServiceType(year,
                    aggregationParam);

            // Determine all periods (months or quarters)
            Set<String> periods = new TreeSet<>();
            for (Map<String, Double> periodMap : revenueData.values()) {
                periods.addAll(periodMap.keySet());
            }

            // If no data, show a message
            if (periods.isEmpty()) {
                AppStage.getInstance().showMessage("No Data",
                        "No revenue data available for the selected year and aggregation.",
                        MessageDialog.MessageType.INFORMATION);
                return stackedBarChart;
            }

            xAxis.setCategories(FXCollections.observableArrayList(periods));

            // Create a series for each service type
            for (Map.Entry<String, Map<String, Double>> entry : revenueData.entrySet()) {
                String serviceType = entry.getKey();
                Map<String, Double> periodRevenue = entry.getValue();

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(serviceType);

                for (String period : periods) {
                    Double revenue = periodRevenue.getOrDefault(period, 0.0);
                    XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(period, revenue);
                    series.getData().add(dataPoint);

                    // Add Tooltip
                    Tooltip tooltip = new Tooltip("Service: " + serviceType + "\nPeriod: " + period + "\nRevenue: $"
                            + String.format("%.2f", revenue));
                    // Delay tooltip installation until the node is rendered
                    dataPoint.nodeProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            Tooltip.install(newValue, tooltip);
                        }
                    });
                }

                stackedBarChart.getData().add(series);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            AppStage.getInstance().showMessage("Error",
                    "Failed to load revenue data:\n" + e.getMessage(),
                    MessageDialog.MessageType.ERROR);
        }

        return stackedBarChart;
    }

    /**
     * Updates the revenue StackedBarChart by removing the existing chart and adding
     * a new one.
     *
     * @param year        The selected year.
     * @param aggregation "Monthly" or "Quarterly".
     */
    private void updateRevenueChart(int year, String aggregation) {
        // Remove the existing chart
        revenueChartContainer.getChildren().clear();

        // Create a new chart with updated data
        StackedBarChart<String, Number> newStackedBarChart = createRevenueChart(year, aggregation);

        // Add the new chart to the container
        revenueChartContainer.getChildren().add(newStackedBarChart);
    }

    /**
     * Cleans up resources when the controller is no longer needed.
     */
    public void cleanup() {
        reportService.close();
    }
}
