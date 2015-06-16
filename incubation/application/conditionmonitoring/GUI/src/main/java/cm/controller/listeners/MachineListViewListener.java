package cm.controller.listeners;

import cm.controller.MainController;
import cm.model.Collection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by Tobi on 03.04.2015.
 */
public class MachineListViewListener implements ChangeListener<Collection> {

    MainController mainController;

    public MachineListViewListener(MainController controller) {
        mainController = controller;
    }

    @Override
    public void changed(ObservableValue<? extends Collection> observable, Collection oldValue, Collection newValue) {
        // Here I have to update the right side
        mainController.updateMachineView(newValue);
    }
}
