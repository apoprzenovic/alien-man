/**
 * 
@ASSESSME.INTENSITY:LOW
 */
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IntroScene extends Application {
    
    public static void main(String[] args) {
        launch(args);
    } // main end

    private static Stage stage;

    private static Integer volumeValue = 0; // volume

    // Music
    private AudioInputStream audioInputStream = null;
    private static Clip clip = null;
    private String musicFilePath = "./media/music/MainMenuMusic.wav";

    private SceneController sceneController = null;

    @Override
    public void start(Stage primaryStage) throws Exception {

        IntroScene.stage = primaryStage;

        try {

            sceneController = new SceneController();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("IntroScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Main Menu");
            primaryStage.setScene(scene);
            primaryStage.show();

            sceneController = fxmlLoader.getController();
            sceneController.initialize(primaryStage);

            audioInputStream = AudioSystem.getAudioInputStream(new File(musicFilePath).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            setMusicVolume(IntroScene.getVolumeValue());

        } catch (Exception e) {
            e.printStackTrace();
        }

    } // staart end

    public static void setMusicVolume(Integer value) {
        Float volume = (value.floatValue()) / 100;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public static void stopMainMenuMusic() {
        clip.stop();
        clip.close();
    }

    public static Clip getClip() {
        return clip;
    }

    public static void setVolumeValue(Integer volume) {
        volumeValue = volume;
    }

    public static Integer getVolumeValue() {
        return volumeValue;
    }

    public static Stage getStage() {
        return stage;
    }

} // IntroScene end
