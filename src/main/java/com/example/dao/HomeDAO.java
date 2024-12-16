package com.example.dao;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.model.home.HomeStats;
import com.example.model.home.LatestOrder;
import com.example.model.home.LatestService;

public class HomeDAO {

    public HomeStats getHomeStats() throws SQLException {
        int totalCars = getCount("cars");
        int totalCustomers = getCount("customers");
        int totalOrders = getCount("orders");
        int totalEmployees = getCount("employees");

        return new HomeStats(totalCars, totalCustomers, totalOrders, totalEmployees);
    }

    private int getCount(String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM " + tableName;
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    public List<LatestOrder> getLatestOrders() throws SQLException {
        String sql = "SELECT car.Make, car.Model, c.FirstName, c.LastName, o.OrderDate, o.TotalPrice " +
                "FROM orders o " +
                "JOIN cars car ON o.CarID = car.CarID " +
                "JOIN customers c ON o.CustomerID = c.CustomerID " +
                "ORDER BY o.OrderDate DESC " +
                "LIMIT 4";

        List<LatestOrder> orders = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String carName = rs.getString("Make") + " " + rs.getString("Model");
                String customerName = rs.getString("FirstName") + " " + rs.getString("LastName");
                LocalDate orderDate = rs.getDate("OrderDate").toLocalDate();
                double totalPrice = rs.getDouble("TotalPrice");

                orders.add(new LatestOrder(carName, customerName, orderDate, totalPrice));
            }
        }
        return orders;
    }

    public List<LatestService> getLatestServices() throws SQLException {
        String sql = "SELECT car.Make, car.Model, c.FirstName, c.LastName, s.ServiceDate, s.Cost, s.ServiceDescription "
                +
                "FROM services s " +
                "JOIN cars car ON s.CarID = car.CarID " +
                "JOIN customers c ON s.CustomerID = c.CustomerID " +
                "ORDER BY s.ServiceDate DESC " +
                "LIMIT 6";

        List<LatestService> services = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String carName = rs.getString("Make") + " " + rs.getString("Model");
                String customerName = rs.getString("FirstName") + " " + rs.getString("LastName");
                LocalDate serviceDate = rs.getDate("ServiceDate").toLocalDate();
                double cost = rs.getDouble("Cost");
                String serviceDescription = rs.getString("ServiceDescription");

                services.add(new LatestService(carName, customerName, cost, serviceDescription, serviceDate));
            }
        }
        return services;
    }
}
