package de.offis.salsa.obsrec.ui.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.offis.salsa.lms.model.Sample;

public class SampleIndicator extends PositionCanvasElement {

	private Rectangle2D rect = new Rectangle2D.Double();
	
	private static int width = 2;
	private static int height = 2;
	
	private Sample sample;
	
	public SampleIndicator(Sample sample) {
		super(sample.getX(), sample.getY(), width, height);
		this.sample = sample;
	}

	@Override
	public void paint(Graphics g, DrawingContext ctx) {
		Graphics2D g2d = (Graphics2D) g;
		
		double x = getX()+ctx.dragOffsetX;
		double y = getY()+ctx.dragOffsetY;
		double width = getWidth();
		double height = getHeight();
				
		rect.setRect(x, y, width, height);
		
		g2d.setColor(Color.BLACK);

		g2d.fill(rect);
//		g.drawString(getX() + "," + getY(), getX(), getY());		
	}

}
