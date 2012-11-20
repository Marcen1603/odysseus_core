package de.offis.gui.client.gwtgraphics;

/**
*
* @author Alexander Funk
*/
public class Pattern extends Definition {

    public Pattern(String id, String x, String y, String w, String h, String viewBox) {
        super("pattern");
        getElement().setAttribute("id", id);
        getElement().setAttribute("x", x);
        getElement().setAttribute("y", y);
        getElement().setAttribute("width", w);
        getElement().setAttribute("height", h);
        getElement().setAttribute("viewBox", viewBox);
        getElement().setAttribute("patternUnits", "userSpaceOnUse");
    }

    public Pattern add(VectorObject obj) {
        getElement().appendChild(obj.getElement());
        return this;
    }
}
