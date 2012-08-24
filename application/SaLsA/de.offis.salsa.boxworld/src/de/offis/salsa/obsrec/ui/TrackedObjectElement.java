package de.offis.salsa.obsrec.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
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
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		bbox.setRect(getX(), getY(), getWidth(), getHeight());		
		g2d.setColor(Color.RED);		
		g2d.draw(bbox);
		
		PredictedNotFreePolygon p = new PredictedNotFreePolygon(box.getPoly());
		p.paint(g);
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(box.getX() + "," + box.getY(), (int)getX(), (int)getY());
		
		TypeDetails d = box.getTypeDetails();
		
		int i = 1;
		for(Type t : d){
			if (d.getTypeAffinity(t) > 0) {
				g2d.drawString(t.toString() + "(" + d.getTypeAffinity(t) + ")",
						(int) getX() + box.getWidth(), (int) getY() + 20 * i);
				i++;
			}
		}
		
	}

}
