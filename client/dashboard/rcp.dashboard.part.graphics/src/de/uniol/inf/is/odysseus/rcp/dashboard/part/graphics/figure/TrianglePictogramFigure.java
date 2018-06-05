package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;


public class TrianglePictogramFigure extends AbstractShapePictogramFigure<RectanglePictogram> {

	@Override
	protected void drawGraphic(Graphics g, Rectangle r) {
		PointList list = new PointList();
		list.addPoint(r.x, r.y+r.height);		
		list.addPoint(r.x+r.width, r.y+r.height);
		list.addPoint(r.x+(r.width/2), r.y);
		g.drawPolygon(list);
		if(this.isFillColor()){
			g.setBackgroundColor(getColor());
			g.fillPolygon(list);
		}		
	}

}
