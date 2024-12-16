package com.example.controller;

import com.example.model.home.HomeStats;
import com.example.model.home.LatestOrder;
import com.example.model.home.LatestService;
import com.example.service.HomeService;

import java.sql.SQLException;
import java.util.List;

public class HomeController {
    private HomeService homeService;

    public HomeController() {
        this.homeService = new HomeService();
    }

    public HomeStats loadHomeStats() {
        try {
            return homeService.getHomeStats();
        } catch (SQLException e) {
            e.printStackTrace();
            return new HomeStats(0, 0, 0, 0);
        }
    }

    public List<LatestOrder> loadLatestOrders() {
        try {
            return homeService.getLatestOrders();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<LatestService> loadLatestServices() {
        try {
            return homeService.getLatestServices();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
