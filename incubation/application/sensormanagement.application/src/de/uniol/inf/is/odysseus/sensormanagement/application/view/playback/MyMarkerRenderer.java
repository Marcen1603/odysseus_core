package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.awt.Color;
import java.awt.Graphics;

import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.TimeBarViewerInterface;
import de.jaret.util.ui.timebars.swing.renderer.IMarkerRenderer;

public class MyMarkerRenderer implements IMarkerRenderer {
    protected Color _markerColor = Color.RED;

    /**
     * {@inheritDoc}
     */
    public int getMarkerWidth(TimeBarMarker marker) {
        return 4;
    }

    /**
     * {@inheritDoc}
     */
    public void renderMarker(TimeBarViewerDelegate delegate, Graphics graphics, TimeBarMarker marker, int x, boolean isDragged) 
    {
        Color oldCol = graphics.getColor();
        graphics.setColor(Color.RED);

        int markerY		 = marker.getY();
        int markerHeight = marker.getHeight();
        if (markerHeight == 0)
        	markerHeight = delegate.getDiagramRect().height + delegate.getXAxisHeight();
        
        if (delegate.getOrientation().equals(TimeBarViewerInterface.Orientation.HORIZONTAL)) {
            graphics.drawLine(x, markerY, x, markerY + markerHeight);
        } else {
            graphics.drawLine(markerY, x, markerY + markerHeight, x);
        }

        graphics.setColor(oldCol);
    }

}
