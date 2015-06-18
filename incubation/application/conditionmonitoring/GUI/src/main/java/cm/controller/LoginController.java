package cm.controller;

import cm.communication.CommunicationService;
import cm.communication.rest.RestException;
import cm.communication.rest.RestService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Tobias
 * @since 18.06.2015.
 */
public class LoginController {

    @FXML
    Parent root;
    @FXML
    TextField loginIp;
    @FXML
    TextField loginUserName;
    @FXML
    PasswordField loginPassword;

    public void login(ActionEvent actionEvent) {
        try {
            String token = RestService.login(loginIp.getText(), loginUserName.getText(), loginPassword.getText());
            CommunicationService.setToken(token);

            // Now we can show the next view with the configurations
            Stage stage = (Stage) root.getScene().getWindow();
            InputStream inputStream = getClass().getResource("../bundles/Language_en.properties").openStream();
            ResourceBundle bundle = new PropertyResourceBundle(inputStream);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/configChooser.fxml"), bundle);
            Parent configChooser = loader.load();
            stage.setTitle("Choose Configuration - Condition Monitoring");
            stage.setScene(new Scene(configChooser, 600, 500));
        } catch (RestException e) {
            // Login was not successful, stay on this view
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
