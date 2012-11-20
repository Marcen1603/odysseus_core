package de.offis.gui.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event  is fired whenever a operator group was simulated.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorGroupSimulatedEvent extends GwtEvent<OperatorGroupSimulatedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onOperatorGroupSimulated(OperatorGroupSimulatedEvent e);
    }

    @Override
    protected void dispatch(OperatorGroupSimulatedEvent.Handler handler) {
        handler.onOperatorGroupSimulated(this);
    }

    @Override
    public GwtEvent.Type<OperatorGroupSimulatedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<OperatorGroupSimulatedEvent.Handler> TYPE = new GwtEvent.Type<OperatorGroupSimulatedEvent.Handler>();

    /**
     * Custom data held within this event object.
     */
    
    public OperatorGroupSimulatedEvent() {
        
    }

}
