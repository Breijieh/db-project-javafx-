package com.example.views;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import com.example.AppStructure.main.home.LatestOrders;
import com.example.AppStructure.main.home.LatestServices;
import com.example.AppStructure.main.home.Records;
import com.example.components.general.ViewTitle;
import com.example.controller.HomeController;
import com.example.model.home.HomeStats;
import com.example.model.home.LatestOrder;
import com.example.model.home.LatestService;

import java.util.List;

public class HomeView extends ScrollPane {
    private HomeController homeController;

    public HomeView() {
        this.homeController = new HomeController();

        VBox contentBox = new VBox();
        contentBox.setPadding(new Insets(20));
        contentBox.setSpacing(20);
        ViewTitle header = new ViewTitle("Home", "This is the home page to help me manage tasks");
        setContent(contentBox);
        setFitToWidth(true);
        setPannable(true);
        contentBox.getStyleClass().add("primary-bg");
        getStyleClass().add("primary-bg");
        setPadding(new Insets(20));

        // Load dynamic data
        HomeStats stats = homeController.loadHomeStats();
        List<LatestOrder> latestOrders = homeController.loadLatestOrders();
        List<LatestService> latestServices = homeController.loadLatestServices();

        Records recordsView = new Records(stats);
        LatestOrders latestOrdersView = new LatestOrders(latestOrders);
        LatestServices latestServicesView = new LatestServices(latestServices);

        contentBox.getChildren().addAll(header, recordsView, latestOrdersView, latestServicesView);
    }
}
