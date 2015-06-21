package cm.controller.listeners;

import cm.controller.MainController;
import cm.model.Collection;
import javafx.collections.ListChangeListener;

/**
 * Created by Tobi on 03.04.2015.
 */
public class CollectionListViewListener implements ListChangeListener<Collection> {

    MainController mainController;

    public CollectionListViewListener(MainController controller) {
        mainController = controller;
    }

    @Override
    public void onChanged(Change<? extends Collection> c) {
        // Here I have to update the right side
        mainController.updateCollectionView(c.getList().get(0));
    }
}
