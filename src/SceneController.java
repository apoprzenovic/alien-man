
/**
 * 
@ASSESSME.INTENSITY:LOW
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SceneController {

    // Attributes other classes can take
    private final URL INTROSCENE_URL = getClass().getResource("IntroScene.fxml");
    private final URL SETTINGS_URL = getClass().getResource("Settings.fxml");
    private final URL HOWTOPLAY_URL = getClass().getResource("HowToPlay.fxml");
    private final URL DEATHSCREEN_URL = getClass().getResource("DeathScreen.fxml");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Slider sliderVolume;

    @FXML
    void initialize(Stage stageX) {
        stageX.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.out.println("Exit was pressed");
                System.exit(0);
            }

        });

    } // initialize end

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToGame(ActionEvent event) {

        System.out.println("Switch to game was pressed");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        Game pacmanGame = new Game();
        pacmanGame.start(new Stage());

    }

    public void switchToIntro(ActionEvent event) {

        System.out.println("Switch To Intro");
        FXMLLoader fxmlLoader = new FXMLLoader(INTROSCENE_URL);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Game.getStage() != null)
            Game.getStage().close();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SceneController sceneController = fxmlLoader.getController();

    }

    public void switchToSettings(ActionEvent event) {

        System.out.println("Switch To Settings 1");

        FXMLLoader fxmlLoader = new FXMLLoader(SETTINGS_URL);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scene = new Scene(root);
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();

        SceneController sceneController = fxmlLoader.getController();
        sceneController.sliderVolume.setValue(IntroScene.getVolumeValue());

    } // end of switchToSettings

    public void switchToHowToPlay(ActionEvent event) {

        System.out.println("Switch To How To Play");

        FXMLLoader fxmlLoader = new FXMLLoader(HOWTOPLAY_URL);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scene = new Scene(root);
        stage.setTitle("How To Play");
        stage.setScene(scene);
        stage.show();

        SceneController sceneController = fxmlLoader.getController();

    }

    public void switchToMultiplayerGame(ActionEvent event) {

        System.out.println("Switch to multiplayer game was pressed");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        GameMultiplayerServer gServer = new GameMultiplayerServer();
        gServer.start(new Stage());

        GameMultiplayerClient pClient1 = new GameMultiplayerClient();
        pClient1.start(new Stage());

    }

    public void restartGame(ActionEvent event) {

        System.out.println("Switch to game was pressed");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        if (Game.getStage() != null)
            Game.getStage().close();
        Game pacmanGame = new Game();
        pacmanGame.start(new Stage());

    }

    public void closeGame(ActionEvent event) {
        System.exit(0);
    }

    public void onVolumeSliderChange() {

        IntroScene.setVolumeValue((int) Math.round(sliderVolume.getValue()));
        IntroScene.setMusicVolume(IntroScene.getVolumeValue());

    }

} // intro scene end
