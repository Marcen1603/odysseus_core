package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;


import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.ProjectionUtil;

public class BasicLayer extends AbstractLayer{

	private ScreenManager screenmanager;
	private ScreenTransformation transformation;
	private Display display;
	private Canvas canvas;
	
	private Color waitBackground, waitForeground;
		
	public BasicLayer(ScreenManager screenmanager) {
		this.screenmanager = screenmanager;
		this.transformation = screenmanager.getTransformation();
		
		this.display = screenmanager.getDisplay();
		this.canvas = screenmanager.getCanvas();
		
		waitBackground = new Color(display, 160, 160, 160);
		waitForeground = new Color(display, 200, 200, 200);	
		
		canvas.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				BasicLayer.this.widgetDisposed(e);
			}
		});
		
		this.name = "Basic Layer";
	}
	
	
	@Override
	public void draw(GC gc) {
		Point size = canvas.getSize();
		int width = size.x, height = size.y;
		int x0 = (int) Math.floor(((double) transformation.getMapPosition().x)
				/ ProjectionUtil.TILE_SIZE);
		int y0 = (int) Math.floor(((double) transformation.getMapPosition().y)
				/ ProjectionUtil.TILE_SIZE);
		int x1 = (int) Math.ceil(((double) transformation.getMapPosition().x + width)
				/ ProjectionUtil.TILE_SIZE);
		int y1 = (int) Math.ceil(((double) transformation.getMapPosition().y + height)
				/ ProjectionUtil.TILE_SIZE);

		int dy = y0 * ProjectionUtil.TILE_SIZE - transformation.getMapPosition().y;
		for (int y = y0; y < y1; ++y) {
			int dx = x0 * ProjectionUtil.TILE_SIZE - transformation.getMapPosition().x;
			for (int x = x0; x < x1; ++x) {
				paintTile(gc, dx, dy, x, y);
				dx += ProjectionUtil.TILE_SIZE;
			}
			dy += ProjectionUtil.TILE_SIZE;
		}
		gc.setForeground(new Color(gc.getDevice(),255,0,0));
		if(screenmanager.getInfoText() != null){
		  gc.drawText(screenmanager.getInfoText(), 0 , 0,true); 
		}
	}
	
	private void paintTile(GC gc, int dx, int dy, int x, int y) {
		boolean imageDrawn = false;
		int xTileCount = 1 << transformation.getZoom();
		int yTileCount = 1 << transformation.getZoom();
		
		boolean tileInBounds = x >= 0 && x < xTileCount && y >= 0 && y < yTileCount;
			
			if (!imageDrawn && tileInBounds) {
				gc.setBackground(waitBackground);
				gc.fillRectangle(dx, dy, ProjectionUtil.TILE_SIZE,
						ProjectionUtil.TILE_SIZE);
				gc.setForeground(waitForeground);
				for (int yl = 0; yl < ProjectionUtil.TILE_SIZE; yl += 32) {
					gc.drawLine(dx, dy + yl, dx + ProjectionUtil.TILE_SIZE, dy + yl);
				}
				for (int xl = 0; xl < ProjectionUtil.TILE_SIZE; xl += 32) {
					gc.drawLine(dx + xl, dy, dx + xl, dy + ProjectionUtil.TILE_SIZE);
				}
		}
	}
	

	
	protected void widgetDisposed(DisposeEvent e) {
		waitBackground.dispose();
		waitForeground.dispose();
	}
	
}
