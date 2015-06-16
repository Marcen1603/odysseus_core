package cm.controller.listeners;

import cm.controller.MainController;
import cm.model.Collection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;

/**
 * Created by Tobi on 03.04.2015.
 */
public class MachineListViewListener implements ListChangeListener<Collection> {

    MainController mainController;

    public MachineListViewListener(MainController controller) {
        mainController = controller;
    }

    @Override
    public void onChanged(Change<? extends Collection> c) {
        // Here I have to update the right side
        mainController.updateMachineView(c.getList().get(0));
    }
}
