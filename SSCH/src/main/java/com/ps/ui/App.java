package com.ps.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = (Parent) loader.load();
        stage.setTitle("SSCH");
        scene = new Scene(root, 600, 750);
        stage.setScene(scene);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("icon.png")));
        stage.show();
    }

    public static void main(String[] args) {
        
        launch();  
   
    }

}
