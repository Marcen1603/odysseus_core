package de.offis.gwtsvgeditor.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import de.offis.gwtsvgeditor.client.svg.SvgLink;

/**
 * Event to be fired over EventBus. Fired when a Link is created by the user.
 * 
 * @author Alexander Funk
 *
 */
public class LinkCreatedEvent extends GwtEvent<LinkCreatedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onLinkCreated(LinkCreatedEvent e);
    }

    @Override
    protected void dispatch(LinkCreatedEvent.Handler handler) {
        handler.onLinkCreated(this);
    }

    @Override
    public GwtEvent.Type<LinkCreatedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<LinkCreatedEvent.Handler> TYPE = new GwtEvent.Type<LinkCreatedEvent.Handler>();
    /**
     * Custom data held within this event object.
     */
    private SvgLink wiring;

    public LinkCreatedEvent(SvgLink wiring) {
        this.wiring = wiring;

    }

    public SvgLink getWiring() {
        return this.wiring;
    }
}
