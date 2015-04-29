package cm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        InputStream inputStream = getClass().getResource("bundles/Language_en.properties").openStream();
        ResourceBundle bundle = new PropertyResourceBundle(inputStream);

        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"), bundle);
        primaryStage.setTitle("Condition Monitoring");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
