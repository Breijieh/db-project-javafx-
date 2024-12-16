package com.example.AppStructure.main.home;

import com.example.components.Theme;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class RecordCard extends VBox {
    private final Label valueLabel;
    private final IntegerProperty currentValue = new SimpleIntegerProperty(0);
    private final int targetValue;

    public RecordCard(String title, int value) {
        this.targetValue = value;

        Label titleLabel = new Label(title);
        titleLabel.setFont(Theme.getPoppinsFont(400, 14));
        titleLabel.getStyleClass().add("view-subtitle");

        valueLabel = new Label("0");
        valueLabel.setFont(Theme.getPoppinsFont(600, 20));
        valueLabel.setTextFill(Theme.BLUE_COLOR);

        currentValue.addListener((observable, oldValue, newValue) -> {
            valueLabel.setText(String.valueOf(newValue.intValue()));
        });

        getStyleClass().addAll("secondary-bg", "rounded");
        setSpacing(10);
        setPadding(new Insets(20));
        getChildren().addAll(titleLabel, valueLabel);
        setEffect(Theme.createShadow());

        initializeCountingAnimation();
    }

    private void initializeCountingAnimation() {
        Duration duration = Duration.seconds(3);
        Duration delay = Duration.seconds(1);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(currentValue, 0)),
                new KeyFrame(duration, new KeyValue(currentValue, targetValue)));
        timeline.setDelay(delay);
        timeline.setCycleCount(1);
        timeline.play();
    }
}
