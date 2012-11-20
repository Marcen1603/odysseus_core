package de.offis.gui.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
/**
 * Event  is fired whenever a operator group was saved.
 * 
 * @author Alexander Funk
 *
 */
public class OperatorGroupSavedEvent extends GwtEvent<OperatorGroupSavedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onOperatorGroupSaved(OperatorGroupSavedEvent e);
    }

    @Override
    protected void dispatch(OperatorGroupSavedEvent.Handler handler) {
        handler.onOperatorGroupSaved(this);
    }

    @Override
    public GwtEvent.Type<OperatorGroupSavedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<OperatorGroupSavedEvent.Handler> TYPE = new GwtEvent.Type<OperatorGroupSavedEvent.Handler>();

    /**
     * Custom data held within this event object.
     */
    
    public OperatorGroupSavedEvent() {
        
    }

}
