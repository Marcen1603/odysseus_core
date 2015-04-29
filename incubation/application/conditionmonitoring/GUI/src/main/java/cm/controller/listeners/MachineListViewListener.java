package cm.controller.listeners;

import cm.controller.MainController;
import cm.model.Machine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by Tobi on 03.04.2015.
 */
public class MachineListViewListener implements ChangeListener<Machine> {

    MainController mainController;

    public MachineListViewListener(MainController controller) {
        mainController = controller;
    }

    @Override
    public void changed(ObservableValue<? extends Machine> observable, Machine oldValue, Machine newValue) {
        // Here I have to update the right side
        mainController.updateMachineView(newValue);
    }
}
