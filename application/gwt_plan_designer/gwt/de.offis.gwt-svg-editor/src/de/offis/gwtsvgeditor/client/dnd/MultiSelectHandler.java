package de.offis.gwtsvgeditor.client.dnd;

import de.offis.gwtsvgeditor.client.svg.SvgMultiSelectArea;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

import de.offis.gwtsvgeditor.client.SvgEditor;

/**
 * Open a rectangle on Click, Hold and Drag to allow 
 * selection of multiple module at once.
 * 
 * @author Alexander Funk
 *
 */
public class MultiSelectHandler extends WorkingAreaDragHandler {

    private SvgMultiSelectArea area;

    public MultiSelectHandler(SvgEditor editor) {
        super(editor);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        event.preventDefault();

        int x = (int) (editor.getViewBox()[0] + (event.getClientX() - editor.getAbsoluteLeft()) * editor.getZoomRatio());
        int y = (int) (editor.getViewBox()[1] + (event.getClientY() - editor.getAbsoluteTop()) * editor.getZoomRatio());
        area = new SvgMultiSelectArea(x, y);
        editor.add(area);
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        if (area != null) {
            int x = (int) (editor.getViewBox()[0] + (event.getClientX() - editor.getAbsoluteLeft()) * editor.getZoomRatio());
            int y = (int) (editor.getViewBox()[1] + (event.getClientY() - editor.getAbsoluteTop()) * editor.getZoomRatio());

            editor.pop(area);
            area.setEndPoint(x, y);
        }
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        if (area != null) {
            area.removeFromParent();
            area = null;
        }
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        if (area != null) {
            double[] a = area.getArea();
            editor.selectElementsinArea(a[0], a[1], a[2], a[3]);

            area.removeFromParent();
            area = null;
        }
    }
}
