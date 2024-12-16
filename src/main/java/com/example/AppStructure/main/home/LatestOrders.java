package com.example.AppStructure.main.home;

import com.example.model.home.LatestOrder;
import java.util.List;
import java.util.stream.Collectors;

public class LatestOrders extends LatestOrdersContainer {
    public LatestOrders(List<LatestOrder> orders) {
        super("Latest Orders", orders.stream()
                .map(o -> new OrderCard(
                        o.getCarName(),
                        o.getCustomerName(),
                        o.getOrderDate().toString(),
                        "$" + String.format("%.2f", o.getTotalPrice()),
                        o.getImageName() // Use the pre-assigned image from LatestOrder
                ))
                .collect(Collectors.toList()));
    }
}
