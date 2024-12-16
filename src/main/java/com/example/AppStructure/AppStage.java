package com.example.AppStructure;

import com.example.AppStructure.aside.Aside;
import com.example.AppStructure.main.Main;
import com.example.components.dialog.ConfirmationDialog;
import com.example.components.dialog.FieldDefinition;
import com.example.components.dialog.MessageDialog;
import com.example.components.dialog.ModalDialog;
import com.example.components.dialog.DetailsDialog; // Import the new DetailsDialog
import com.example.session.SessionManager;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * The main application stage that manages the primary scene,
 * modals, messages, confirmations, and theme settings.
 */
public class AppStage extends Stage {

    // Singleton instance of AppStage
    private static AppStage instance;

    // Root layout
    private final StackPane root;

    // Dialog components
    private final ModalDialog modalDialog;
    private final MessageDialog messageDialog;
    private final ConfirmationDialog confirmationDialog;
    private final DetailsDialog detailsDialog; // New DetailsDialog instance

    // Scene
    private Scene scene;

    // Theme tracking
    private boolean isDarkTheme = false;

    // Preferences key for theme
    private static final String PREF_THEME = "theme";
    private final Preferences prefs = Preferences.userNodeForPackage(AppStage.class);

    /**
     * Constructor initializes the main stage with all components.
     */
    public AppStage() {
        // Set the singleton instance
        instance = this;

        // Initialize root layout
        root = new StackPane();

        // Initialize main content layout
        HBox mainContent = new HBox();

        // Create main view and allow it to grow
        Main main = new Main();
        HBox.setHgrow(main, Priority.ALWAYS);

        // Add aside and main content to the layout
        mainContent.getChildren().addAll(new Aside(), main);

        // Initialize dialog components
        modalDialog = new ModalDialog();
        messageDialog = new MessageDialog();
        confirmationDialog = new ConfirmationDialog();
        detailsDialog = new DetailsDialog(); // Initialize DetailsDialog

        // Add all components to the root layout
        root.getChildren().addAll(mainContent, modalDialog, messageDialog, confirmationDialog, detailsDialog);

        // Initialize scene with root layout and default size
        scene = new Scene(root, 1400, 800);

        // Add light theme stylesheet by default
        scene.getStylesheets().add(getClass().getResource("/com/example/css/light-theme.css").toExternalForm());

        // Load theme preference
        boolean dark = prefs.getBoolean(PREF_THEME, false);
        if (dark) {
            // If dark theme is preferred, add dark theme stylesheet
            scene.getStylesheets().add(getClass().getResource("/com/example/css/dark-theme.css").toExternalForm());
            isDarkTheme = true;
        } else {
            isDarkTheme = false;
        }

        // Set the scene to the stage
        this.setScene(scene);

        // Set the title with the current user's username
        this.setTitle("Main Application - " + SessionManager.getInstance().getCurrentUser().getUsername());
    }

    /**
     * Retrieves the singleton instance of AppStage.
     *
     * @return The AppStage instance.
     */
    public static AppStage getInstance() {
        return instance;
    }

    public static StackPane getRoot() {
        return instance.root;
    }

    public void showModal(
            Object entity,
            List<FieldDefinition> fieldDefinitions,
            String dialogTitle, // Title of the dialog
            String confirmButtonText, // Text for the confirm button
            Runnable onConfirm,
            Runnable onCancel) {
        modalDialog.setContent(entity, fieldDefinitions, dialogTitle, confirmButtonText, onConfirm, onCancel);
        modalDialog.show();
    }

    public void hideModal() {
        modalDialog.hide();
    }

    public void showMessage(String title, String message, MessageDialog.MessageType type) {
        messageDialog.show(title, message, type);
    }

    public void showConfirmation(String title, String message, Runnable onYes, Runnable onNo) {
        confirmationDialog.show(title, message, onYes, onNo);
    }

    public void toggleTheme() {
        if (isDarkTheme) {
            // Remove dark-theme.css to revert to light theme
            scene.getStylesheets().remove(getClass().getResource("/com/example/css/dark-theme.css").toExternalForm());
            isDarkTheme = false;
            prefs.putBoolean(PREF_THEME, false);
        } else {
            // Add dark-theme.css on top of light-theme.css
            if (!scene.getStylesheets()
                    .contains(getClass().getResource("/com/example/css/dark-theme.css").toExternalForm())) {
                scene.getStylesheets().add(getClass().getResource("/com/example/css/dark-theme.css").toExternalForm());
            }
            isDarkTheme = true;
            prefs.putBoolean(PREF_THEME, true);
        }
    }

    public void setTheme(boolean dark) {
        if (dark && !isDarkTheme) {
            toggleTheme();
        } else if (!dark && isDarkTheme) {
            toggleTheme();
        }
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public ModalDialog getModalDialog() {
        return modalDialog;
    }

    public DetailsDialog getDetailsDialog() {
        return detailsDialog;
    }
}
