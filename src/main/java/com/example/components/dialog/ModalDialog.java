package com.example.components.dialog;

import com.example.AppStructure.AppStage;
import com.example.components.Theme;
import com.example.components.general.CustomButton;
import com.example.components.general.CustomSearchBox;
import com.example.util.DateUtil;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModalDialog extends StackPane {

    private VBox modalContent; // Container for dialog content
    private Map<String, CustomSearchBox> customSearchBoxMap; // Map of fields to CustomSearchBox
    private Object entity; // The entity to insert
    private Runnable onConfirm; // Action on confirmation
    private Runnable onCancel; // Action on cancellation
    private Label errorLabel; // Label to display validation or SQL errors

    public ModalDialog() {
        // Modal background styling
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        this.setPickOnBounds(true);
        this.setVisible(false);
        this.setAlignment(Pos.CENTER);

        // Main content container
        modalContent = new VBox(10);
        modalContent.setStyle("-fx-background-color: white; "
                + "-fx-padding: 20; "
                + "-fx-border-radius: 10; "
                + "-fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0.5, 0, 0);");
        modalContent.setMaxSize(400, Region.USE_PREF_SIZE);
        modalContent.setAlignment(Pos.TOP_LEFT);

        // Error label for validation or SQL errors
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);

        this.getChildren().add(modalContent);
        StackPane.setAlignment(modalContent, Pos.CENTER);
    }

    public void setContent(
            Object entity,
            List<FieldDefinition> fieldDefinitions,
            String dialogTitle, // New parameter for title
            String confirmButtonText, // New parameter for confirm button text
            Runnable onConfirm,
            Runnable onCancel) {

        this.entity = entity;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;

        modalContent.getChildren().clear();

        // Modal Header
        VBox modalHeader = new VBox(5);
        Label modalTitle = new Label(dialogTitle); // Use the custom title
        Region spacer = new Region();
        spacer.setPrefHeight(3);
        spacer.setBackground(new Background(new BackgroundFill(Theme.GRAY_COLOR, new CornerRadii(10), Insets.EMPTY)));
        modalTitle.setFont(Theme.getPoppinsFont(600, 20));
        modalTitle.setTextFill(Theme.BLUE_COLOR);

        modalHeader.getChildren().addAll(modalTitle, spacer);
        modalContent.getChildren().add(modalHeader);

        // Initialize errorLabel
        errorLabel.setText("");
        errorLabel.setVisible(false);
        modalContent.getChildren().add(errorLabel);

        customSearchBoxMap = new HashMap<>();

        // Create form fields dynamically based on FieldDefinitions
        for (FieldDefinition fieldDef : fieldDefinitions) {
            String fieldName = fieldDef.getFieldName();
            String displayName = fieldDef.getDisplayName();
            String dataType = fieldDef.getDataType();
            Object value = fieldDef.getValue();

            CustomSearchBox customSearchBox = new CustomSearchBox(displayName, dataType);
            customSearchBox.setText(value != null ? value.toString() : ""); // Default to empty if null
            modalContent.getChildren().add(customSearchBox);
            customSearchBoxMap.put(fieldName, customSearchBox);
        }

        // Buttons
        CustomButton confirmButton = new CustomButton(confirmButtonText); // Use custom button text
        CustomButton cancelButton = new CustomButton("Cancel", true);

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(confirmButton, cancelButton);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        // Confirm Button Action
        confirmButton.setOnAction(event -> handleInsert(fieldDefinitions));

        // Cancel Button Action
        cancelButton.setOnAction(event -> handleCancel());

        modalContent.getChildren().addAll(buttonsBox);
    }

    /**
     * Handles the insert operation when the user clicks "Add".
     */
    private void handleInsert(List<FieldDefinition> fieldDefinitions) {
        boolean hasError = false;
        errorLabel.setText("");
        errorLabel.setVisible(false);

        for (FieldDefinition fieldDef : fieldDefinitions) {
            String fieldName = fieldDef.getFieldName();
            CustomSearchBox customSearchBox = customSearchBoxMap.get(fieldName);
            String newValue = customSearchBox.getText();

            try {
                // Validate and convert input
                Object convertedValue = validateAndConvert(fieldDef.getDataType(), newValue, fieldDef.getDisplayName());

                // Update entity field dynamically using reflection
                updateEntityField(entity, fieldName, convertedValue);

                // Mark field as valid
                customSearchBox.setValid(true);
                customSearchBox.getSearchField().setTooltip(null); // Clear tooltip

            } catch (ValidationException ve) {
                // Validation error
                hasError = true;
                customSearchBox.setValid(false);
                customSearchBox.getSearchField().setTooltip(new Tooltip(ve.getMessage()));
            } catch (Exception e) {
                // General error
                hasError = true;
                errorLabel.setText("An unexpected error occurred: " + simplifyErrorMessage(e.getMessage()));
                errorLabel.setVisible(true);
                e.printStackTrace(); // Optional: Log full error for debugging
            }
        }

        if (!hasError) {
            // If no errors, run confirm action
            if (onConfirm != null) {
                onConfirm.run();
            }
            this.setVisible(false);
        }
    }

    private void handleCancel() {
        // Clear all errors
        errorLabel.setText("");
        errorLabel.setVisible(false);
        customSearchBoxMap.values().forEach(CustomSearchBox::clearValidation);

        if (onCancel != null) {
            onCancel.run();
        }
        this.hideWithAnimation();
    }

    private Object validateAndConvert(String dataType, String value, String displayName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(displayName + " cannot be empty.");
        }

        try {
            switch (dataType.toUpperCase()) {
                case "INT":
                    return Integer.parseInt(value);
                case "DOUBLE":
                    return Double.parseDouble(value);
                case "BOOLEAN":
                    return Boolean.parseBoolean(value);
                case "VARCHAR":
                case "STRING":
                    return value;
                case "DATE":
                    return parseDateFlexible(value, displayName);
                default:
                    throw new ValidationException("Unsupported data type for " + displayName + ".");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException(displayName + " has an invalid format.");
        }
    }

    private void updateEntityField(Object entity, String fieldName, Object value) throws Exception {
        Field field = entity.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        // If the field is a JavaFX Property, use its setValue() method
        if (Property.class.isAssignableFrom(field.getType()) || field.getType().getName().contains("Property")) {
            Object property = field.get(entity);
            if (property instanceof Property) {
                ((Property<Object>) property).setValue(value);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported property type for field: " + fieldName);
            }
        } else {
            // For non-property fields, directly set the value
            field.set(entity, value);
        }
    }

    private String simplifyErrorMessage(String message) {
        if (message.contains(":")) {
            return message.substring(message.lastIndexOf(":") + 1).trim();
        }
        return message;
    }

    public void showWithAnimation() {
        // Ensure modalContent's visibility and position are reset
        modalContent.setOpacity(1.0); // Ensure it's visible
        modalContent.setTranslateY(0); // Reset position in case it was moved
        modalContent.setScaleX(1.0); // Ensure it's full scale
        modalContent.setScaleY(1.0);

        this.setVisible(true); // Make the entire modal visible

        // Animation: Fade in the background
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Animation: Slide and scale the modalContent
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), modalContent);
        slideIn.setFromY(-100); // Start above its normal position
        slideIn.setToY(0); // End at its normal position

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), modalContent);
        scaleIn.setFromX(0.9);
        scaleIn.setFromY(0.9);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        // Apply BoxBlur to the background (exclude modalContent itself)
        // AppStage.getRoot().getChildren().stream()
                // .filter(node -> node != this) // Exclude the current modal dialog
                // .forEach(node -> node.setEffect(new BoxBlur(5, 5, 3)));

        // Play the animations together
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
            this.setVisible(false); // Hide the modal after animation

            // // Clear the blur effect from the background
            // AppStage.getRoot().getChildren().forEach(node -> node.setEffect(null));
            // AppStage.getRoot().setEffect(null);
        });

        hideTransition.play();
    }

    public void show() {
        showWithAnimation();
    }

    public void hide() {
        hideWithAnimation();
    }

    /**
     * Custom exception for validation errors.
     */
    private static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    private Date parseDateFlexible(String dateStr, String displayName) throws ValidationException {
        try {
            return DateUtil.parseDateFlexible(dateStr);
        } catch (ParseException e) {
            throw new ValidationException("Invalid date format for " + displayName
                    + ". Accepted formats: dd-MM-yyyy, dd MM yyyy, dd/MM/yyyy, dd.MM.yyyy.");
        }
    }
}
