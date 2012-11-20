package de.offis.gwtsvgeditor.client.dnd;

import de.offis.gwtsvgeditor.client.svg.SvgModule;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

import de.offis.gwtsvgeditor.client.SvgEditor;

/**
 * MouseHandler to achieve a DragnDrop of Modules on the Working area.
 * 
 * @author Alexander Funk
 *
 */
public class ObjectDragHandler implements MouseDownHandler, MouseUpHandler,
        MouseMoveHandler, ClickHandler, MouseOverHandler, DoubleClickHandler {

    private boolean dragging = false;
    private boolean wasDragged = false;
    private int startX;
    private int startY;
    private SvgModule elem;
    private SvgEditor editor;

    public ObjectDragHandler(SvgEditor editor, SvgModule elem) {
        this.elem = elem;
        this.editor = editor;
    }

    public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();
        event.stopPropagation();

        wasDragged = false;

        if (!dragging) {
            dragging = true;
            startX = event.getScreenX();
            startY = event.getScreenY();
            DOM.setCapture(((Widget) event.getSource()).getElement());
        }
    }

    public void onMouseUp(MouseUpEvent event) {
        if (dragging) {
            dragging = false;
            DOM.releaseCapture(((Widget) event.getSource()).getElement());
        }
    }

    public void onMouseMove(MouseMoveEvent event) {
        if (dragging) {
            wasDragged = true;
            int wegX = event.getScreenX() - startX;
            int wegY = event.getScreenY() - startY;

            startX = event.getScreenX();
            startY = event.getScreenY();

            elem.movePosition(wegX * editor.getZoomRatio(), wegY * editor.getZoomRatio());

            // damit die komplette selection bewegt wird
            if (elem.isSelected()) {
                for(SvgModule m : editor.getSelectedModules()){
                    if(!m.equals(elem)){
                        m.movePosition(wegX * editor.getZoomRatio(), wegY * editor.getZoomRatio());
                    }
                }
            }
        }
    }

    public void onClick(ClickEvent event) {
        if (!wasDragged) {
            if (editor.ctrlPressed) {
                elem.switchSelect();
            } else {
                editor.deselectAll();
                elem.switchSelect();
            }
        }
    }

    public void onDoubleClick(DoubleClickEvent event) {
        // dbl click connects all selected elements with the dblclick target
//		for (OperatorSVG o : selectedModules) {
//			createConnection(o, elem);
//			elem.select(false);
//			selectedModules.remove(elem);
//		}
    }

    public void onMouseOver(MouseOverEvent event) {
        // bring module to foreground an mouseOver
        editor.pop(elem);
    }
}
