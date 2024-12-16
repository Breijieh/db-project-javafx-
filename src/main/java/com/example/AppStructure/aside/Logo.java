package com.example.AppStructure.aside;

import com.example.components.Theme;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Logo extends Text {
    public Logo() {
        super("BorjCars");
        setFont(Theme.getPoppinsFont(800, 30));
        setFill(Theme.BLUE_COLOR);
    }

}
