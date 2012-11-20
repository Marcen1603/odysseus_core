package de.offis.gwtsvgeditor.client.svg;

import de.offis.gui.client.gwtgraphics.shape.Path;
import de.offis.gui.client.gwtgraphics.shape.path.LineTo;

/**
 * Preview of a link between two ports.
 * 
 * @author Alexander Funk
 *
 */
public class SvgGhostLink extends Path {

    public SvgGhostLink(double x1, double y1) {
        super((int)x1, (int)y1);
        lineTo((int)x1, (int)y1);
        
        setFillOpacity(0);
        setStrokeWidth(5);
        setStrokeColor("black");
        setStrokeDashArray("15,15");
    }

    public void update(double x2, double y2) {
        setStep(1, new LineTo(false, (int)x2, (int)y2));// x,y from endpiont
    }
}
