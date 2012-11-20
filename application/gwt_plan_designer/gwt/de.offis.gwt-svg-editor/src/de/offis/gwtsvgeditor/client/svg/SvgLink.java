package de.offis.gwtsvgeditor.client.svg;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import de.offis.gui.client.gwtgraphics.Group;
import de.offis.gui.client.gwtgraphics.shape.Path;
import de.offis.gui.client.gwtgraphics.shape.path.CurveTo;
import de.offis.gui.client.gwtgraphics.shape.path.MoveTo;
import de.offis.gwtsvgeditor.client.SvgEditor;
import de.offis.gwtsvgeditor.client.SvgLinkPopup;
import de.offis.gwtsvgeditor.client.events.LinkCreatedEvent;
import de.offis.gwtsvgeditor.client.events.LinkDeletedEvent;
import de.offis.gwtsvgeditor.client.events.LinkSelectedEvent;

/**
 * Link between two ports.
 * 
 * @author Alexander Funk
 *
 */
public class SvgLink extends Group implements ClickHandler, MouseOutHandler, MouseOverHandler,
        LinkSelectedEvent.Handler {

    private String _id; // start + end
    private boolean selected = false;
    private final SvgEditor editor;
    protected SvgPort source;
    protected SvgPort destination;
    protected Path link;

    public SvgLink(SvgEditor editor, SvgPort source, SvgPort destination) {
        link = new Path((int) (source.getX() + source.getWidth() / 2), (int) (source.getY() + source.getHeight()));
        // we need to set this line, otherwise update() throws an error because 
        // there is only 1 step inside the datastracture and it cant set step 2
        link.lineTo(0,0);
        add(link);        

        this._id = source.getId() + " <-> " + destination.getId();
        this.editor = editor;

        this.source = source;
        this.destination = destination;

        link.setStyleName("wiring");
        link.setFillOpacity(0);
        link.setStrokeWidth(5);
        link.setStrokeColor("black");

        link.addClickHandler(this);
        link.addMouseOutHandler(this);
        link.addMouseOverHandler(this);

        // subscribe to events on eventbus
        editor.getEventBus().addHandler(LinkSelectedEvent.TYPE, this);
    }

    public final void init() {
        source.addLink(this);
        destination.addLink(this);
        update();
        editor.getEventBus().fireEvent(new LinkCreatedEvent(this));
    }

    public final void update() {
        int x1 = (int) (source.getX() + source.getWidth() / 2);
        int y1 = (int) (source.getY() + source.getHeight());

        int x2 = (int) (destination.getX() + destination.getWidth() / 2);
        int y2 = (int) destination.getY();

        update(x1, y1, x2, y2);
    }

    public void update(int x1, int y1, int x2, int y2) {
        if(popup != null){
            popup.setTranslate(x1 + (x2 - x1 )/2, y1 + (y2 - y1 )/2);
        }

        int abstand = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        int controlPoints = 50;
        // controlpoints anhand des abstands anpassen
        if (abstand < 300) {
            controlPoints = abstand / 4;
        }
        if (abstand < 100) {
            controlPoints = abstand / 2;
        }

        link.setStep(0, new MoveTo(false, x1, y1));
//        link.setStep(1, new LineTo(false, x2, y2));
        link.setStep(1, new CurveTo(false, x1, y1 + controlPoints, // first controlpoint = x is same as start but y is 100px higher
                x2, y2 - controlPoints, // second controlpoint = x is same as end, but 100 px lower
                x2, y2));// x,y from endpiont
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SvgLink)) {
            return false;
        }

        SvgLink temp = (SvgLink) obj;

        if (this._id.equals(temp._id)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 23 * hash + (this.destination != null ? this.destination.hashCode() : 0);
        hash = 23 * hash + (this.link != null ? this.link.hashCode() : 0);
        return hash;
    }

    public String getId() {
        return _id;
    }

    @Override
    public void removeFromParent() {
        source.removeLink(this);
        destination.removeLink(this);

        source = null;
        destination = null;

        editor.getEventBus().fireEvent(new LinkDeletedEvent(this));

        if (isAttached()) {
            super.removeFromParent();
        }
    }
    
    private SvgLinkPopup popup = null;

    public void select(boolean select) {
        if (select) {
            link.setStrokeColor("silver");
            // show popup with buttons
            if (popup == null) {
                popup = new SvgLinkPopup(this);

                CurveTo curveto = (CurveTo)link.getStep(1);

                double x = (curveto.getX2() - curveto.getX1() )/2;
                double y = (curveto.getY2() - curveto.getY1() )/2;
                popup.setTranslate(curveto.getX1() + x, curveto.getY1() + y);
                add(popup);
                pop(popup);
            }
            selected = true;
        } else {
            link.setStrokeColor("black");

            // hide popup
            if (popup != null) {
                popup.removeFromParent();
                popup = null;
            }

            selected = false;
        }
    }

    public void onClick(ClickEvent event) {
        int x = event.getNativeEvent().getClientX();
        int y = event.getNativeEvent().getClientY();

        if (selected) {
            select(false);
        } else {
            select(true);
            editor.getEventBus().fireEvent(new LinkSelectedEvent(this, x, y));
        }
    }

    public void onMouseOut(MouseOutEvent event) {
        if (!selected) {
//            link.setStrokeColor("black");
        }
    }

    public void onMouseOver(MouseOverEvent event) {
        if (!selected) {
//            link.setStrokeColor("red");
        }
    }

    public void onLinkSelected(LinkSelectedEvent e) {
        // if this wiring is selected and another wiring gets selected, then
        // deselect this wiring so only one wiring at a time is selected
        if (selected && !e.getLink().equals(this)) {
            // other wiring got selected and this one was selected before
            select(false);
        }
    }

    public SvgPort getSource() {
        return this.source;
    }

    public SvgPort getDestination() {
        return this.destination;
    }

    public SvgEditor getEditor() {
        return this.editor;
    }
}
