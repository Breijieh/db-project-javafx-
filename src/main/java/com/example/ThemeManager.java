package com.example;

import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static String currentTheme = "light"; // Default theme
    private static final List<Scene> registeredScenes = new ArrayList<>();

    public static void registerScene(Scene scene) {
        if (!registeredScenes.contains(scene)) {
            registeredScenes.add(scene);
            applyTheme(scene);
        }
    }

    public static void setTheme(String theme) {
        currentTheme = theme;
        for (Scene scene : registeredScenes) {
            applyTheme(scene);
        }
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

    private static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();

        // Load common styles
        String commonStyles = ThemeManager.class.getResource("/com/example/css/common.css").toExternalForm();

        // Load theme-specific styles
        String themeStyles = ThemeManager.class.getResource("/com/example/css/" + currentTheme + "-theme.css")
                .toExternalForm();

        scene.getStylesheets().addAll(commonStyles, themeStyles);
    }
}
