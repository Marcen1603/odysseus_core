package de.offis.gui.client.gwtgraphics;

import com.google.gwt.user.client.ui.Widget;
import de.offis.gui.client.gwtgraphics.impl.util.SVGUtil;

/**
*
* @author Alexander Funk
*/
public class GradientStop extends Widget {

    public GradientStop(String offset, String color, String opacity) {
        setElement(SVGUtil.createSVGElementNS("stop"));
        getElement().setAttribute("offset", offset);
        getElement().setAttribute("stop-color", color);
        getElement().setAttribute("stop-opacity", opacity);
    }
}
