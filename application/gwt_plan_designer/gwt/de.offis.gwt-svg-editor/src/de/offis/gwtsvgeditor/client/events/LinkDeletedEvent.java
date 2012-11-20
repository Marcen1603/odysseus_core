package de.offis.gwtsvgeditor.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import de.offis.gwtsvgeditor.client.svg.SvgLink;

/**
 * Event to be fired over EventBus. Fired when a Link is deleted by the user.
 * 
 * @author Alexander Funk
 *
 */
public class LinkDeletedEvent extends GwtEvent<LinkDeletedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onLinkDeleted(LinkDeletedEvent e);
    }

    @Override
    protected void dispatch(LinkDeletedEvent.Handler handler) {
        handler.onLinkDeleted(this);
    }

    @Override
    public GwtEvent.Type<LinkDeletedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<LinkDeletedEvent.Handler> TYPE = new GwtEvent.Type<LinkDeletedEvent.Handler>();
    /**
     * Custom data held within this event object.
     */
    private SvgLink link;

    public LinkDeletedEvent(SvgLink link) {
        this.link = link;

    }

	public SvgLink getLink() {
		return link;
	}
}
