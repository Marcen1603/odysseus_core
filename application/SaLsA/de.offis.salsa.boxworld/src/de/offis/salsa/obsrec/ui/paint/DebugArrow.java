package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DebugArrow extends CanvasElement {

	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	
	public DebugArrow(double x, double y, double x3, double y3) {
		this.x1 = (int) x;
		this.y1 = (int) y;
		this.x2 = (int) x3;
		this.y2 = (int) y3;
	}

	
	@Override
	public void paint(Graphics g, DrawingContext ctx) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLACK);
		
		int x = (int) ctx.dragOffsetX;
		int y = (int) ctx.dragOffsetY;
		
		
		g2d.drawLine(x1+x, y1+y, x2+x, y2+y);	
	}
}
