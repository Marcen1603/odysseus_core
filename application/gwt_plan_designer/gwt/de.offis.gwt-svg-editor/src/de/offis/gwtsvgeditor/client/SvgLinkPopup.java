package de.offis.gwtsvgeditor.client;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import de.offis.gui.client.gwtgraphics.Group;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gwtsvgeditor.client.svg.SvgLink;

/**
 * This SVG-Popup is shown when a user clicks on a SVG-Line.
 *
 * @author Alexander Funk
 * 
 */
public class SvgLinkPopup extends Group {


    public SvgLinkPopup(final SvgLink link) {
        super();

        Image closeButton = new Image(0, 0, 20, 20, "images/editor/closeButton.png");
        closeButton.addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(MouseDownEvent event) {
                event.stopPropagation();

                link.removeFromParent();
                removeFromParent();
            }
        });
        add(closeButton);
    }
}
