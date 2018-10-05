
package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author Ceagan
 */
public class Snake {
    private double head[];
    private double body[][]; //uses the x and y values
    private int bodyLength = 4; //the amount of parts on the snake
    private double step = 10, stepX = 0, stepY = 0;
    private final int bodySize = 10; //full body length
    private int score;
    private double x, y; //the canvas limit
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
            body[i][0] = head[0];
            body[i][1] = head[1];
        }
        
        foodX = (int)(Math.random() * 500); //should create a grid the food spawns on
        foodY = (int)(Math.random() * 500); //should create a grid the food spawns on
    }
    
    /**
     * Based off of key press the snake will perform an action
     * @param gc used for identifying user interaction
     */
    public void move(GraphicsContext gc){
        while (state == true){
            gc.getCanvas().getScene().setOnKeyPressed((KeyEvent ke) -> {
                switch (String.valueOf(ke.getCode()).toUpperCase()) {
                    //movement functions when user presses a key.
                    case "UP":
                        stepX = 0;
                        stepY = -step;
                        break;
                    case "DOWN":
                        stepX = 0;
                        stepY = step;
                        break;
                    case "LEFT":
                        stepY = 0;
                        stepX = -step;
                        break;    
                    case "RIGHT":
                        stepY = 0;
                        stepX = step;
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
        
        for (int i = bodyLength - 1; i > 0; i--){
                body[i][0] = body[i - 1][0];
                body[i][1] = body[i - 1][1];
            }
        body[0][0] = head[0];
        body[0][1] = head[1];

        head[0] += stepX;
        head[1] += stepY;
        
        for (double ix = head[0]; ix <= head[0] + bodySize; ix++){
            for (double iy = head[1]; iy <= head[1] + bodySize; iy++){
                for (int i = bodyLength - 1; i > 0; i--){
                    if (body[i][0] == ix && body[i][1] == iy){
                        stop();
                    }
                }
            }
        }    
        
        //for the top right of the head to the bottom right if any pixel matches that of the food it will trigger the statements within
        for (double ix = head[0]; ix <= head[0] + bodySize; ix++){
            for (double iy = head[1]; iy <= head[1] + bodySize; iy++){
                if (ix > foodX && ix < foodX + bodySize && iy > foodY && iy < foodY + bodySize){
                    
                    foodX = (int)(Math.random() * (x / 10)) * bodySize;
                    foodY = (int)(Math.random() * (y / 10)) * bodySize;
                    
                    bodyLength += 1;
                    double tempX[] = new double[bodyLength];
                    double tempY[] = new double[bodyLength];
                    for (int z = 0; z < bodyLength -1; z++){
                        tempX[z] = body[z][0];
                        tempY[z] = body[z][1];
                    }
                    
                    body = new double[bodyLength][2];
                    
                    for (int z = 0; z < bodyLength -1; z++){
                        body[z][0] = tempX[z];
                        body[z][1] = tempY[z];
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
            
            //create snake in reverse order, it moves back to front, sensible to move it in that motion also
            for (int i = bodyLength - 1; i > 0; i--){
                body[i][0] = body[i - 1][0];
                body[i][1] = body[i - 1][1];
            }
            body[0][0] = head[0];
            body[0][1] = head[1];

            head[0] += stepX;
            head[1] += stepY;


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
        System.out.println("Snake closed");
        System.exit(0);
    }
}


