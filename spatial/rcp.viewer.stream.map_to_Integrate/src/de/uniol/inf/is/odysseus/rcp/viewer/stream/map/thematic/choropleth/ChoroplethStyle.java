package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PolygonStyle;

public class ChoroplethStyle extends PolygonStyle{
	private int transparency;
	public ChoroplethStyle(int lineWidth, Color lineColor, Color fillColor, int transparency) {
		super(lineWidth, lineColor, fillColor);
		this.transparency = transparency;
	}
	public ChoroplethStyle(ChoroplethStyle style) {
		super(style.getLineWidth(), style.getLineColor(), style.getFillColor());
		this.transparency = style.transparency;
	}
	public void draw(GC gc, int[][] list) {
		gc.setAlpha(transparency);
		draw(gc, list, getLineColor(), getFillColor());
		super.draw(gc, list);
	}
	public void draw(GC gc, int[] list) {
		gc.setAlpha(transparency);
		draw(gc, list, getLineColor(), getFillColor());
	}

	public int getTransparency() {
		return transparency;
	}
	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}

	public final static ChoroplethStyle DEFAULTCHOROPLETHSTYLE = new ChoroplethStyle(1, new Color(Display.getCurrent(), 0, 0, 0), new Color(Display.getCurrent(), 0, 255, 0), 255);
}
