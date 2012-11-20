package de.offis.gwtsvgeditor.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import de.offis.gwtsvgeditor.client.svg.SvgPort;

/**
 * Event to be fired over EventBus. Fired when a Port is clicked by the user.
 * 
 * @author Alexander Funk
 *
 */
public class PortClickEvent extends GwtEvent<PortClickEvent.Handler> {

    /**
     * Interface to describe this event. Handlers must implement.
     */
    public interface Handler extends EventHandler {
        public void onPortClicked(PortClickEvent e);
    }

    @Override
    protected void dispatch(PortClickEvent.Handler handler) {
        handler.onPortClicked(this);
    }

    @Override
    public GwtEvent.Type<PortClickEvent.Handler> getAssociatedType() {
        return TYPE;
    }
    public static final GwtEvent.Type<PortClickEvent.Handler> TYPE = new GwtEvent.Type<PortClickEvent.Handler>();

    /**
     * Custom data held within this event object.
     */

	private SvgPort port;
	
    public PortClickEvent(SvgPort port){
    	this.port = port;	
    }

	public SvgPort getPort() {
		return port;
	}
}
