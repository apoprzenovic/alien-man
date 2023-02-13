import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.*;

public class PacmanPlayer extends Pane {

    private Double racePosX = 440.0; // x position of the racer
    private Double racePosY = 470.0; // y position of the racer
    private Boolean allowMovement = false;

    private int movementPacmanX = 0;
    private int movementPacmanY = 0;

    private String keyPressed = null;
    private int turn = 0;

    private ImageView pacmanImageView; // a view of the icon ... used to display and move the image

    private Image red_green_background_image = null;
    private Image pacmanImage1 = null;
    private Image pacmanImage1Flipped = null;

    private Image pacmanImage1Nitrous = null;
    private Image pacmanImage1NitrousFlipped = null;

    private final static String ICON_IMAGE = "./media/images/icons/packman.gif"; // file with icon for a racer
    private final static String ICON_IMAGE2 = "./media/images/icons/packmanFlipped.gif"; // file with icon for a

    private final static String ICON_IMAGE_NITROUS = "./media/images/icons/packmanNitrous.gif"; // file with icon for a
    private final static String ICON_IMAGE_NITROUS2 = "./media/images/icons/packmanNitrousFlipped.gif"; // file wit

    private final static String ICON_IMAGE_BLUE = "./media/images/icons/packman2.gif"; // file with icon for a racer
    private final static String ICON_IMAGE_BLUE2 = "./media/images/icons/packman2Flipped.gif"; // file with icon for a

    private final static String ICON_IMAGE_NITROUS_BLUE = "./media/images/icons/packman2Nitrous.gif"; // file with icon
                                                                                                      // for a
    private final static String ICON_IMAGE_NITROUS_BLUE2 = "./media/images/icons/packman2NitrousFlipped.gif"; // file
                                                                                                              // wit

    private final String RED_GREEN_BACKGROUND_PATH = "./media/images/background/red-green-background.png";

    private Boolean forBooleanChecker = true;
    private Boolean moveFromWall = false;

    private Boolean moveRightBoolean = false;
    private Boolean moveLeftBoolean = false;
    private Boolean moveDownBoolean = false;
    private Boolean moveUpBoolean = false;

    private char moveFromWallDirection;

    private final Double ACCELERATION = 1.40;
    private boolean allowAcceleration = false;

    private Integer pacmanID = -1;

    public PacmanPlayer(Integer pacmanID) {

        this.pacmanID = pacmanID;

        // Draw the icon for the racer
        try {
            if (pacmanID == 0) {
                pacmanImage1 = new Image(new FileInputStream(ICON_IMAGE));
                pacmanImage1Flipped = new Image(new FileInputStream(ICON_IMAGE2));
                pacmanImage1Nitrous = new Image(new FileInputStream(ICON_IMAGE_NITROUS));
                pacmanImage1NitrousFlipped = new Image(new FileInputStream(ICON_IMAGE_NITROUS2));
            }
            else if (pacmanID == 1) {
                pacmanImage1 = new Image(new FileInputStream(ICON_IMAGE_BLUE));
                pacmanImage1Flipped = new Image(new FileInputStream(ICON_IMAGE_BLUE2));
                pacmanImage1Nitrous = new Image(new FileInputStream(ICON_IMAGE_NITROUS_BLUE));
                pacmanImage1NitrousFlipped = new Image(new FileInputStream(ICON_IMAGE_NITROUS_BLUE2));
            }
            red_green_background_image = new Image(new FileInputStream(RED_GREEN_BACKGROUND_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pacmanImageView = new ImageView(pacmanImage1);

        pacmanImageView.setX(racePosX);
        pacmanImageView.setY(racePosY);
        this.getChildren().add(pacmanImageView);

    }

    public void setX(Double x){
        racePosX = x;
    }

    public void setY(Double y){
        racePosY = y;
    }

    public Double getX(){
        return racePosX;
    }

    public Double getY(){
        return racePosY;
    }

    public void hitNitrous() {
        allowAcceleration = true;
        System.out.println("Nitrous brmmmmmmmmmmmm");
    }

    public void ranOutOfNitrous() {
        allowAcceleration = false;
        System.out.println("Out of NOs :(");
    }

    public void restartPosition() {
        racePosX = 440.0;
        racePosY = 470.0;
    }

    /**
     * update() method keeps the thread (racer) alive and moving.
     */
    public void update() {

        keyListener();

        if (allowMovement) {

            booleanChecker();

            moveRight();
            moveLeft();
            moveUp();
            moveDown();

            if (allowAcceleration) {
                racePosX += (movementPacmanX * ACCELERATION);
                racePosY += (movementPacmanY * ACCELERATION);
            } else if (!allowAcceleration) {
                racePosX += (movementPacmanX);
                racePosY += (movementPacmanY);
            }

            pacmanImageView.setX(racePosX);
            pacmanImageView.setY(racePosY);
            pacmanImageView.setRotate(turn);

            moveFromWall();

        }

    } // end update()

    public Integer getPacmanID(){
        return pacmanID;
    }

    public void allowMovementMethodForMultiplayer(){
        allowMovement = true;
    }

    public void moveFromWall() {

        switch (moveFromWallDirection) {
            case 'L':
                if (moveFromWall) {
                    forBooleanChecker = true;
                    racePosX += 2;
                }
                break;
            case 'R':
                if (moveFromWall) {
                    forBooleanChecker = true;
                    racePosX -= 2;
                }
                break;
            case 'U':
                if (moveFromWall) {
                    forBooleanChecker = true;
                    racePosY += 2;
                }
                break;
            case 'D':
                if (moveFromWall) {
                    forBooleanChecker = true;
                    racePosY -= 2;
                }
                break;
            default:
                break;
        }

        moveFromWall = false;
    }

    public void booleanChecker() {

        int[] rgbArrayTopLeft = getPixelRGB((int) (pacmanImageView.getX()), (int) (pacmanImageView.getY()));
        int[] rgbArrayBottomRight = getPixelRGB((int) (pacmanImageView.getX() + 25),
                (int) (pacmanImageView.getY() + 25));

        if (((rgbArrayTopLeft[0] == 200 && rgbArrayTopLeft[1] == 1 && rgbArrayTopLeft[2] == 1)
                || (rgbArrayBottomRight[0] == 200 && rgbArrayBottomRight[1] == 1 && rgbArrayBottomRight[2] == 1))
                && forBooleanChecker) {

            if (movementPacmanX == 0 && movementPacmanY == -1) {
                // UP
                movementPacmanY = 0;
                turn = 90;
                if (allowAcceleration)
                    pacmanImageView.setImage(pacmanImage1Nitrous);
                else
                    pacmanImageView.setImage(pacmanImage1);
                moveFromWall = true;
                forBooleanChecker = false;
                moveFromWallDirection = 'U';
            } else if (movementPacmanX == 1 && movementPacmanY == 0) {
                // RIGHT
                movementPacmanX = 0;
                turn = 180;
                if (allowAcceleration)
                    pacmanImageView.setImage(pacmanImage1Nitrous);
                else
                    pacmanImageView.setImage(pacmanImage1);
                moveFromWall = true;
                forBooleanChecker = false;
                moveFromWallDirection = 'R';
            } else if (movementPacmanX == 0 && movementPacmanY == 1) {
                // DOWN
                movementPacmanY = 0;
                turn = -90;
                if (allowAcceleration)
                    pacmanImageView.setImage(pacmanImage1Nitrous);
                else
                    pacmanImageView.setImage(pacmanImage1);
                moveFromWall = true;
                forBooleanChecker = false;
                moveFromWallDirection = 'D';
            } else if (movementPacmanX == -1 && movementPacmanY == 0) {
                // LEFT
                movementPacmanX = 0;
                turn = 0;
                if (allowAcceleration)
                    pacmanImageView.setImage(pacmanImage1NitrousFlipped);
                else
                    pacmanImageView.setImage(pacmanImage1Flipped);
                moveFromWall = true;
                forBooleanChecker = false;
                moveFromWallDirection = 'L';
            }

        } // if end for hitting red

    } // booleanChecker end

    public void moveRight() {

        if (!checkRedArea((int) (pacmanImageView.getX() + 45), (int) (pacmanImageView.getY() + 13))
                && !checkRedArea((int) (pacmanImageView.getX() + 45), (int) (pacmanImageView.getY() - 1))
                && !checkRedArea((int) (pacmanImageView.getX() + 45),
                        (int) (pacmanImageView.getY() + 26))
                && moveRightBoolean) { // check
            // RIGHT
            movementPacmanX = 1;
            movementPacmanY = 0;
            turn = 0;
            if (allowAcceleration)
                pacmanImageView.setImage(pacmanImage1Nitrous);
            else
                pacmanImageView.setImage(pacmanImage1);

        }

    } // moveRight() end

    public void moveLeft() {

        if (!checkRedArea((int) (pacmanImageView.getX() - 20), (int) (pacmanImageView.getY() - 1))
                && !checkRedArea((int) (pacmanImageView.getX() - 20),
                        (int) (pacmanImageView.getY() + 13))
                && !checkRedArea((int) (pacmanImageView.getX() - 20),
                        (int) (pacmanImageView.getY() + 26))
                && moveLeftBoolean) { // checks
            // LEFT
            movementPacmanX = -1;
            movementPacmanY = 0;
            if (allowAcceleration)
                pacmanImageView.setImage(pacmanImage1NitrousFlipped);
            else
                pacmanImageView.setImage(pacmanImage1Flipped);
        }

    } // moveLeft() end

    public void moveUp() {

        if (!checkRedArea((int) (pacmanImageView.getX() + 13), (int) (pacmanImageView.getY() - 25))
                && !checkRedArea((int) (pacmanImageView.getX() - 1), (int) (pacmanImageView.getY() - 25))
                && !checkRedArea((int) (pacmanImageView.getX()) + 26,
                        (int) (pacmanImageView.getY() - 25))
                && moveUpBoolean) { // checks
            // UP
            movementPacmanX = 0;
            movementPacmanY = -1;
            turn = -90;
            if (allowAcceleration)
                pacmanImageView.setImage(pacmanImage1Nitrous);
            else
                pacmanImageView.setImage(pacmanImage1);
        }

    } // moveUp() end

    public void moveDown() {

        if (!checkRedArea((int) (pacmanImageView.getX() + 13), (int) (pacmanImageView.getY() + 50))
                && !checkRedArea((int) (pacmanImageView.getX() - 1), (int) (pacmanImageView.getY() + 50))
                && !checkRedArea((int) (pacmanImageView.getX() + 26),
                        (int) (pacmanImageView.getY() + 50))
                && moveDownBoolean) { // checks
            // DOWN
            movementPacmanX = 0;
            movementPacmanY = 1;
            turn = 90;
            if (allowAcceleration)
                pacmanImageView.setImage(pacmanImage1Nitrous);
            else
                pacmanImageView.setImage(pacmanImage1);
        }

    } // moveDown() end

    public void keyListener() {

        if (Game.getScene() != null)
            Game.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {

                    keyPressed = ke.getCode().getName().toUpperCase();
                    allowMovement = true;
                    turn = 0;

                    switch (keyPressed) {
                        case "UP":
                        case "W":
                            moveUpBoolean = true;
                            moveDownBoolean = false;
                            moveRightBoolean = false;
                            moveLeftBoolean = false;
                            break;
                        case "RIGHT":
                        case "D":
                            moveRightBoolean = true;
                            moveLeftBoolean = false;
                            moveUpBoolean = false;
                            moveDownBoolean = false;
                            break;
                        case "DOWN":
                        case "S":
                            moveDownBoolean = true;
                            moveUpBoolean = false;
                            moveRightBoolean = false;
                            moveLeftBoolean = false;
                            break;
                        case "LEFT":
                        case "A":
                            moveLeftBoolean = true;
                            moveRightBoolean = false;
                            moveUpBoolean = false;
                            moveDownBoolean = false;
                            break;
                    } // switch case end

                }
            }); // setOnKeyPressed end

        if (GameMultiplayerClient.getScene() != null)
            GameMultiplayerClient.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent ke) {

                    keyPressed = ke.getCode().getName().toUpperCase();
                    allowMovement = true;
                    turn = 0;

                    switch (keyPressed) {
                        case "UP":
                        case "W":
                            moveUpBoolean = true;
                            moveDownBoolean = false;
                            moveRightBoolean = false;
                            moveLeftBoolean = false;
                            break;
                        case "RIGHT":
                        case "D":
                            moveRightBoolean = true;
                            moveLeftBoolean = false;
                            moveUpBoolean = false;
                            moveDownBoolean = false;
                            break;
                        case "DOWN":
                        case "S":
                            moveDownBoolean = true;
                            moveUpBoolean = false;
                            moveRightBoolean = false;
                            moveLeftBoolean = false;
                            break;
                        case "LEFT":
                        case "A":
                            pacmanImageView.setImage(pacmanImage1Flipped);
                            moveLeftBoolean = true;
                            moveRightBoolean = false;
                            moveUpBoolean = false;
                            moveDownBoolean = false;
                            break;
                        case "ENTER":
                        case "T":
                            if (GameMultiplayerClient.getTfChat().isDisabled()) {
                                // if isDisabled true / so you can input text now
                                GameMultiplayerClient.getTfChat().setDisable(false);
                            } else if (!GameMultiplayerClient.getTfChat().isDisabled()) {
                                // if isDisabled true / so you can close input now
                                GameMultiplayerClient.getTfChat().setDisable(true);
                                GameMultiplayerClient.allowToSendMsg();
                            }
                            break;
                    } // switch case end

                }
            }); // setOnKeyPressed end

    } // keyListener end

    public int[] getPixelRGB(int x, int y) {

        PixelReader pixelReader = red_green_background_image.getPixelReader();
        Color color = pixelReader.getColor(x, y);

        return new int[] { (int) (color.getRed() * 256), (int) (color.getGreen() * 256),
                (int) (color.getBlue() * 256) };

    } // pixel reader end

    public boolean checkRedArea(int x, int y) {

        PixelReader pixelReader = red_green_background_image.getPixelReader();
        Color color = pixelReader.getColor(x, y);

        int[] rgbArray = new int[] { (int) (color.getRed() * 256), (int) (color.getGreen() * 256),
                (int) (color.getBlue() * 256) };

        if (rgbArray[0] == 200 && rgbArray[1] == 1 && rgbArray[2] == 1)
            return true;

        return false;

    }

    public ImageView getPacmanImageView() {
        return pacmanImageView;
    }

} // end inner class PacmanRacer *****
