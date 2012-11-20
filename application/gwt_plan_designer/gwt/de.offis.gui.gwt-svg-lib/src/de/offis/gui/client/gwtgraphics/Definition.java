package de.offis.gui.client.gwtgraphics;

import com.google.gwt.user.client.ui.UIObject;
import de.offis.gui.client.gwtgraphics.impl.util.SVGUtil;

/**
*
* @author Alexander Funk
*/
public abstract class Definition extends UIObject {

    public Definition(String tag) {
        setElement(SVGUtil.createSVGElementNS(tag));
    }
}
