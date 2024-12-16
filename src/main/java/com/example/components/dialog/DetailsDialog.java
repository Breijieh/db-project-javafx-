package com.example.components.dialog;

import com.example.AppStructure.AppStage;
import com.example.components.Theme;
import com.example.components.general.CustomButton;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
// import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DetailsDialog extends StackPane {

    private VBox modalContent;
    private Label titleLabel;
    private Pane customContent;
    private CustomButton closeButton;

    public DetailsDialog() {

        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        this.setPickOnBounds(true);
        this.setVisible(false);
        this.setAlignment(Pos.CENTER);

        modalContent = new VBox(15);
        modalContent.setAlignment(Pos.CENTER);
        modalContent.setPadding(new Insets(20));
        modalContent.setMaxWidth(600);
        modalContent.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        modalContent.setEffect(Theme.createShadow());

        modalContent.setMaxHeight(Region.USE_PREF_SIZE);
        modalContent.setPrefHeight(Region.USE_COMPUTED_SIZE);

        titleLabel = new Label("Details");
        titleLabel.setFont(Theme.getPoppinsFont(500, 20));
        titleLabel.setTextFill(Theme.BLUE_COLOR);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setWrapText(true);

        customContent = new Pane();
        customContent.setMinHeight(100);

        closeButton = new CustomButton("Close");
        closeButton.setMinWidth(80);
        closeButton.setOnAction(event -> hideWithAnimation());

        modalContent.getChildren().addAll(titleLabel, customContent, closeButton);
        this.getChildren().add(modalContent);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setCustomContent(Pane content) {
        customContent = content;

        modalContent.getChildren().set(1, customContent);
    }

    public void showWithAnimation() {
        modalContent.setOpacity(1.0);
        modalContent.setTranslateY(0);
        modalContent.setScaleX(1.0);
        modalContent.setScaleY(1.0);

        this.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), modalContent);
        slideIn.setFromY(-100);
        slideIn.setToY(0);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), modalContent);
        scaleIn.setFromX(0.9);
        scaleIn.setFromY(0.9);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        // AppStage.getRoot().getChildren().stream()
                // .filter(node -> node != this)
                // .forEach(node -> node.setEffect(new BoxBlur(5, 5, 3)));

        ParallelTransition showTransition = new ParallelTransition(fadeIn, slideIn, scaleIn);
        showTransition.play();
    }

    public void hideWithAnimation() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), modalContent);
        slideOut.setFromY(0);
        slideOut.setToY(-20);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(300), modalContent);
        scaleOut.setFromX(1.0);
        scaleOut.setFromY(1.0);
        scaleOut.setToX(0.9);
        scaleOut.setToY(0.9);

        ParallelTransition hideTransition = new ParallelTransition(fadeOut, slideOut, scaleOut);

        hideTransition.setOnFinished(event -> {
            this.setVisible(false);

            AppStage.getRoot().getChildren().forEach(node -> node.setEffect(null));
            AppStage.getRoot().setEffect(null);
        });

        hideTransition.play();
    }

    public void show() {
        showWithAnimation();
    }
}
