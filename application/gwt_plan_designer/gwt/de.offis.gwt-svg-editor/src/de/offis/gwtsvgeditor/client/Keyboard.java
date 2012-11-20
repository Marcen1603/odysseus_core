package de.offis.gwtsvgeditor.client;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import de.offis.gwtsvgeditor.client.events.GlobalKeyChangeEvent;

/**
 * Will catch all Key-events and re-route them onto an EventBus.
 *
 * @author Alexander Funk
 * 
 */
public final class Keyboard {

    private static final class WindowCloseHandlerImpl implements CloseHandler<Window> {

        public native void onWindowClosed() /*-{
        $doc.onkeydown = null;
        $doc.onkeypress = null;
        $doc.onkeyup = null;
        }-*/;

        private native void init()/*-{
        $doc.onkeydown = function(evt) {
        @de.offis.gwtsvgeditor.client.Keyboard::onKeyDown(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
        }
        $doc.onkeypress = function(evt) {
        @de.offis.gwtsvgeditor.client.Keyboard::onKeyPress(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
        }
        $doc.onkeyup = function(evt) {
        @de.offis.gwtsvgeditor.client.Keyboard::onKeyUp(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
        }
        }-*/;

        public void onClose(CloseEvent<Window> event) {
            onWindowClosed();
        }
    }

    static {
        WindowCloseHandlerImpl closeHandler = new WindowCloseHandlerImpl();
        Window.addCloseHandler(closeHandler);
        closeHandler.init();
    }

    private static EventBus events;

    @SuppressWarnings("unused")
	private static void onKeyDown(Event event) {
        char keyCode = (char) DOM.eventGetKeyCode(event);
        
        events.fireEvent(new GlobalKeyChangeEvent(keyCode, true));
    }

    @SuppressWarnings("unused")
	private static void onKeyPress(Event event) {
        char keyCode = (char) DOM.eventGetKeyCode(event);
        // ....your code here....
    }

    @SuppressWarnings("unused")
	private static void onKeyUp(Event event) {
        char keyCode = (char) DOM.eventGetKeyCode(event);
        
        events.fireEvent(new GlobalKeyChangeEvent(keyCode, false));
    }

    public Keyboard(EventBus events) {
        Keyboard.events = events;
    }
}
