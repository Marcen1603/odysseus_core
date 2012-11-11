package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;

public class LocationStyle extends PointStyle{
	private int transparency;
	public LocationStyle(SHAPE shape, int size, int lineWidth, Color lineColor,	Color fillColor, int transparency) {
		super(shape, size, lineWidth, lineColor, fillColor);
		this.transparency = transparency;
	}
	public LocationStyle(LocationStyle style){
		super(style.getShape(), style.getSize(), style.getLineWidth(), style.getLineColor(), style.getFillColor());
		this.transparency = style.transparency;
	}
	public void draw(GC gc, int[] list) {
		gc.setAlpha(transparency);
		draw(gc, list, getLineColor(), getFillColor());
		super.draw(gc, list);
	}
	public int getTransparency() {
		return transparency;
	}
	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}
	
	public Image getImage() {
		int[] list = { DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2 };
		Display display = Display.getCurrent();
		Color white = display.getSystemColor(SWT.COLOR_WHITE);
	    Color black = display.getSystemColor(SWT.COLOR_BLACK);
	    Image image = new Image(display, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	    GC gc = new GC(image);
		draw(gc, list);
	    gc.dispose();
	    ImageData imageData = image.getImageData();
	    
	    PaletteData palette = new PaletteData(new RGB[] { new RGB(0, 0, 0),
		        new RGB(0xFF, 0xFF, 0xFF), });
	    ImageData maskData = new ImageData(DEFAULT_WIDTH, DEFAULT_HEIGHT, 1, palette);
	    Image mask = new Image(Display.getCurrent(), maskData);
	    gc = new GC(mask);
	    gc.setBackground(black);
	    gc.fillRectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	    draw(gc, list, white, white);
	    gc.dispose();
	    maskData = mask.getImageData();

		Image icon = new Image(display, imageData, maskData);
		
		return icon;
	}
	
	public final static LocationStyle DEFAULTLOCATIONSTYLE = new LocationStyle(PointStyle.SHAPE.CIRCLE, 5, 1, new Color(Display.getCurrent(), 255,255,255), new Color(Display.getCurrent(), 0, 0, 0), 255);
}
