package com.example.AppStructure.main.home;

import com.example.components.Theme;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ServiceCard extends HBox {

    public ServiceCard(String carModel, String customerName, String cost, String description, String carImagePath) {
        setPadding(new Insets(15));
        setSpacing(15);
        setAlignment(Pos.CENTER_LEFT);
        // setBackground(new Background(new BackgroundFill(Theme.BACKGROUND_COLOR, new
        // CornerRadii(15), Insets.EMPTY)));
        getStyleClass().addAll("primary-bg","rounded");

        Image carImage = new Image(getClass().getResourceAsStream("/com/example/images/" + carImagePath));
        ImageView carImageView = new ImageView(carImage);
        carImageView.setFitHeight(60);
        carImageView.setPreserveRatio(true);

        VBox detailsBox = new VBox(5);
        detailsBox.setAlignment(Pos.TOP_LEFT);

        Label modelLabel = new Label(carModel);
        modelLabel.setFont(Theme.getPoppinsFont(500, 17));
        modelLabel.getStyleClass().add("view-title");

        Label customerLabel = new Label("Customer: " + customerName);
        customerLabel.setFont(Theme.getPoppinsFont(400, 14));
        customerLabel.getStyleClass().add("view-subtitle");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setFont(Theme.getPoppinsFont(400, 14));
        descriptionLabel.getStyleClass().add("view-subtitle");

        Label costLabel = new Label(cost);
        costLabel.setFont(Theme.getPoppinsFont(500, 18));
        costLabel.setTextFill(Theme.BLUE_COLOR);

        detailsBox.getChildren().addAll(modelLabel, customerLabel, descriptionLabel, costLabel);
        getChildren().addAll(carImageView, detailsBox);
    }
}
