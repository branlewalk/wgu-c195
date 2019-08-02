package org.branlewalk.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.chart.XYChart.*;

public class ReportController implements Initializable {



    @FXML
    NumberAxis numberAxis;
    @FXML
    CategoryAxis categoryAxis;
    @FXML
    BarChart reportBarChart;
    private ObservableList<Series> series;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        series = FXCollections.observableArrayList();
        reportBarChart.setData(series);
    }

    public void handleExitButton(ActionEvent actionEvent) {
        Main.closeWindow(actionEvent);
    }

    public void setNumberAxis(NumberAxis numberAxis) {
        this.numberAxis = numberAxis;
    }

    public void setCategoryAxis(CategoryAxis categoryAxis) {
        this.categoryAxis = categoryAxis;
    }

    public void setChartData(Series chartData) {
        series.clear();
        series.add(chartData);
    }



}
