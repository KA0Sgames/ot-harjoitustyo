package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import domain.Player;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import domain.Controller;
import javafx.scene.paint.Color;

public class Ui extends Application {
    private Scene loginScene;
    private Scene userCreationScene;
    private Scene characterScene;
    private Scene gameScene;
    private Controller controller;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        this.controller = new Controller();
        // login scene:
        
        GridPane loginPane = new GridPane();
        loginPane.setPrefSize(400, 300);
        loginPane.setPadding(new Insets(10));
        loginPane.setVgap(10);
        loginPane.setHgap(10);
        
        Label errorMessage = new Label("");
        TextField usernameInput = new TextField();
        PasswordField passwordInput = new PasswordField();
        Button loginButton = new Button("Login");
        Button createUserButton = new Button("Create user");
        
        loginPane.add(errorMessage, 2, 1);
        loginPane.add(new Label("Username"), 1, 2);
        loginPane.add(usernameInput, 2, 2);
        loginPane.add(new Label("Password"), 1, 3);
        loginPane.add(passwordInput, 2, 3);
        loginPane.add(loginButton, 2, 4);
        loginPane.add(createUserButton, 2, 5);
        
        this.loginScene = new Scene(loginPane);
        
        loginButton.setOnAction(e -> {
            if (!this.controller.askIfUserExists(usernameInput.getText().trim())) {
                errorMessage.setText("Username doesn't exist");
                errorMessage.setTextFill(Color.RED);
            } else if (!this.controller.askIfPasswordMatches(usernameInput.getText().trim(), passwordInput.getText())) {
                errorMessage.setText("Password incorrect!");
                errorMessage.setTextFill(Color.RED);
            } else {
                stage.setScene(this.characterScene);
            }
        });
        
        createUserButton.setOnAction(e -> {
            errorMessage.setText("");
            usernameInput.setText("");
            passwordInput.setText("");
            stage.setScene(this.userCreationScene);
        });
        
        
        // user creation scene:
        
        GridPane userCreationPane = new GridPane();
        userCreationPane.setPrefSize(400, 300);
        userCreationPane.setPadding((new Insets(10)));
        userCreationPane.setVgap(10);
        userCreationPane.setHgap(10);
        
        Label errorInCreation = new Label("");
        TextField newUsername = new TextField();
        TextField newPassword = new TextField();
        Button createUser = new Button("Create");
        
        userCreationPane.add(errorInCreation, 2, 1);
        userCreationPane.add(new Label("Username:"), 1, 2);
        userCreationPane.add(newUsername, 2, 2);
        userCreationPane.add(new Label("Password:"), 1, 3);
        userCreationPane.add(newPassword, 2, 3);
        userCreationPane.add(createUser, 2, 4);
        
        this.userCreationScene = new Scene(userCreationPane);
        
        createUser.setOnAction(e -> {
            if (this.controller.askIfUserExists(newUsername.getText().trim())) {
                errorInCreation.setText("Username is already reserved");
                errorInCreation.setTextFill(Color.RED);
            } else if (newUsername.getText().trim().length() < 4) {
                errorInCreation.setText("Username is too short, at least 4 characters needed.");
                errorInCreation.setTextFill(Color.RED);
            } else if (newPassword.getText().trim().length() < 4) {
                errorInCreation.setText("Password is too short, at least 4 characters needed.");
                errorInCreation.setTextFill(Color.RED);
            } else {
                this.controller.createUser(newUsername.getText().trim(), newPassword.getText().trim());
                errorInCreation.setText("");
                newUsername.setText("");
                newPassword.setText("");
                stage.setScene(this.loginScene);
            }
        });
        
        // character scene:
        
        GridPane characterPane = new GridPane();
        characterPane.setPrefSize(400, 300);
        characterPane.setPadding((new Insets(10)));
        characterPane.setVgap(10);
        characterPane.setHgap(10);
        
        
        // game scene
        
        Pane screen = new Pane();
        screen.setPrefSize(1200, 800);
        
        Player player = new Player(50, 400);
        
        screen.getChildren().add(player.getCharacter());
        
        this.gameScene = new Scene(screen);
        stage.setTitle("Caventure");
        stage.setScene(loginScene);
        stage.show();
        
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        
        this.gameScene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });
        
        this.gameScene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });
        
        new AnimationTimer() {
            
            @Override
            public void handle(long present) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, Boolean.FALSE)) {
                    player.moveLeft();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)) {
                    player.moveRight();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.UP, Boolean.FALSE)) {
                    player.moveUp();
                }
                
                if (pressedKeys.getOrDefault(KeyCode.DOWN, Boolean.FALSE)) {
                    player.moveDown();
                }
            }
        }.start();
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
