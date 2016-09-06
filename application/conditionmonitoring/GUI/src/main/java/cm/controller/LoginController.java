package cm.controller;

import cm.communication.CommunicationService;
import cm.communication.rest.RestException;
import cm.communication.rest.RestService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.restlet.data.Product;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

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
    @FXML
    ProgressIndicator loginProgress;
    @FXML
    Button loginButton;

    public void login(ActionEvent actionEvent) {

        // Show that we are doing something
        loginProgress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        loginProgress.setVisible(true);


        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // What i actually want to do
                                    CommunicationService.setLoginIp(loginIp.getText());

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
                                    loginProgress.setVisible(false);
                                    animateWrongLogin();
                                } catch (IOException e) {
                                    // Loading the next view was not successful
                                    loginProgress.setVisible(false);
                                    e.printStackTrace();
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    private void animateWrongLogin() {
        // Left
        final Timeline leftTimeline1 = new Timeline();
        final KeyValue kv1 = new KeyValue(loginButton.translateXProperty(), -25);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(120), kv1);
        leftTimeline1.getKeyFrames().add(kf1);

        // Right
        final Timeline rightTimeline1 = new Timeline();
        final KeyValue kv2 = new KeyValue(loginButton.translateXProperty(), 35);
        final KeyFrame kf2 = new KeyFrame(Duration.millis(240), kv2);
        rightTimeline1.getKeyFrames().add(kf2);

        // Left
        final Timeline leftTimeline2 = new Timeline();
        final KeyValue kv3 = new KeyValue(loginButton.translateXProperty(), -35);
        final KeyFrame kf3 = new KeyFrame(Duration.millis(240), kv3);
        leftTimeline2.getKeyFrames().add(kf3);

        // Right
        final Timeline rightTimeline2 = new Timeline();
        final KeyValue kv4 = new KeyValue(loginButton.translateXProperty(), 5);
        final KeyFrame kf4 = new KeyFrame(Duration.millis(120), kv4);
        rightTimeline2.getKeyFrames().add(kf4);

        SequentialTransition sequence = new SequentialTransition(leftTimeline1, rightTimeline1, leftTimeline2, rightTimeline2);
        sequence.play();
    }
}
