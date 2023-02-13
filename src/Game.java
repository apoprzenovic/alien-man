
/**
 * 
@ASSESSME.INTENSITY:LOW
 */
import javafx.application.*;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.animation.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * PackmanGEOStarter with JavaFX and Thread
 */

public class Game extends Application {

   private final static String ICON_IMAGE = "./media/images/icons/packman.gif"; // file with icon for a racer
   private final static String ICON_IMAGE2 = "./media/images/icons/packmanFlipped.gif"; // file with icon for a

   private final static String ICON_IMAGE_NITROUS = "./media/images/icons/packmanNitrous.gif"; // file with icon for a
   private final static String ICON_IMAGE_NITROUS2 = "./media/images/icons/packmanNitrousFlipped.gif"; // file wit
   // Window attributes
   private static Stage stage;
   private static Scene scene;
   private AnchorPane root;

   private static String[] args;

   private PacmanPlayer pacman = null; // array of racers

   private GhostHunters ghostHunters = null;
   private GhostHunters ghostHunters2 = null;
   private GhostHunters ghostHunters3 = null;
   private GhostHunters ghostHunters4 = null;

   private AnimationTimer timer; // timer to control animation
   private Timer timer3;

   private List<GhostHunters> arrayOfGhosts = null;

   private final static String GHOST_TEST1 = "./media/images/icons/alien1.gif"; // file with icon for a racer
   private final static String GHOST_TEST2 = "./media/images/icons/alien2.gif"; // file with icon for a racer
   private final static String GHOST_TEST3 = "./media/images/icons/alien3.gif"; // file with icon for a racer
   private final static String GHOST_TEST4 = "./media/images/icons/alien4.gif"; // file with icon for a racer
   private final String RED_GREEN_BACKGROUND_PATH = "./media/images/background/red-green-background.png";
   private final String HEART_PATH = "./media/images/sprites/heart.png";
   private final String NITROUS_PATH = "./media/images/sprites/nos.png";
   private final String COIN_PATH = "./media/images/sprites/gold.png";

   private Image[] coinImageArray = new Image[15];
   private Coin[] coinsArray = new Coin[15];
   private ImageView[] heartImageArray = new ImageView[3];
   private ImageView[] nitrousImageArray = new ImageView[2];
   private Integer coinCounter = 0;
   private Integer powerCounter = 0;

   private boolean allowWin = true;
   // Music
   private AudioInputStream audioInputStream = null;
   private Clip clipMusic = null;
   private String musicFilePath = "./media/music/PacmanMusic.wav";

   private Image ghostImage1 = null;
   private Image ghostImage2 = null;
   private Image ghostImage3 = null;
   private Image ghostImage4 = null;

   private Image red_green_background_image = null;

   private Label lblCoins = new Label("\t\tCoins:");
   private TextField tfCoins = new TextField();
   private Label lblPowers = new Label("\t\tPowers Taken:");
   private TextField tfPowers = new TextField();
   private Label lblLives = new Label("\tLives:");
   private TextField tfLives = new TextField();

   private final URL DEATHSCREEN_URL = getClass().getResource("DeathScreen.fxml");
   private final URL WINSCREEN_URL = getClass().getResource("WinScreen.fxml");

   private Integer livesCounter = 3;

   // main program
   public static void main(String[] _args) {
      args = _args;
      launch(args);
   }

   // start() method, called via launch
   public void start(Stage _stage) {
      // stage seteup
      stage = _stage;
      stage.setTitle("Pacman Game");
      stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  System.exit(0);
               }
            });

      // root pane
      root = new AnchorPane();

      initializeScene();

   }

   // start the race
   public void initializeScene() {
      System.out.println("Damn thats sick");
      try {
         ghostImage1 = new Image(new FileInputStream(GHOST_TEST1));
         ghostImage2 = new Image(new FileInputStream(GHOST_TEST2));
         ghostImage3 = new Image(new FileInputStream(GHOST_TEST3));
         ghostImage4 = new Image(new FileInputStream(GHOST_TEST4));
         red_green_background_image = new Image(new FileInputStream(RED_GREEN_BACKGROUND_PATH));
         for (int i = 0; i < coinImageArray.length; i++) {
            coinImageArray[i] = new Image(new FileInputStream(COIN_PATH));
         }
         for (int i = 0; i < heartImageArray.length; i++) {
            heartImageArray[i] = new ImageView(new Image(new FileInputStream(HEART_PATH)));
            heartImageArray[i].setY(28);
            heartImageArray[i].setX(78 + (i + 1) * 25);
            root.getChildren().add(heartImageArray[i]);
         }
         for (int i = 0; i < nitrousImageArray.length; i++) {
            nitrousImageArray[i] = new ImageView(new Image(new FileInputStream(NITROUS_PATH)));
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }

      pacman = new PacmanPlayer(0);
      ghostHunters = new GhostHunters(ghostImage1, "Teal Ghost", 102, 590, 1);
      ghostHunters2 = new GhostHunters(ghostImage2, "Pink Ghost", 102, 590, 2);
      ghostHunters3 = new GhostHunters(ghostImage3, "Blue Ghost", 980, 293, 2);
      ghostHunters4 = new GhostHunters(ghostImage4, "Brown Ghost", 980, 293, 3);

      arrayOfGhosts = new ArrayList<>();
      arrayOfGhosts.add(ghostHunters);
      arrayOfGhosts.add(ghostHunters2);
      arrayOfGhosts.add(ghostHunters3);
      arrayOfGhosts.add(ghostHunters4);

      for (int i = 0; i < coinsArray.length; i++) {
         coinsArray[i] = new Coin(coinImageArray[i]);
      }

      // coinCounter

      placeCoins();

      FlowPane scoreBoardFP = new FlowPane(8, 20);
      scoreBoardFP.setAlignment(Pos.CENTER_LEFT);
      scoreBoardFP.setPrefWidth(1150);
      scoreBoardFP.setPrefHeight(80);
      scoreBoardFP.setStyle("-fx-background-color: rgba(50, 100, 100, 0.3);");
      tfLives.setPrefWidth(100);
      tfLives.setPrefHeight(40);
      tfLives.setEditable(false);
      tfLives.setDisable(true);
      tfLives.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");
      tfCoins.setPrefWidth(100);
      tfCoins.setPrefHeight(40);
      tfCoins.setAlignment(Pos.CENTER);
      tfCoins.setStyle(
            "-fx-font: 24 calibri; -fx-cursor: cursor; -fx-text-fill: black; -fx-background-color: rgba(255, 223, 0, 1);");
      tfCoins.setEditable(false);
      tfCoins.setDisable(true);
      tfPowers.setPrefWidth(100);
      tfPowers.setPrefHeight(40);
      tfPowers.setAlignment(Pos.CENTER);
      tfPowers.setStyle(
            "-fx-font: 24 calibri; -fx-cursor: cursor; -fx-text-fill: black; -fx-background-color: rgba(131, 238, 255, 1);");
      tfPowers.setEditable(false);
      tfPowers.setDisable(true);
      lblLives.setTextFill(Color.WHITE);
      lblLives.setStyle("-fx-font: 20 calibri; -fx-font-weight: 700; -fx-cursor: cursor;");
      lblCoins.setTextFill(Color.WHITE);
      lblCoins.setStyle("-fx-font: 20 calibri; -fx-font-weight: 700; -fx-cursor: cursor;");
      lblPowers.setTextFill(Color.WHITE);
      lblPowers.setStyle("-fx-font: 20 calibri; -fx-font-weight: 700; -fx-cursor: cursor;");
      scoreBoardFP.getChildren().addAll(lblLives, tfLives, lblCoins, tfCoins, lblPowers, tfPowers);

      root.getChildren().addAll(pacman, ghostHunters, ghostHunters2, ghostHunters3, ghostHunters4, scoreBoardFP);
      root.setId("pane");

      tfCoins.setText("00/30");
      tfPowers.setText("00/02");
      // display the window
      scene = new Scene(root, 1150, 750);
      scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setScene(scene);
      stage.show();

      System.out.println("Starting race...");

      try {

         if (IntroScene.getClip() != null)
            IntroScene.stopMainMenuMusic();

         audioInputStream = AudioSystem.getAudioInputStream(new File(musicFilePath).getAbsoluteFile());

         clipMusic = AudioSystem.getClip();
         clipMusic.open(audioInputStream);
         clipMusic.loop(Clip.LOOP_CONTINUOUSLY);
         setMusicVolume(IntroScene.getVolumeValue());

      } catch (UnsupportedAudioFileException uafe) {
         uafe.printStackTrace();
      } catch (IOException ioe) {
         ioe.printStackTrace();
      } catch (LineUnavailableException lue) {
         lue.printStackTrace();
      }

      // ********* MUSIC

      timer = new AnimationTimer() {

         @Override
         public void handle(long now) {

            pacman.update();

            for (GhostHunters ghostHuntersInstantiated : arrayOfGhosts) {
               ghostHuntersInstantiated.update();
               synchronized (pacman) {
                  collidingGhostWithPacman(ghostHuntersInstantiated);
               } // sync end
            }

            for (int i = 0; i < coinsArray.length; i++) {
               collidingCoinsWithPacman(coinsArray[i]);
            }

            for (int j = 0; j < nitrousImageArray.length; j++) {
               collidingPacmanWithNitrous(nitrousImageArray[j]);
            }

         }

      };

      // TimerTask to delay start of race for 2 seconds

      TimerTask taskRandomGhost = new TimerTask() {
         public void run() {
            timer.start();
         }
      };
      Timer startTimer = new Timer();
      long delay2 = 1500L;
      startTimer.schedule(taskRandomGhost, delay2); // update ghost

      Executors.newSingleThreadExecutor().execute(new Runnable() {
         @Override
         public void run() { // executor public void run

            timer3 = new Timer();
            TimerTask tt = new TimerTask() {

               @Override
               public void run() {
                  synchronized (timer) {

                     for (GhostHunters ghostHuntersInstantiated : arrayOfGhosts) {
                        ghostHuntersInstantiated.movementRandomiser();
                     }

                  } // synchronized end
               }
            };
            timer3.scheduleAtFixedRate(tt, 1500l, 50l); // movement for ghosts
         }
      }); // running on seperate thread

   } // method initializeScene end

   public void placeCoins() {

      root.getChildren().add(coinsArray[0]);
      coinsArray[0].setX(80);
      coinsArray[0].setY(135);

      root.getChildren().add(coinsArray[1]);
      coinsArray[1].setX(440);
      coinsArray[1].setY(135);

      root.getChildren().add(coinsArray[2]);
      coinsArray[2].setX(175);
      coinsArray[2].setY(225);

      root.getChildren().add(coinsArray[3]);
      coinsArray[3].setX(315);
      coinsArray[3].setY(405);

      root.getChildren().add(coinsArray[4]);
      coinsArray[4].setX(105);
      coinsArray[4].setY(510);

      root.getChildren().add(coinsArray[5]);
      coinsArray[5].setX(33);
      coinsArray[5].setY(683);

      root.getChildren().add(coinsArray[6]);
      coinsArray[6].setX(640);
      coinsArray[6].setY(683);

      root.getChildren().add(coinsArray[7]);
      coinsArray[7].setX(1092);
      coinsArray[7].setY(600);

      root.getChildren().add(coinsArray[8]);
      coinsArray[8].setX(860);
      coinsArray[8].setY(430);

      root.getChildren().add(coinsArray[9]);
      coinsArray[9].setX(375);
      coinsArray[9].setY(593);

      root.getChildren().add(coinsArray[10]);
      coinsArray[10].setX(640);
      coinsArray[10].setY(473);

      root.getChildren().add(coinsArray[11]);
      coinsArray[11].setX(640);
      coinsArray[11].setY(593);

      root.getChildren().add(coinsArray[12]);
      coinsArray[12].setX(867);
      coinsArray[12].setY(300);

      root.getChildren().add(coinsArray[13]);
      coinsArray[13].setX(635);
      coinsArray[13].setY(184);

      root.getChildren().add(coinsArray[14]);
      coinsArray[14].setX(985);
      coinsArray[14].setY(404);

   } // placeCoins end

   public void replaceAllCoins() {
      for (Coin coin : coinsArray) {
         coin.getCoinImageView().setVisible(true);
      }
   }

   public void collidingCoinsWithPacman(Coin coinForCollision) {

      ImageView imageView = coinForCollision.getCoinImageView();
      // sqrt()
      double x2x1 = Math.pow(pacman.getPacmanImageView().getX() - imageView.getX(), 2);
      double y2y1 = Math.pow(pacman.getPacmanImageView().getY() - imageView.getY(), 2);

      double distance = Math.sqrt(x2x1 + y2y1);

      if (distance < 25 && imageView.isVisible()) {
         imageView.setVisible(false);
         tfCoins.setText(String.format("%02d/30", ++coinCounter));
      }

      if (coinCounter == 15 && !root.getChildren().contains(nitrousImageArray[0])) {

         Platform.runLater(new Runnable() {

            @Override
            public void run() {
               synchronized (timer) {
                  if (!root.getChildren().contains(nitrousImageArray[0])) {
                     root.getChildren().addAll(nitrousImageArray[0]);
                     nitrousImageArray[0].setX(63);
                     nitrousImageArray[0].setY(593);
                  }
                  if (!root.getChildren().contains(nitrousImageArray[1])) {
                     root.getChildren().addAll(nitrousImageArray[1]);
                     nitrousImageArray[1].setX(980);
                     nitrousImageArray[1].setY(190);
                  }
               } // run end
            } // new Runnable end

         }); // Platform.runLater
         replaceAllCoins();

      }

      else if (coinCounter == 30 && allowWin) {

            synchronized (timer) {
               timer.stop();
               timer3.cancel();
               allowWin = false;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(WINSCREEN_URL);

            Stage newStage = new Stage();
            try {
               root = fxmlLoader.load();
            } catch (IOException e) {
               e.printStackTrace();
            }

            scene = new Scene(root);
            newStage.setTitle("Winner");
            Platform.runLater(new Runnable() {
               @Override
               public void run() {
                  newStage.setScene(scene);
                  newStage.show();
      
               }
            });

            newStage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  System.exit(0);
               }
            });

            SceneController sceneController = fxmlLoader.getController();
      }

   } // collidesWithPacman end

   public void collidingPacmanWithNitrous(ImageView imageView) {

      // sqrt()
      double x2x1 = Math.pow(pacman.getPacmanImageView().getX() - imageView.getX(), 2);
      double y2y1 = Math.pow(pacman.getPacmanImageView().getY() - imageView.getY(), 2);

      double distance = Math.sqrt(x2x1 + y2y1);

      if (distance < 25 && imageView.isVisible()) {
         imageView.setVisible(false);
         tfPowers.setText(String.format("%02d/02", ++powerCounter));
         pacman.hitNitrous();

         ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
         TimerTask delayChangeMap = new TimerTask() {
            @Override
            public void run() {
               pacman.ranOutOfNitrous();
            }
         };
         service.schedule(delayChangeMap, 5000l, TimeUnit.MILLISECONDS);
         service.shutdown();
         //stop nitrous
      }

   } // collidesWithPacman end

   public void collidingGhostWithPacman(GhostHunters ghostHunterForCollision) {

      ImageView imageView = ghostHunterForCollision.getGhostImageView();
      // sqrt()
      double x2x1 = Math.pow(pacman.getPacmanImageView().getX() - imageView.getX(), 2);
      double y2y1 = Math.pow(pacman.getPacmanImageView().getY() - imageView.getY(), 2);
      //
      double distance = Math.sqrt(x2x1 + y2y1);

      if (distance < 27) {

         livesCounter--;

         if (livesCounter != 0) {
            Platform.runLater(new Runnable() {

               @Override
               public void run() {

                  tfLives.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");

                  pacman.restartPosition();

                  System.out.println(livesCounter + " lives");
                  heartImageArray[livesCounter].setVisible(false);

                  ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                  TimerTask delayChangeMap = new TimerTask() {
                     @Override
                     public void run() {
                        tfLives.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);");
                     }
                  };
                  service.schedule(delayChangeMap, 200l, TimeUnit.MILLISECONDS);
                  service.shutdown();

               }

            });

         } // inner if

         else if (livesCounter == 0) {

            heartImageArray[livesCounter].setVisible(false);

            System.out.println("Collision between pacman and " + ghostHunterForCollision.getName());
            clipMusic.stop();
            clipMusic.close();

            synchronized (timer) {
               timer.stop();
               timer3.cancel();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(DEATHSCREEN_URL);

            Stage newStage = new Stage();
            try {
               root = fxmlLoader.load();
            } catch (IOException e) {
               e.printStackTrace();
            }

            scene = new Scene(root);
            newStage.setTitle("Sucker");
            newStage.setScene(scene);
            newStage.show();

            newStage.setOnCloseRequest(
               new EventHandler<WindowEvent>() {
                  public void handle(WindowEvent evt) {
                     System.exit(0);
                  }
               });

            SceneController sceneController = fxmlLoader.getController();

         } // else if

      } // if end

   } // collidesWithPacman end

   public void setMusicVolume(Integer value) {
      Float volume = (value.floatValue()) / 100;
      FloatControl gainControl = (FloatControl) clipMusic.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(20f * (float) Math.log10(volume));
   }

   public static Scene getScene() {
      return scene;
   }

   public static Stage getStage() {
      return stage;
   }

   public Boolean checkGreenArea(int x, int y) {

      PixelReader pixelReader = red_green_background_image.getPixelReader();

      Color color = pixelReader.getColor(x, y);

      int[] rgbArray = new int[] { (int) (color.getRed() * 256), (int) (color.getGreen() * 256),
            (int) (color.getBlue() * 256) };

      if (rgbArray[0] == 100 && rgbArray[1] == 200 && rgbArray[2] == 80)
         return true;

      return false;

   }

} // end class PacmanGame