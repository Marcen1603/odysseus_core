package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class LaserAreaFreePolygon extends CanvasElement {

	private Polygon p = new Polygon();

	public LaserAreaFreePolygon() {
		p.addPoint(0, 0);
	}
	
	@Override
	public void paint(Graphics g, DrawingContext ctx) {
		Graphics2D g2d = (Graphics2D) g;		
		
		p.translate((int)ctx.dragOffsetX, (int)ctx.dragOffsetY);
		
		g2d.setColor(new Color(0, 255, 0, 80));		
		
		g2d.fill(p);
		
		p.translate((int)-ctx.dragOffsetX, (int)-ctx.dragOffsetY);
	}

	public void addPoint(int x, int y) {
		p.addPoint(x, y);
	}
}
