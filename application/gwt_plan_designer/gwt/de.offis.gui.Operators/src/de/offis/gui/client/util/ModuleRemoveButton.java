package de.offis.gui.client.util;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import de.offis.gui.client.gwtgraphics.Image;
import de.offis.gwtsvgeditor.client.svg.SvgModule;

/**
 * Button to remove a specific module on click.
 *
 * @author Alexander Funk
 * 
 */
public class ModuleRemoveButton extends Image {

    public ModuleRemoveButton(final SvgModule module, int x, int y, int width, int height) {
        super(x, y, width, height, "images/editor/closeButton.png");

        addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(MouseDownEvent event) {
                event.stopPropagation();

                module.removeModule();
            }
        });
    }
}
