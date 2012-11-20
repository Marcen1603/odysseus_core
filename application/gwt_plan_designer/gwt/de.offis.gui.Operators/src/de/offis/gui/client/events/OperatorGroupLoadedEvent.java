package de.offis.gui.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event  is fired whenever a operator group was loaded.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorGroupLoadedEvent extends GwtEvent<OperatorGroupLoadedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onOperatorGroupLoaded(OperatorGroupLoadedEvent e);
    }

    @Override
    protected void dispatch(OperatorGroupLoadedEvent.Handler handler) {
        handler.onOperatorGroupLoaded(this);
    }

    @Override
    public GwtEvent.Type<OperatorGroupLoadedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<OperatorGroupLoadedEvent.Handler> TYPE = new GwtEvent.Type<OperatorGroupLoadedEvent.Handler>();

    /**
     * Custom data held within this event object.
     */
    
    private String name;
    
    public OperatorGroupLoadedEvent(String name) {
        this.name = name;
    }

    public String getName() {
		return name;
	}
}
