package com.example.AppStructure.header;

import java.util.prefs.Preferences;

import com.example.App;
import com.example.AppStructure.AppStage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ThemeToggle extends HBox {
    private Label lightLabel;
    private Label darkLabel;
    private Preferences prefs = Preferences.userNodeForPackage(AppStage.class);

    public ThemeToggle() {
        lightLabel = createLabel("Light");
        darkLabel = createLabel("Dark");
        if (prefs.getBoolean("theme", false)) {
            setSelected(darkLabel);
        } else {
            setSelected(lightLabel);
        }

        lightLabel.setOnMouseClicked(event -> {
            if (!AppStage.getInstance().isDarkTheme()) {
                return;
            }
            AppStage.getInstance().toggleTheme();
            setSelected(lightLabel);
        });

        darkLabel.setOnMouseClicked(event -> {
            if (AppStage.getInstance().isDarkTheme()) {
                return; // Already in dark theme
            }
            AppStage.getInstance().toggleTheme();
            setSelected(darkLabel);
        });

        getChildren().addAll(lightLabel, darkLabel);
        getStyleClass().add("theme-toggle");
        setSpacing(5);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5, 10, 5, 10));
        setMaxWidth(160);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("theme-toggle-label");
        label.setCursor(Cursor.HAND);
        return label;
    }

    private void setSelected(Label selectedLabel) {
        lightLabel.getStyleClass().removeAll("selected");
        darkLabel.getStyleClass().removeAll("selected");

        selectedLabel.getStyleClass().add("selected");
    }
}
