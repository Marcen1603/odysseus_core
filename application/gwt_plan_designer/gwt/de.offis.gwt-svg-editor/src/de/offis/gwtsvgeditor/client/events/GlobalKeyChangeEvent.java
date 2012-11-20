package de.offis.gwtsvgeditor.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event to be fired over EventBus. Wraps the KeyCode which was pressed.
 * 
 * @author Alexander Funk
 *
 */
public class GlobalKeyChangeEvent extends GwtEvent<GlobalKeyChangeEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onGlobalKeyChange(GlobalKeyChangeEvent e);
    }

    @Override
    protected void dispatch(GlobalKeyChangeEvent.Handler handler) {
        handler.onGlobalKeyChange(this);
    }

    @Override
    public GwtEvent.Type<GlobalKeyChangeEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<GlobalKeyChangeEvent.Handler> TYPE = new GwtEvent.Type<GlobalKeyChangeEvent.Handler>();

    private boolean value;
    private int keycode;

    public GlobalKeyChangeEvent(int keycode, boolean value) {
        this.value = value;
        this.keycode = keycode;
    }

    public int getKeyCode() {
        return keycode;
    }

    public boolean getValue() {
        return this.value;
    }
}
