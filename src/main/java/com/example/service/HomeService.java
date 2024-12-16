package com.example.service;

import com.example.dao.HomeDAO;
import com.example.model.home.HomeStats;
import com.example.model.home.LatestOrder;
import com.example.model.home.LatestService;

import java.sql.SQLException;
import java.util.List;

public class HomeService {
    private HomeDAO homeDAO;

    public HomeService() {
        this.homeDAO = new HomeDAO();
    }

    public HomeStats getHomeStats() throws SQLException {
        return homeDAO.getHomeStats();
    }

    public List<LatestOrder> getLatestOrders() throws SQLException {
        return homeDAO.getLatestOrders();
    }

    public List<LatestService> getLatestServices() throws SQLException {
        return homeDAO.getLatestServices();
    }
}
