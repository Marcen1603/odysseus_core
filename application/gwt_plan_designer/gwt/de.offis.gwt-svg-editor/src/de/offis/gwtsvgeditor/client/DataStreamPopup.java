package de.offis.gwtsvgeditor.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class DataStreamPopup extends PopupPanel{

    public DataStreamPopup(String[] datastream) {
        super(true, false);

        StringBuilder streamtitle = new StringBuilder();
        streamtitle.append("[");
        int i = 0;
        for (String elem : datastream) {
            streamtitle.append(elem);
            if (i != datastream.length - 1) {
                streamtitle.append(" | ");
            }
            i++;
        }
        streamtitle.append("]");

        setWidget(new Label(streamtitle.toString()));
    }
}