package de.offis.gwtsvgeditor.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event to be fired over EventBus. Fired when a Module is deleted by the user.
 * 
 * @author Alexander Funk
 *
 */
public class ModuleDeletedEvent extends GwtEvent<ModuleDeletedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onModuleDeleted(ModuleDeletedEvent e);
    }

    @Override
    protected void dispatch(ModuleDeletedEvent.Handler handler) {
        handler.onModuleDeleted(this);
    }

    @Override
    public GwtEvent.Type<ModuleDeletedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<ModuleDeletedEvent.Handler> TYPE = new GwtEvent.Type<ModuleDeletedEvent.Handler>();

    /**
     * Custom data held within this event object.
     */
    private String _id;

    public ModuleDeletedEvent(String _id) {
        this._id = _id;
    }

    public String getId() {
        return this._id;
    }
}
