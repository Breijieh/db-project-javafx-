package com.example.AppStructure.main.home;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import com.example.model.home.HomeStats;

public class Records extends HBox {
    public Records(HomeStats stats) {
        setSpacing(20);
        RecordCard cars = new RecordCard("Total Cars", (stats.getTotalCars()));
        RecordCard customers = new RecordCard("Total Customers", stats.getTotalCustomers());
        RecordCard orders = new RecordCard("Total Orders", (stats.getTotalOrders()));
        RecordCard employees = new RecordCard("Total Employees", (stats.getTotalEmployees()));

        HBox.setHgrow(cars, Priority.ALWAYS);
        HBox.setHgrow(customers, Priority.ALWAYS);
        HBox.setHgrow(orders, Priority.ALWAYS);
        HBox.setHgrow(employees, Priority.ALWAYS);

        getChildren().addAll(cars, customers, orders, employees);
    }
}
