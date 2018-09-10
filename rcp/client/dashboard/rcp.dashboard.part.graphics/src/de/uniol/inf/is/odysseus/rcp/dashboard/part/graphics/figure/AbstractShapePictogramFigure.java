package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractShapePictogram;

public abstract class AbstractShapePictogramFigure<T extends AbstractShapePictogram> extends AbstractPictogramFigure<T> {

	private boolean visibile = true;
	private Color color;
	private int width = 3;
	private boolean fillColor = false;
	private boolean keepRatio = false;
	private float rotate = 0.0f;

	public AbstractShapePictogramFigure() {
		super();		
	}

	@Override
	public void updateValues(T node) {
		this.visibile = node.isVisibile();
		this.setColor(node.getCurrentColor());
		this.setWidth(node.getWidth());
		this.setFillColor(node.getFillColor());
		this.setKeepRatio(node.isKeepRatio());
		this.rotate = node.getRotate();
	}

	@Override
	public void paintGraphic(Graphics g) {
		
		int shrink = (int)Math.ceil(width);
		Rectangle r = getContentBounds().getShrinked(shrink, shrink);		
		if(this.keepRatio){
			int size = Math.min(r.height, r.width);	
			int x = r.x+(r.width-size)/2;
			int y = r.y+(r.height-size)/2;
			r = r.setBounds(x, y, size, size);
		}		
		Point center = new Point(r.x+(r.width/2), r.y+(r.height)/2);
		g.setForegroundColor(getColor());
		g.setLineWidth(this.getWidth());
		if (this.visibile) {
			g.setAntialias(SWT.ON);
			g.translate(center.x, center.y);
			g.rotate(rotate);			
			g.translate(-center.x, -center.y);
			drawGraphic(g, r);					
		}
		
	
	}

	protected abstract void drawGraphic(Graphics g, Rectangle r);	

	@Override
	public Dimension getContentSize() {
		return getPreferredSize();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isFillColor() {
		return fillColor;
	}

	public void setFillColor(boolean fillColor) {
		this.fillColor = fillColor;
	}

	public boolean isKeepRatio() {
		return keepRatio;
	}

	public void setKeepRatio(boolean keepRatio) {
		this.keepRatio = keepRatio;
	}

}