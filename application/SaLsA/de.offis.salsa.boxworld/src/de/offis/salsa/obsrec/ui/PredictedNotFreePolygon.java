package de.offis.salsa.obsrec.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class PredictedNotFreePolygon extends CanvasElement {

	private static final Color COLOR = new Color(100, 100, 100, 80);
	
	private Polygon p;
	
	public PredictedNotFreePolygon(Polygon poly) {
		this.p = poly;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		p.translate(offsetX, offsetY);
		
		g2d.setColor(COLOR);
		
		g2d.fill(p);
		
		p.translate(-offsetX, -offsetY);
	}
}
