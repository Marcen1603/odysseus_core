package de.offis.gwtsvgeditor.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * Event to be fired over EventBus. Fired when a Module is created by the user.
 * 
 * @author Alexander Funk
 *
 */
public class ModuleCreatedEvent extends GwtEvent<ModuleCreatedEvent.Handler> {

    /**
     * Interface to describe this event. Handlers must implement.
     */
    public interface Handler extends EventHandler {
        public void onOperatorDrop(ModuleCreatedEvent e);
    }

    @Override
    protected void dispatch(ModuleCreatedEvent.Handler handler) {
        handler.onOperatorDrop(this);
    }

    @Override
    public GwtEvent.Type<ModuleCreatedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<ModuleCreatedEvent.Handler> TYPE = new GwtEvent.Type<ModuleCreatedEvent.Handler>();

    /**
     * Custom data held within this event object.
     */

    private Widget module;

    public ModuleCreatedEvent(Widget module){
        super();
        this.module = module;
    }

    public Widget getModule(){
        return this.module;
    }
}
