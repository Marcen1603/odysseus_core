package de.offis.gui.client.widgets;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Shows a message to the user.
 *
 * @author Alexander Funk
 * 
 */
public class DebugWindow extends DialogBox {

    public DebugWindow(String message) {
        TextArea area = new TextArea();
        area.setSize("1000px", "500px");
        area.setText(message);
        area.setEnabled(true);

        setTitle("Debug Window");
        setText("Debug Window");
        setWidget(area);
        setGlassEnabled(true);
        setGlassStyleName("abstractconfigurationdialog-glass");
        setAutoHideEnabled(true);
    }
}
