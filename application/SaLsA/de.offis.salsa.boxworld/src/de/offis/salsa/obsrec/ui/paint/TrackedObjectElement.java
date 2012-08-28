package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Map.Entry;

import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.TrackedObject.Type;
import de.offis.salsa.obsrec.TypeDetails;
import de.offis.salsa.obsrec.util.Util;

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
		
		PredictedNotFreePolygon p = new PredictedNotFreePolygon(Util.getMaxAffinityPolygon(box));
		p.paint(g, ctx);
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(box.getX() + "," + box.getY(), (int)getX(), (int)getY());
		
		Map<String, Double> d = box.getTypeAffinitys();
		
		int i = 1;
		for(Entry<String, Double> t : d.entrySet()){
			if (t.getValue() > 0) {
				g2d.drawString(t.getKey() + "(" + t.getValue() + ")",
						(int) x + box.getWidth() + 5, (int) y + 10 * i);
				i++;
			}
		}
		
	}

}
