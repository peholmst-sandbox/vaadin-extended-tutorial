package com.example.ui;

import com.example.service.Store;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dashboard.Dashboard;
import com.vaadin.flow.component.dashboard.DashboardWidget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class StoreDashboard extends Composite<Dashboard> {

    private final Chart revenueByStoreChart;
    private final Chart performanceRatingChart;
    private final Chart revenueVsEmployeesChart;
    private final Chart revenueVsEfficiencyChart;

    public StoreDashboard() {
        // Create the components
        revenueByStoreChart = createRevenueByStoreChart();
        performanceRatingChart = createPerformanceRatingChart();
        revenueVsEmployeesChart = createRevenueVsEmployeesChart();
        revenueVsEfficiencyChart = createRevenueVsEfficiencyChart();

        // Configure dashboard
        var dashboard = getContent();
        var revenueVsEfficiencyWidget = new DashboardWidget("Revenue vs Efficiency", revenueVsEfficiencyChart);
        revenueVsEfficiencyWidget.setColspan(2);

        dashboard.add(
                new DashboardWidget("Revenue by Store", revenueByStoreChart),
                new DashboardWidget("Performance Rating Distribution", performanceRatingChart),
                new DashboardWidget("Revenue vs Employees", revenueVsEmployeesChart),
                revenueVsEfficiencyWidget
        );
    }

    private Chart createRevenueByStoreChart() {
        var chart = new Chart(ChartType.BAR);
        var configuration = chart.getConfiguration();

        var tooltip = new Tooltip();
        configuration.setTooltip(tooltip);
        tooltip.setValuePrefix("$");

        configuration.getxAxis().setType(AxisType.CATEGORY);

        return chart;
    }

    private void setRevenueByStoreChartData(List<Store> stores) {
        var items = stores.stream()
                .sorted(Comparator.comparing(Store::revenue).reversed())
                .map(store -> new DataSeriesItem(
                        store.storeName(), store.revenue()
                )).toList();
        var series = new DataSeries(items);
        series.setName("Revenue");
        var configuration = revenueByStoreChart.getConfiguration();
        configuration.setSeries(series);
    }

    private Chart createPerformanceRatingChart() {
        var chart = new Chart(ChartType.PIE);
        var configuration = chart.getConfiguration();

        var tooltip = new Tooltip();
        tooltip.setEnabled(true);
        configuration.setTooltip(tooltip);

        return chart;
    }

    private void setPerformanceRatingData(List<Store> stores) {
        var storesByRating = stores.stream().collect(Collectors.groupingBy(
                Store::performanceRating,
                Collectors.counting()
        ));
        var items = storesByRating.entrySet().stream()
                .map(entry -> new DataSeriesItem(
                        entry.getKey().getDisplayName(),
                        entry.getValue()
                ))
                .toList();
        var series = new DataSeries(items);
        series.setName("Stores");
        performanceRatingChart.getConfiguration().setSeries(series);
    }

    private Chart createRevenueVsEmployeesChart() {
        var chart = new Chart(ChartType.SCATTER);
        var configuration = chart.getConfiguration();
        configuration.getxAxis().setTitle("Revenue");
        configuration.getyAxis().setTitle("Employee Count");
        configuration.getLegend().setEnabled(false);
        return chart;
    }

    private void setRevenueVsEmployeesData(List<Store> stores) {
        var items = stores.stream()
                .map(store -> new DataSeriesItem(
                        store.revenue(), store.employeeCount()
                )).toList();
        var series = new DataSeries(items);
        revenueVsEmployeesChart.getConfiguration().setSeries(series);
    }

    private Chart createRevenueVsEfficiencyChart() {
        var chart = new Chart();
        var configuration = chart.getConfiguration();
        configuration.getxAxis().setType(AxisType.CATEGORY);

        var revenueAxis = new YAxis();
        revenueAxis.setTitle("Revenue");
        configuration.addyAxis(revenueAxis);

        var efficiencyAxis = new YAxis();
        efficiencyAxis.setTitle("Efficiency");
        efficiencyAxis.setOpposite(true);
        configuration.addyAxis(efficiencyAxis);

        var tooltip = new Tooltip();
        tooltip.setEnabled(true);
        configuration.setTooltip(tooltip);
        configuration.getLegend().setEnabled(false);

        return chart;
    }

    private void setRevenueVsEfficiencyData(List<Store> stores) {
        var revenueSeries = new DataSeries(stores.stream()
                .map(store -> new DataSeriesItem(store.storeName(), store.revenue()))
                .toList());
        {
            var options = new PlotOptionsColumn();

            var tooltip = new SeriesTooltip();
            tooltip.setValuePrefix("$");
            options.setTooltip(tooltip);

            revenueSeries.setName("Revenue");
            revenueSeries.setPlotOptions(options);
            revenueSeries.setxAxis(0);
        }

        var efficiencySeries = new DataSeries(stores.stream()
                .map(store -> new DataSeriesItem(store.storeName(), calculateEfficiency(store)))
                .toList());
        {
            var options = new PlotOptionsLine();

            var tooltip = new SeriesTooltip();
            tooltip.setValuePrefix("$");
            tooltip.setValueSuffix("/employee");
            options.setTooltip(tooltip);

            efficiencySeries.setName("Efficiency");
            efficiencySeries.setPlotOptions(options);
            efficiencySeries.setyAxis(1);
        }

        revenueVsEfficiencyChart.getConfiguration().setSeries(revenueSeries, efficiencySeries);
    }

    private BigDecimal calculateEfficiency(Store store) {
        return store.revenue().divide(new BigDecimal(store.employeeCount()), 2, RoundingMode.HALF_UP);
    }

    public void setStores(List<Store> stores) {
        setRevenueByStoreChartData(stores);
        setPerformanceRatingData(stores);
        setRevenueVsEmployeesData(stores);
        setRevenueVsEfficiencyData(stores);
    }
}
