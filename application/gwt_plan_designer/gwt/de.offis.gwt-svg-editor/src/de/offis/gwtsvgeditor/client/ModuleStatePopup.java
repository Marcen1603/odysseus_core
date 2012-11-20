package de.offis.gwtsvgeditor.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class ModuleStatePopup extends PopupPanel {

    public ModuleStatePopup(String message) {
        super(true, false);

        setWidget(new Label(message));
    }
}