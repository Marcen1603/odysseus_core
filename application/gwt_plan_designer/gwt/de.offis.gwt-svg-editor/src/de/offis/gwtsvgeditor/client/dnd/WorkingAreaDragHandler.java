package de.offis.gwtsvgeditor.client.dnd;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

import de.offis.gwtsvgeditor.client.SvgEditor;

/**
 * MouseHandler to achieve navigation by moving the working area by ClicknHold.
 * 
 * @author Alexander Funk
 *
 */
public class WorkingAreaDragHandler implements MouseWheelHandler,
        MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler {

    private boolean dragging = false;
    private boolean wasDragged = false;
    private int startX;
    private int startY;
    protected SvgEditor editor;

    public WorkingAreaDragHandler(SvgEditor editor) {
        this.editor = editor;
    }

    public void onMouseWheel(MouseWheelEvent event) {
        event.preventDefault();

        if (event.getDeltaY() < 0) {
            editor.zoomIn(event.getX(), event.getY());
        } else {
            editor.zoomOut(event.getX(), event.getY());
        }
    }

    public void onMouseDown(MouseDownEvent event) {
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
        if (!wasDragged) {
            editor.deselectAll();
        }

        if (dragging) {
            dragging = false;
            DOM.releaseCapture(((Widget) event.getSource()).getElement());
        }
    }

    public void onMouseMove(MouseMoveEvent event) {
        event.stopPropagation();

        if (dragging) {
            wasDragged = true;
            int wegX = event.getScreenX() - startX;
            int wegY = event.getScreenY() - startY;

            startX = event.getScreenX();
            startY = event.getScreenY();



            double newX = editor.getViewBox()[0] - (double) ((double) wegX * (double) editor.getViewBox()[2]) / (double) editor.getElement().getClientWidth();
            double newY = editor.getViewBox()[1] - (double) ((double) wegY * (double) editor.getViewBox()[3]) / (double) editor.getElement().getClientHeight();

            editor.setViewBox(newX, newY, editor.getViewBox()[2], editor.getViewBox()[3]);

        }
    }

    public void onMouseOut(MouseOutEvent event) {
        if (dragging) {
            dragging = false;
            DOM.releaseCapture(((Widget) event.getSource()).getElement());
        }
    }
}
