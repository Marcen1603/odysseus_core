package de.offis.gui.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event  is fired whenever a operator group was created.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorGroupCreatedEvent extends GwtEvent<OperatorGroupCreatedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onOperatorGroupCreated(OperatorGroupCreatedEvent e);
    }

    @Override
    protected void dispatch(OperatorGroupCreatedEvent.Handler handler) {
        handler.onOperatorGroupCreated(this);
    }

    @Override
    public GwtEvent.Type<OperatorGroupCreatedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<OperatorGroupCreatedEvent.Handler> TYPE = new GwtEvent.Type<OperatorGroupCreatedEvent.Handler>();

    /**
     * Custom data held within this event object.
     */
    private String name;
    
    public OperatorGroupCreatedEvent(String name) {
        this.name = name;
    }
    
    public String getName() {
		return name;
	}
}
