package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.TrackedObject.Type;
import de.offis.salsa.obsrec.TypeDetails;

public class TrackedObjectElement extends PositionCanvasElement {

	private Rectangle2D bbox = new Rectangle2D.Double();
	
	private TrackedObject box;
	
	public TrackedObjectElement(TrackedObject trackedObj) {
		super(trackedObj.getX(), trackedObj.getY(), trackedObj.getWidth(), trackedObj.getHeight());
		this.box = trackedObj;
	}

	@Override
	public void paint(Graphics g, DrawingContext ctx) {
		Graphics2D g2d = (Graphics2D) g;
		
		double x = getX()+ctx.dragOffsetX;
		double y = getY()+ctx.dragOffsetY;
		
		bbox.setRect(x, y, getWidth(), getHeight());		
		g2d.setColor(Color.RED);		
		g2d.draw(bbox);
		
		PredictedNotFreePolygon p = new PredictedNotFreePolygon(box.getPolygonContainer().getPolygon(box.getTypeDetails().getMaxAffinityType()));
		p.paint(g, ctx);
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(box.getX() + "," + box.getY(), (int)getX(), (int)getY());
		
		TypeDetails d = box.getTypeDetails();
		
		int i = 1;
		for(Type t : d){
			if (d.getTypeAffinity(t) > 0) {
				g2d.drawString(t.toString() + "(" + d.getTypeAffinity(t) + ")",
						(int) x + box.getWidth(), (int) y + 20 * i);
				i++;
			}
		}
		
	}

}
