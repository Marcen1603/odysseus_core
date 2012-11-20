package de.offis.gui.client.gwtgraphics;

import com.google.gwt.user.client.Element;

/**
 *
 * @author Alexander Funk
 */
public class Marker extends Definition {

    public Marker(String id) {
        super("marker");
        getElement().setAttribute("id", id);
        getElement().setAttribute("viewBox", "0 0 10 10");
        getElement().setAttribute("refY", "5");
        getElement().setAttribute("markerUnits", "strokeWidth");
        getElement().setAttribute("markerWidth", "4");
        getElement().setAttribute("markerHeight", "3");
        getElement().setAttribute("orient", "auto");
    }

    public Marker addElement(Element element){
       getElement().appendChild(element);
       return this;
    }

    public Marker setRefX(int x){
        getElement().setAttribute("refX", x+"");
        return this;
    }
}
