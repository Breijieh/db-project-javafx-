package com.example.AppStructure.main;

import com.example.AppStructure.header.Header;
import com.example.views.HomeView;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Main extends VBox {

    private static Main instance;
    private final StackPane contentPane;

    public Main() {
        instance = this;

        Header header = new Header();
        contentPane = new StackPane();
        contentPane.getChildren().add(new HomeView());
        // setBackground(new Background(new BackgroundFill(Theme.BLUE_COLOR,
        // CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().addAll(header, contentPane);
    }

    public static Main getInstance() {
        return instance;
    }

    public void changeView(Node view) {
        StackPane loadingScreen = new StackPane();
        loadingScreen.getStyleClass().add("loading-screen");

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.getStyleClass().add("loading-indicator");
        loadingScreen.getChildren().add(loadingIndicator);

        contentPane.getChildren().add(loadingScreen);

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                contentPane.getChildren().clear();
                contentPane.getChildren().add(view);
            });
        }).start();
    }
}
