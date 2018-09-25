package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;


public class PentagonPictogramFigure extends AbstractShapePictogramFigure<RectanglePictogram> {

	@Override
	protected void drawGraphic(Graphics g, Rectangle r) {
		PointList list = new PointList();
		// top middle
		list.addPoint(r.x+(r.width/2), r.y);
		// left middle
		list.addPoint(r.x, r.y+(r.height/2));
		// bottom left side
		list.addPoint(r.x+(int)(0.25*r.width), r.y+r.height);
		// bottom right side
		list.addPoint(r.x+(int)(0.75*r.width), r.y+r.height);
		// right middle
		list.addPoint(r.x+r.width, r.y+(r.height/2));		
		g.drawPolygon(list);
		if(this.isFillColor()){
			g.setBackgroundColor(getColor());
			g.fillPolygon(list);
		}		
	}

}
