import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class Coin extends Pane {

    private ImageView coinImageView = null;

    public Coin(Image image) {

        coinImageView = new ImageView(image);
        coinImageView.setFitWidth(20);
        coinImageView.setFitHeight(20);
        this.getChildren().add(coinImageView);

    } // constructor end

    public ImageView getCoinImageView(){
        return coinImageView;
    }

    public void setX(Integer x){
        coinImageView.setX(x);
    }

    public void setY(Integer y){
        coinImageView.setY(y);
    }

    public void clearImageView(){
        this.getChildren().clear();
        this.getChildren().remove(coinImageView);
        coinImageView.setImage(null);
    }

}