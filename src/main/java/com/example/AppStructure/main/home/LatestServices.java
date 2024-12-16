package com.example.AppStructure.main.home;

import com.example.model.home.LatestService;
import java.util.List;
import java.util.stream.Collectors;

public class LatestServices extends LatestServicesContainer {
    public LatestServices(List<LatestService> services) {
        super("Latest Services", services.stream()
                .map(s -> new ServiceCard(
                        s.getCarName(),
                        s.getCustomerName(),
                        "$" + String.format("%.2f", s.getCost()),
                        s.getServiceDescription(),
                        s.getImageName()))
                .collect(Collectors.toList()));
    }
}
