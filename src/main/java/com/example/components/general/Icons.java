package com.example.components.general;

import org.kordamp.ikonli.javafx.FontIcon;

import com.example.components.Theme;

public class Icons extends FontIcon {
    public Icons(String iconCode) {
        super(iconCode);
        setFill(Theme.WHITE_COLOR);
    }
}