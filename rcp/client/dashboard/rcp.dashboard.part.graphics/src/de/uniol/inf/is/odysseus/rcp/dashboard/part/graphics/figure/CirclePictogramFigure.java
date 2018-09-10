package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;


public class CirclePictogramFigure extends AbstractShapePictogramFigure<RectanglePictogram> {

	@Override
	protected void drawGraphic(Graphics g, Rectangle r) {
		g.drawOval(r.x, r.y, r.width, r.height);
		if(this.isFillColor()){
			g.setBackgroundColor(getColor());
			g.fillOval(r.x, r.y, r.width, r.height);
		}		
	}

}
