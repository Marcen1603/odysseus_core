package de.offis.gui.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event  is fired whenever a operator group was closed.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorGroupClosedEvent extends GwtEvent<OperatorGroupClosedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onOperatorGroupClosed(OperatorGroupClosedEvent e);
    }

    @Override
    protected void dispatch(OperatorGroupClosedEvent.Handler handler) {
        handler.onOperatorGroupClosed(this);
    }

    @Override
    public GwtEvent.Type<OperatorGroupClosedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<OperatorGroupClosedEvent.Handler> TYPE = new GwtEvent.Type<OperatorGroupClosedEvent.Handler>();

    /**
     * Custom data held within this event object.
     */
    
    public OperatorGroupClosedEvent() {
      
    }
}
