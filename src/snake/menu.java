/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Ceagan
 */
public class menu extends Application{
    
    private final double maximumX = 500, maximumY = 500;
    private static Stage stage;
    /**
     * Creates the primary display for the game
     * @param stage primary display
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;
        //variable initializations AKA object initialization
        Pane root = new Pane();
        Scene scene = new Scene(root, maximumX, maximumY);
        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        stage.setScene(scene);
        //secondary object initialization
        Button snakeBtn = new Button("Snake");
        
        //secondary object specifications done here, same order they are declared
        snakeBtn.setLayoutX(100);
        snakeBtn.setLayoutY(100);
        snakeBtn.setOnAction(this::buttonClicked);
     
        //stage declarations
        stage.setTitle("Games Menu");
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();    
        
        //adding objects to the pane
        root.getChildren().add(canvas);
        root.getChildren().add(snakeBtn);
        
        //declaring the drawing surface
        GraphicsContext gc = canvas.getGraphicsContext2D();
    }
 
    private void buttonClicked(ActionEvent e){
        Platform.runLater(() -> {
            try {
                new Launcher().start(new Stage());
                toggleVisibility();
            } catch (Exception ex) {
                Alert alert = new Alert("Alert", "Error launching Snake");
            }
        });
        
        System.out.println("ButtonClicked");
    }
    
    public static void toggleVisibility(){
        System.out.println("Visibility toggled");
        if (stage.isShowing() == false){
            stage.show();
        } else if (stage.isShowing() == true) {
            stage.hide();
        }        
    }

    /**
     * Used instead of thread.sleep() handles possible errors.
     *
     * @param duration time in milliseconds
     */
    private static void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
        }
    }

    //ends program run duration. Kills all tasks
    @Override
    public void stop() {
        System.exit(0);
    }
    
    //starts running the program
    public static void main(String[] args) {
        launch(args);
    }
}
