package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.vividsolutions.jts.math.Vector2D;

public class DebugJtsVector extends CanvasElement {

	private Vector2D v = null;
	
	public DebugJtsVector(Vector2D v) {
		this.v = v;
	}

	
	@Override
	public void paint(Graphics g, DrawingContext ctx) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLACK);
		
		int zerox = (int) (0 + ctx.dragOffsetX);
		int zeroy = (int) (0 + ctx.dragOffsetY);
		
		int x = (int) (v.getX() + ctx.dragOffsetX);
		int y = (int) (v.getY() + ctx.dragOffsetY);
		
		
		
		g2d.drawLine(zerox, zeroy, x, y);
		g2d.fillRect(x, y, 10, 10);
	}
}
