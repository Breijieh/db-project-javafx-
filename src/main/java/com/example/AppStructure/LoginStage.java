package com.example.AppStructure;

import com.example.AppStructure.aside.Logo;
import com.example.components.Theme;
import com.example.components.general.CustomButton;
import com.example.components.general.CustomInput;
import com.example.components.general.CustomPassword;
import com.example.components.general.Icons;
import com.example.dao.UserDAO;
import com.example.model.UserAccount;
import com.example.session.SessionManager;
// import com.mysql.cj.log.Log;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class LoginStage extends Stage {
        private static LoginStage instance;

        public LoginStage() {
                instance = this;
                FlowPane sceneContainer = new FlowPane();

                HBox container = new HBox();
                container.setPrefSize(800, 500);

                // Left side setup
                VBox leftSide = new VBox();
                leftSide.setPrefWidth(400);
                leftSide.setPadding(new Insets(40));
                leftSide.setSpacing(35);

                // Image background for leftSide
                leftSide.setBackground(new Background(new BackgroundImage(
                                new Image(getClass().getResource("/com/example/images/loginBackground.png")
                                                .toExternalForm()),
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.DEFAULT,
                                new BackgroundSize(
                                                BackgroundSize.AUTO, // Width
                                                BackgroundSize.AUTO, // Height
                                                false, // Absolute width
                                                false, // Absolute height
                                                true, // Cover the region
                                                true // Contain within the region
                                ))));

                // Clip the left side for rounded corners
                Rectangle clip = new Rectangle(400, 500);
                clip.setArcWidth(60);
                clip.setArcHeight(60);
                leftSide.setClip(clip);

                VBox title = new VBox();
                Label hello = new Label("Hello To");
                hello.setFont(Theme.getPoppinsFont(600, 32));
                hello.setTextFill(Color.WHITE);

                Logo logo = new Logo();
                logo.setFill(Theme.WHITE_COLOR);
                logo.setFont(Theme.getPoppinsFont(800, 36));
                title.getChildren().addAll(hello, logo);

                Label description = new Label("BorjCars helps you automate repetitive sales and marketing tasks.\n" +
                                "Get highly productive through automation and save tons of time!");
                description.setFont(Theme.getPoppinsFont(300, 15));
                description.setTextFill(Theme.GRAY_COLOR);
                description.setWrapText(true);

                Label footer = new Label("© 2024 BorjCars. All rights reserved.");
                footer.setFont(Theme.getPoppinsFont(300, 14));
                footer.setTextFill(Color.LIGHTGRAY);

                Label icon = new Label();
                Icons iconGraphic = new Icons("fa-sign-in");
                icon.setFont(Theme.getPoppinsFont(900, 60));
                icon.setGraphic(iconGraphic);
                leftSide.getChildren().addAll(icon, title, description, footer);

                // Right side setup
                VBox rightSide = new VBox();
                rightSide.setAlignment(Pos.CENTER);
                rightSide.setPadding(new Insets(40));
                rightSide.setSpacing(20);
                rightSide.setBackground(
                                new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0, 30, 30, 0, false),
                                                Insets.EMPTY)));

                Label formTitle = new Label("Login to BorjCars");
                formTitle.setFont(Theme.getPoppinsFont(600, 24));
                formTitle.setTextFill(Theme.NAVY_COLOR);

                Label formSubtitle = new Label("Enter the essential information to have the access");
                formSubtitle.setFont(Theme.getPoppinsFont(400, 14));
                formSubtitle.setTextFill(Color.GRAY);

                CustomInput usernameField = new CustomInput("Username");
                usernameField.setPromptText("Email");
                usernameField.setPrefWidth(300);

                CustomPassword passwordField = new CustomPassword("Password");
                passwordField.setPrefWidth(300);

                CustomButton loginButton = new CustomButton("Log in");
                loginButton.setStyle("-fx-padding: 10 20;");
                loginButton.setPrefWidth(350);

                rightSide.getChildren().addAll(formTitle, formSubtitle, usernameField, passwordField, loginButton);

                container.getChildren().addAll(leftSide, rightSide);

                sceneContainer.getChildren().add(container);
                container.setBackground(new Background(new BackgroundFill(
                                Color.TRANSPARENT,
                                new CornerRadii(30),
                                Insets.EMPTY)));
                container.setAlignment(Pos.CENTER);
                container.setEffect(Theme.createShadow());
                sceneContainer.setAlignment(Pos.CENTER);
                sceneContainer.setBackground(
                                new Background(new BackgroundFill(Theme.BACKGROUND_COLOR, new CornerRadii(30),
                                                Insets.EMPTY)));
                Scene scene = new Scene(sceneContainer);
                scene.getStylesheets().add(getClass().getResource("/com/example/css/light-theme.css").toExternalForm());

                // stage properties
                setScene(scene);
                setTitle("BorjCars - Login");
                setMaximized(true);
                // _stage properties

                // Login button event
                loginButton.setOnAction(event -> {
                        String username = usernameField.getText();
                        String password = passwordField.getText();

                        UserDAO userDAO = new UserDAO();
                        UserAccount user = userDAO.getUserByUsernameAndPassword(username, password);

                        if (user != null) {
                                SessionManager.getInstance().setCurrentUser(user);

                                this.close();
                                AppStage appScene = new AppStage();
                                appScene.show();
                        } else {
                                formSubtitle.setText("Invalid username or password. Try again.");
                                formSubtitle.setTextFill(Color.RED);
                        }
                });
                // _Login button event
        }

        public static LoginStage getInstance() {
                return instance;
        }
}
