package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.RectanglePictogram;


public class HexagonPictogramFigure extends AbstractShapePictogramFigure<RectanglePictogram> {

	@Override
	protected void drawGraphic(Graphics g, Rectangle r) {
		PointList list = new PointList();
		// top right side 
		list.addPoint((int)(0.75*r.width), 0);
		// top left side
		list.addPoint((int)(0.25*r.width), 0);
		// left middle
		list.addPoint(0, r.height/2);
		// bottom left side
		list.addPoint((int)(0.25*r.width), r.height);
		// bottom right side
		list.addPoint((int)(0.75*r.width), r.height);
		// right middle
		list.addPoint(r.width, (r.height/2));	
		g.drawPolygon(list);
		if(this.isFillColor()){
			g.setBackgroundColor(getColor());
			g.fillPolygon(list);
		}		
	}

}
