package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class DebugVector extends CanvasElement {

	private Vector2D v = null;
	
	public DebugVector(Vector2D v) {
		this.v = v;
	}

	
	@Override
	public void paint(Graphics g, DrawingContext ctx) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLACK);
		
		int zerox = (int) (v.getZero().getX() + ctx.dragOffsetX);
		int zeroy = (int) (v.getZero().getY() + ctx.dragOffsetY);
		
		int x = (int) (v.getX() + ctx.dragOffsetX);
		int y = (int) (v.getY() + ctx.dragOffsetY);
		
		
		
		g2d.drawLine(zerox, zeroy, x, y);
		g2d.fillRect(x, y, 10, 10);
	}
}
