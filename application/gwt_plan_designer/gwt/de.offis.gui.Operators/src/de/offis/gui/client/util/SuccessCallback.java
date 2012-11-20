package de.offis.gui.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.offis.gui.client.widgets.DebugWindow;

/**
 *  Standard callback that handles the onFailure method himself (by showing or logging the error message).
 *
 * @author Alexander Funk
 */
public abstract class SuccessCallback<T> implements AsyncCallback<T>{

    private DebugWindow window;

    public void onFailure(Throwable caught) {
        window = new DebugWindow(caught.getMessage());
        window.center();
    }

    public abstract void onSuccess(T result);
}
