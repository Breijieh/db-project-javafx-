package com.example.AppStructure.main.home;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.beans.binding.Bindings;
import java.util.List;

import com.example.components.Theme;
import com.example.components.general.SectionTitle;

public class LatestServicesContainer extends VBox {

    public LatestServicesContainer(String title, List<ServiceCard> items) {
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_LEFT);
        // setBackground(new Background(new BackgroundFill(Theme.WHITE_COLOR, new
        // CornerRadii(15), Insets.EMPTY)));
        getStyleClass().add("service-card");
        setEffect(Theme.createShadow());

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(15);
        flowPane.setVgap(15);
        flowPane.setPadding(new Insets(10));
        flowPane.setAlignment(Pos.TOP_LEFT);

        items.forEach(item -> {
            item.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                double flowPaneWidth = flowPane.getWidth();
                double cardWidth = (flowPaneWidth) / 2;
                return Math.max(150, cardWidth - 20);
            }, flowPane.widthProperty()));

            flowPane.getChildren().add(item);
        });

        getChildren().addAll(new SectionTitle(title), flowPane);
    }
}
