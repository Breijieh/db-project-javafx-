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

public class OrderCard extends VBox {

    public OrderCard(String carModel, String customerName, String orderDate, String price, String carImagePath) {
        setPadding(new Insets(15));
        setSpacing(10);
        setAlignment(Pos.TOP_LEFT);
        // setBackground(new Background(new BackgroundFill(Theme.BACKGROUND_COLOR, new
        // CornerRadii(15), Insets.EMPTY)));
        getStyleClass().addAll("primary-bg","rounded");

        Image carImage = new Image(getClass().getResourceAsStream("/com/example/images/" + carImagePath));
        ImageView carImageView = new ImageView(carImage);
        carImageView.fitHeightProperty().bind(heightProperty().multiply(0.66));
        carImageView.setPreserveRatio(true);

        HBox carImageBox = new HBox(carImageView);
        carImageBox.setAlignment(Pos.CENTER);

        Label modelLabel = new Label(carModel);
        modelLabel.setFont(Theme.getPoppinsFont(600, 18));
        // modelLabel.setTextFill(Theme.NAVY_COLOR);
        modelLabel.getStyleClass().add("view-title");

        Label infoLabel = new Label("Customer: " + customerName + "      Order Date: " + orderDate);
        infoLabel.setFont(Theme.getPoppinsFont(400, 13));
        // infoLabel.setTextFill(Theme.GRAY_COLOR);
        infoLabel.getStyleClass().add("view-subtitle");

        Label priceLabel = new Label(price);
        priceLabel.setFont(Theme.getPoppinsFont(600, 20));
        priceLabel.setTextFill(Theme.BLUE_COLOR);
        setPadding(new Insets(0, 0, 30, 30));
        getChildren().addAll(carImageView, modelLabel, infoLabel, priceLabel);
    }
}
