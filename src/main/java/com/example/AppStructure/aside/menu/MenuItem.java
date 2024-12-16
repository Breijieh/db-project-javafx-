package com.example.AppStructure.aside.menu;

import com.example.AppStructure.main.Main;
import com.example.components.Theme;
import com.example.views.CarsView;
import com.example.views.HomeView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class MenuItem extends HBox {

    private final ImageView iconView;
    private final Label label;
    private final String iconName;
    private boolean isSelected;
    private final String text;

    public MenuItem(String iconName, String text, boolean isSelected) {
        this.iconName = iconName;
        this.text = text;
        this.isSelected = isSelected;

        iconView = new ImageView();
        updateIcon();

        cursorProperty().setValue(Cursor.HAND);
        iconView.setFitHeight(22);
        iconView.setFitWidth(22);

        label = new Label(text);
        label.setFont(Theme.getPoppinsFont(500, 16));
        getStyleClass().addAll("rounded", "menu-item-label","menu-item");
        label.getStyleClass().add("menu-item-label");
        setSelected(isSelected);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(15));
        setSpacing(30);
        getChildren().addAll(iconView, label);

    }

    public String getText() {
        return text;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        updateIcon();
        if (selected) {
            label.setStyle("-fx-text-fill: white");
            label.getStyleClass().add("menu-item-label-selected");
            getStyleClass().add("primary-color-bg");
            setEffect(null);
        } else {
            label.setStyle("-fx-text-fill: inherit");
            label.getStyleClass().remove("menu-item-label-selected");
            getStyleClass().remove("primary-color-bg");
            setOnMouseEntered(e -> applyHoverShadow());
            setOnMouseExited(e -> setEffect(null));
        }
    }

    private void updateIcon() {
        String selectedIconName = isSelected ? iconName.replace(".png", "-selected.png") : iconName;
        Image icon = new Image(getClass().getResourceAsStream("/com/example/images/" + selectedIconName));
        iconView.setImage(icon);
    }

    private void applyHoverShadow() {
        setEffect(Theme.createShadow());
    }

}
