package de.offis.salsa.obsrec.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.offis.salsa.lms.model.Sample;

@SuppressWarnings("serial")
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
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		double x = getX();
		double y = getY();
		double width = getWidth();
		double height = getHeight();
				
		rect.setRect(x, y, width, height);
		
		g2d.setColor(Color.BLACK);

		g2d.fill(rect);
//		g.drawString(getX() + "," + getY(), getX(), getY());		
	}

}
