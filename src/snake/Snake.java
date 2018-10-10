
package snake;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static snake.menu.toggleVisibility;

/**
 * This class functions as a container for all the values of the snake
 * @author Ceagan
 */
public class Snake {
    private double head[];
    private double body[][]; //uses the x and y values
    private int bodyLength = 4; //the amount of parts on the snake
    private double step = 10, stepX = 0, stepY = 0;
    private final int bodySize = 10; //full body length
    private int score = 0;
    private double x, y; //the canvas limit
    private boolean collision = false; //handles when the user hits themselves
    private boolean unchanged = true; //when just bare defaults are present in the program this is false, otherwise true
    
    private boolean state; //true while running false while not, control the threads to only run after the user starts to move
    
    private double foodX = -50, foodY = -50;//created off screen
    
    /**
     * Primary constructor, no override methods available. 
     * @param maxX the maximum width
     * @param maxY the maximum height
     */
    public Snake(double maxX, double maxY){
        x = maxX;
        y = maxY;
        head = new double[2]; //contains the values of x and y
        body = new double[bodyLength][2];
        
        //starting location, should be the middle
        head[0] = maxX / 2 - bodySize;
        head[1] = maxY / 2 - bodySize;
               
        state = true;
        
        for (int i = bodyLength - 1; i >= 0; i--){
            body[i][0] = head[0] - stepX;
            body[i][1] = head[1] - stepY;
        }
        
        foodX = (int)(Math.random() * (x / 10)) * bodySize; //spawns the food on the grid
        foodY = (int)(Math.random() * (y / 10)) * bodySize; //spawns the food on the grid
                    
    }
    
    /**
     * Based off of key press the snake will perform an action
     * @param gc used for identifying user interaction
     */
    public void checkInput(GraphicsContext gc){
        while (state == true){
            gc.getCanvas().getScene().setOnKeyPressed((KeyEvent ke) -> {
                switch (String.valueOf(ke.getCode()).toUpperCase()) {
                    //movement functions when user presses a key.
                    case "UP":
                        stepX = 0;
                        stepY = -step;
                        unchanged = false;
                        break;
                    case "DOWN":
                        stepX = 0;
                        stepY = step;
                        unchanged = false;

                        break;
                    case "LEFT":
                        stepY = 0;
                        stepX = -step;
                        unchanged = false;                        
                        break;    
                    case "RIGHT":
                        stepY = 0;
                        stepX = step;
                        unchanged = false;
                        break;   
                    //additional functions added in
                    case "R":
                        //ADD A FUNCTION TO EASILY RESTART THE APPLICATION
                        break;
                    case "ESCAPE":
                        state = false;
                        break;
                    case "X":
                        state = false;
                        break;
                    //built in tostring to check values during runtime
                    case "T":
                        String output = "";
                        output += "\nHead X: " + head[0];
                        output += "\nHead Y: " + head[1];
                        for (double[] b : body) {
                            output += "\nBody X: " + b[0];
                            output += "\nBody X: " + b[1];
                        }
                        output += "\nBody Length: " + bodyLength; //the amount of parts on the snake
                        output += "\nStep X: " + stepX;
                        output += "\nStep Y: " + stepY;
                        output += "\nBody Size: " + bodySize; //full body length
                        output += "\nCurrent Score: " + score;
                        output += "\nHas Collided: " + collision; //handles when the user hits themselves
                        output += "\nValues Unchanged: " + unchanged; //when just bare defaults are present in the program this is false, otherwise true
                        System.out.println(output);
                        break;
                    default:
                        break;
                }
            });       
            checkConstraints();
        }
    }
    
    /**
     * used to check when collision occurs with the wall and with the food. Also handles the addition of size
     */
    private void checkConstraints(){
        if (head[0] > x - step){ //greater than right wall
            head[0] = 0;
        }
        if (head[0] < 0){ //lesser than left wall
            head[0] = x - step;
        }
        if (head[1] > y - step){ //greater than bottom wall
            head[1] = 0;
        }
        if (head[1] < 0){
            head[1] = y - step; //lesser than top wall
        }
        
        //for the top right of the head to the bottom right if any pixel matches that of the food it will trigger the statements within
        for (double ix = head[0]; ix <= head[0] + bodySize; ix++){
            for (double iy = head[1]; iy <= head[1] + bodySize; iy++){
                if (ix > foodX && ix < foodX + bodySize && iy > foodY && iy < foodY + bodySize){//food trigger
                    
                    foodX = (int)(Math.random() * (x / 10)) * bodySize; //new random x
                    foodY = (int)(Math.random() * (y / 10)) * bodySize; //new random y
                    
                    bodyLength += 1; //size goes up by 1
                    //CODE BELOW, STORE TEMP TO EXPAND SIZE THEN RETURN VALUES
                    double tempX[] = new double[bodyLength];
                    double tempY[] = new double[bodyLength];
                    for (int z = 0; z < bodyLength -1; z++){
                        tempX[z] = body[z][0];
                        tempY[z] = body[z][1];
                    }
                    body = new double[bodyLength][2];//recreate body here
                    for (int z = 0; z < bodyLength -1; z++){ //insert values into appropriate location here
                        body[z][0] = tempX[z];
                        body[z][1] = tempY[z];
                    }
                    
                    if (collision == true){
                        stop();
                    }
                    //END OF POSITION TRANSFERRAL
                }
            }
        }
    }
    
    public void move(){
        //create snake in reverse order, it moves back to front, sensible to move it in that motion also
        for (int i = bodyLength - 1; i > 0; i--){
            body[i][0] = body[i - 1][0];
            body[i][1] = body[i - 1][1];
        }
        body[0][0] = head[0];
        body[0][1] = head[1];

        head[0] += stepX;
        head[1] += stepY;
        //checks for self collision
        for (double ix = head[0]; ix <= head[0] + bodySize; ix++){
            for (double iy = head[1]; iy <= head[1] + bodySize; iy++){
                for (double[] b : body){
                    if (ix > b[0] && ix < b[0] + bodySize && iy > b[1] && iy < b[1] + bodySize){
                        collision = true;
                        if (!unchanged){
                            stop();
                        }
                    }
                }
            }
        }
        
    }
    
    /**
     * the primary drawing output for the snake, utilizes the parent object
     * @param g 
     */
    public void draw(GraphicsContext g){
        if (stepX != 0 || stepY != 0){
            g.setFill(Color.GREEN);
            g.fillRect(foodX, foodY, bodySize, bodySize);
            
            move();
            
            //drawing the snake after the movements have been made
            g.setFill(Color.WHITE);
            //x location, y location, width, height
            g.fillRect(head[0], head[1], bodySize, bodySize);

            g.setFill(Color.WHITE);
            for (double[] b : body){
                g.fillRect(b[0], b[1], bodySize, bodySize);
                g.setStroke(Color.BLACK);
                g.strokeRect(b[0], b[1], bodySize, bodySize);
            }
        }
    }
    
    /**
     * used to identify whether the snake is alive or not 
     * @return 
     */
    public boolean getState(){
        return state;
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
    
    public void stop() {         
        menu.toggleVisibility();
        
        System.exit(0);
    }
}


