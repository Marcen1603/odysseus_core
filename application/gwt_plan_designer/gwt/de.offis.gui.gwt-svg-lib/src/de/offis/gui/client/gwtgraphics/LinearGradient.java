package de.offis.gui.client.gwtgraphics;

/**
*
* @author Alexander Funk
*/
public class LinearGradient extends Gradient {

    public LinearGradient(String id, String x1, String y1, String x2, String y2) {
        super("linearGradient");
        getElement().setAttribute("id", id);
        getElement().setAttribute("x1", x1);
        getElement().setAttribute("y1", y1);
        getElement().setAttribute("x2", x2);
        getElement().setAttribute("y2", y2);
    }
}
