package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class CoordMarker extends PositionCanvasElement {

	private Rectangle2D rect = new Rectangle2D.Double();
	
	private static int width = 2;
	private static int height = 20;
	
	public CoordMarker(int x, int y) {
		super(x, y, width, height);
	}

	
	@Override
	public void paint(Graphics g, DrawingContext ctx) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLACK);
		
		double x = getX() + ctx.dragOffsetX;
		double y = getY() + ctx.dragOffsetY;
		
		rect.setRect(x, y - 9, getWidth(), getHeight());
		g2d.fill(rect);		
		
		rect.setRect(x-9, y, getHeight(), getWidth());
		g2d.fill(rect);
		g.drawString(getX() + "," + getY(), (int)x, (int)y);		
	}
}
