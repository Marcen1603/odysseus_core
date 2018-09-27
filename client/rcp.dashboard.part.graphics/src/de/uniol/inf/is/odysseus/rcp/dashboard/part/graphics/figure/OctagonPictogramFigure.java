package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;


public class OctagonPictogramFigure extends AbstractShapePictogramFigure<RectanglePictogram> {

	@Override
	protected void drawGraphic(Graphics g, Rectangle r) {
		PointList list = new PointList();
		// top right  
		list.addPoint(r.x+(int)(0.75*r.width), r.y);
		// top left 
		list.addPoint(r.x+(int)(0.25*r.width), r.y);
		// left top 
		list.addPoint(r.x, r.y+(int)(0.25*r.height));
		// left bottom 
		list.addPoint(r.x, r.y+(int)(0.75*r.height));
		// bottom left side
		list.addPoint(r.x+(int)(0.25*r.width), r.y+r.height);
		// bottom right side
		list.addPoint(r.x+(int)(0.75*r.width), r.y+r.height);
		// right bottom 
		list.addPoint(r.x+r.width, r.y+(int)(0.75*r.height));
		// right top 
		list.addPoint(r.x+r.width, r.y+(int)(0.25*r.height));
		g.drawPolygon(list);
		if(this.isFillColor()){
			g.setBackgroundColor(getColor());
			g.fillPolygon(list);
		}		
	}

}
