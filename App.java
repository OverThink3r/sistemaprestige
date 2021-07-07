import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage prymaryStage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/vistas/vistalogin.fxml"));
            Scene scene = new Scene(root);
            prymaryStage.setScene(scene);
            prymaryStage.setTitle("Prestige English School");
            prymaryStage.setResizable(false);
            prymaryStage.setFullScreen(false);

            Image img = new Image("/assets/logo_login.png");
            prymaryStage.getIcons().add(img);
            prymaryStage.show();
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            prymaryStage.setX((bounds.getWidth() - prymaryStage.getWidth()) / 2);
            prymaryStage.setY((bounds.getHeight() - prymaryStage.getHeight()) / 4);
        }catch(Exception e){
            e.printStackTrace();
        }   
    }
    
}