import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GhostHunters extends Pane {

    private Double racePosX = 0.0; // x position of the racer
    private Double racePosY = 0.0; // y position of the racer
    private int raceROT = 0; // x position of the racer
    private String name = null;

    private ImageView ghostImageView = null;

    // Ghost movement attribute
    private Integer moveXGhost = 0;
    private Integer moveYGhost = 0;

    private Random random = new Random();

    private Image red_green_background_image = null;
    private Image red_green_background_image_blocked_spawn = null;

    private final String RED_GREEN_BACKGROUND_PATH = "./media/images/background/red-green-background.png";
    private final String RED_GREEN_BACKGROUND_PATH_BLOCKED_SPAWN = "./media/images/background/red-green-background2.png";

    private boolean lastDirectionLeftBoolean = true;
    private boolean lastDirectionRightBoolean = false;
    private boolean lastDirectionUpBoolean = false;
    private boolean lastDirectionDownBoolean = false;

    private boolean leftSpawn = true;
    private Integer ghostID = -1;

    public GhostHunters(Image imageForGhost, String name, int startingX, int startingY, Integer ghostID) {

        this.name = name;
        this.ghostID = ghostID;
        racePosX = startingX * 1.0;
        racePosY = startingY * 1.0;
        // Draw the icon for the racer
        ghostImageView = new ImageView(imageForGhost);
        ghostImageView.setX(racePosX);
        ghostImageView.setY(racePosY);
        this.getChildren().add(ghostImageView);

        try {
            red_green_background_image = new Image(new FileInputStream(new File(RED_GREEN_BACKGROUND_PATH)));
            red_green_background_image_blocked_spawn = new Image(
                    new FileInputStream(new File(RED_GREEN_BACKGROUND_PATH_BLOCKED_SPAWN)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        goLeft();

        // ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        // TimerTask delayChangeMap = new TimerTask() {

        //     @Override
        //     public void run() {
        //         leftSpawn = true;
        //     }

        // };

        // service.schedule(delayChangeMap, 2100l, TimeUnit.MILLISECONDS);

        // service.shutdown();

    } // GhostHunters public constructor

    public Integer getGhostID(){
        return ghostID;
    }

    public void setX(Double x) {
        ghostImageView.setX(x);
    }

    public Double getX() {
        return racePosX;
    }

    public void setY(Double y) {
        ghostImageView.setY(y);
    }

    public Double getY(){
        return racePosY;
    }

    public ImageView getGhostImageView() {
        return this.ghostImageView;

    }

    public void goRight() {
        moveXGhost = 1;
        moveYGhost = 0;
        lastDirectionUpBoolean = false;
        lastDirectionDownBoolean = false;
        lastDirectionLeftBoolean = false;
        lastDirectionRightBoolean = true;
    }

    public void goLeft() {
        moveXGhost = -1;
        moveYGhost = 0;
        lastDirectionUpBoolean = false;
        lastDirectionDownBoolean = false;
        lastDirectionLeftBoolean = true;
        lastDirectionRightBoolean = false;
    }

    public void goUp() {
        moveXGhost = 0;
        moveYGhost = -1;
        lastDirectionUpBoolean = true;
        lastDirectionDownBoolean = false;
        lastDirectionLeftBoolean = false;
        lastDirectionRightBoolean = false;
    }

    public void goDown() {
        moveXGhost = 0;
        moveYGhost = 1;
        lastDirectionUpBoolean = false;
        lastDirectionDownBoolean = true;
        lastDirectionLeftBoolean = false;
        lastDirectionRightBoolean = false;
    }

    public void update() {

        booleanChecker();

        racePosX += (moveXGhost);
        racePosY += (moveYGhost);

        ghostImageView.setX(racePosX);
        ghostImageView.setY(racePosY);
        ghostImageView.setRotate(raceROT);

    } // end update()

    public void movementRandomiser() {

        if ((checkYellowArea((int) ghostImageView.getX(), (int) ghostImageView.getY())
                && checkYellowArea((int) ghostImageView.getX() + 25, (int) ghostImageView.getY() + 25))) {
            goToRandomDirection();
        }

    }

    public void booleanChecker() {

        if (moveXGhost == 0 && moveYGhost == -1) {
            // UP
            if (!checkRedArea((int) (ghostImageView.getX()), (int) (ghostImageView.getY() - 10))
                    && !checkRedArea((int) (ghostImageView.getX() + 25), (int) (ghostImageView.getY() - 10))) { // checks
                // UP
                goUp();

            } else
                goToRandomDirection();

        } else if (moveXGhost == 1 && moveYGhost == 0) {
            // RIGHT
            if (!checkRedArea((int) (ghostImageView.getX() + 35), (int) (ghostImageView.getY()))
                    && !checkRedArea((int) (ghostImageView.getX() + 35), (int) (ghostImageView.getY() + 25))) { // checks
                // RIGHT
                goRight();

            } else
                goToRandomDirection();

        } else if (moveXGhost == 0 && moveYGhost == 1) {
            // DOWN
            if (!checkRedArea((int) (ghostImageView.getX()), (int) (ghostImageView.getY() + 35))
                    && !checkRedArea((int) (ghostImageView.getX() + 25), (int) (ghostImageView.getY() + 35))) { // checks
                // DOWN
                goDown();

            } else
                goToRandomDirection();

        } else if (moveXGhost == -1 && moveYGhost == 0) {
            // LEFT
            if (!checkRedArea((int) (ghostImageView.getX() - 10), (int) (ghostImageView.getY()))
                    && !checkRedArea((int) (ghostImageView.getX() - 10), (int) (ghostImageView.getY() + 25))) { // checks
                                                                                                                // LEFT
                goLeft();
            } else
                goToRandomDirection();
        }

    } // booleanChecker end

    public void goToRandomDirection() {

        boolean check = true;

        while (check) {

            switch (random.nextInt(4)) {
                case 0:
                    if (!checkRedArea((int) (ghostImageView.getX() - 10), (int) (ghostImageView.getY()))
                            && !checkRedArea((int) (ghostImageView.getX() - 10), (int) (ghostImageView.getY() + 25))
                            && !lastDirectionRightBoolean) { // checks
                        // LEFT
                        goLeft();
                        check = false;
                    }
                    break;
                case 1:
                    if (!checkRedArea((int) (ghostImageView.getX() + 35), (int) (ghostImageView.getY()))
                            && !checkRedArea((int) (ghostImageView.getX() + 35), (int) (ghostImageView.getY() + 25))
                            && !lastDirectionLeftBoolean) { // checks
                        // RIGHT
                        goRight();
                        check = false;

                    }
                    break;
                case 2:
                    if (!checkRedArea((int) (ghostImageView.getX()), (int) (ghostImageView.getY() - 10))
                            && !checkRedArea((int) (ghostImageView.getX() + 25), (int) (ghostImageView.getY() - 10))
                            && !lastDirectionDownBoolean) { // checks
                        // UP
                        goUp();
                        check = false;
                    }
                    break;
                case 3:
                    if (!checkRedArea((int) (ghostImageView.getX()), (int) (ghostImageView.getY() + 35))
                            && !checkRedArea((int) (ghostImageView.getX() + 25), (int) (ghostImageView.getY() + 35))
                            && !lastDirectionUpBoolean) { // checks
                        // DOWN
                        goDown();
                        check = false;
                    }
                    break;
            } // switch case end

        } // while end

    } // goToRandomDirection() end

    public String getName() {
        return name;
    }

    public int[] getPixelRGB(int x, int y) {

        PixelReader pixelReader = red_green_background_image.getPixelReader();
        Color color = pixelReader.getColor(x, y);

        return new int[] { (int) (color.getRed() * 256), (int) (color.getGreen() * 256),
                (int) (color.getBlue() * 256) };

    } // pixel reader end

    public boolean checkRedArea(int x, int y) {

        PixelReader pixelReader = null;

        if (!leftSpawn)
            pixelReader = red_green_background_image.getPixelReader();

        else
            pixelReader = red_green_background_image_blocked_spawn.getPixelReader();

        Color color = pixelReader.getColor(x, y);
        int[] rgbArray = new int[] { (int) (color.getRed() * 256), (int) (color.getGreen() * 256),
                (int) (color.getBlue() * 256) };

        if (rgbArray[0] == 200 && rgbArray[1] == 1 && rgbArray[2] == 1)
            return true;

        return false;

    }

    public boolean checkYellowArea(int x, int y) {

        PixelReader pixelReader = red_green_background_image.getPixelReader();
        Color color = pixelReader.getColor(x, y);
        int[] rgbArray = new int[] { (int) (color.getRed() * 256), (int) (color.getGreen() * 256),
                (int) (color.getBlue() * 256) };

        if (rgbArray[0] == 244 && rgbArray[1] == 221 && rgbArray[2] == 20)
            return true;

        return false;

    }

    public boolean checkPurpleArea(int x, int y) {

        PixelReader pixelReader = red_green_background_image.getPixelReader();
        Color color = pixelReader.getColor(x, y);
        int[] rgbArray = new int[] { (int) (color.getRed() * 256), (int) (color.getGreen() * 256),
                (int) (color.getBlue() * 256) };

        if (rgbArray[0] == 71 && rgbArray[1] == 1 && rgbArray[2] == 230)
            return true;

        return false;

    }

} // Ghost Hunters end inner class *****
