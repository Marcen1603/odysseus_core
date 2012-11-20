package de.offis.gwtsvgeditor.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import de.offis.gwtsvgeditor.client.svg.SvgLink;

/**
 * Event to be fired over EventBus. Fired when a Link is selected by the user.
 * 
 * @author Alexander Funk
 *
 */
public class LinkSelectedEvent extends GwtEvent<LinkSelectedEvent.Handler> {

    public interface Handler extends EventHandler {

        public void onLinkSelected(LinkSelectedEvent e);
    }

    @Override
    protected void dispatch(LinkSelectedEvent.Handler handler) {
        handler.onLinkSelected(this);
    }

    @Override
    public GwtEvent.Type<LinkSelectedEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<LinkSelectedEvent.Handler> TYPE = new GwtEvent.Type<LinkSelectedEvent.Handler>();
    /**
     * Custom data held within this event object.
     */
    private SvgLink link;
    private int x;
    private int y;

    public LinkSelectedEvent(SvgLink link, int x, int y) {
        this.link = link;
        this.x = x;
        this.y = y;
    }

    public SvgLink getLink() {
        return this.link;
    }

    public int getScreenX(){
        return x;
    }

    public int getScreenY(){
        return y;
    }
}
