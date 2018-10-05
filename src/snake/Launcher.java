/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Ceagan
 */
public class Launcher extends Application{
    
    private final double maximumX = 500, maximumY = 500;
    private Snake snake = new Snake(maximumX, maximumY);
    
    /**
     * Creates the primary display for the game
     * @param stage primary display
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception{
        //variable initializations
        Pane root = new Pane();
        Scene scene = new Scene(root, maximumX, maximumY);
        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        stage.setScene(scene);
        
        //stage declarations
        stage.setTitle("Snake");
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();    
        
        root.getChildren().add(canvas);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
                
        Thread t = new Thread(() -> animate(gc));
        Thread move = new Thread(()->snake.move(gc));        
        t.start();
        move.start();
    }
    
    private void animate(GraphicsContext gc){
        
        while (snake.getState()){        
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            snake.draw(gc);
            pause(100);
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
        System.out.println("Snake closed");
        System.exit(0);
    }
    
    //starts running the program
    public static void main(String[] args) {
        launch(args);
    }
}
