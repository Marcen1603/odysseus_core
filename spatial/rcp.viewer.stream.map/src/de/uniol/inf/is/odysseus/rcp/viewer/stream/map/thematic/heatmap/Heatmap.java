package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;

public class Heatmap extends RasterLayer {

	ScreenManager screenManager;
	RasterLayerConfiguration config;
	ArrayList<Tuple<?>> data;
	
	public Heatmap(RasterLayerConfiguration configuration, ScreenManager screenManager) {
		super(configuration);
		this.screenManager = screenManager;
		this.config = configuration;
		data = new ArrayList<Tuple<?>>();
	}
	
	@Override
	public void draw(GC gc) {
		Image img = createImage();
		// Upper left of Germany, bottom right of Germany - have to replace by upper left of incoming data
		drawImage(gc, img, 53.77, 7.00, 47.7, 13.7);		
	}
	
	/**
	 * Draws an image in the given area
	 * @param gc
	 * @param img
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 */
	private void drawImage(GC gc, Image img, double lat1, double lng1, double lat2, double lng2) {
		
		ScreenTransformation transformation = screenManager.getTransformation();
		int srid = config.getSrid();		
		int[] newUpperRightCoords = transformation.transformCoord(new Coordinate(lng1,lat1), srid);
		int[] newBottomLeftCoords = transformation.transformCoord(new Coordinate(lng2,lat2), srid);	
		
		// Calculate width and height of the area we want to cover with the image
		int width = 100;
		if(newBottomLeftCoords[0] > newUpperRightCoords[0]) {
			width = newBottomLeftCoords[0] - newUpperRightCoords[0];
		} else {
			width = newUpperRightCoords[0] - newBottomLeftCoords[0];
		}
		
		int height = 100;
		if(newBottomLeftCoords[1] > newUpperRightCoords[1]) {
			height = newBottomLeftCoords[1] - newUpperRightCoords[1];
		} else {
			height = newUpperRightCoords[1] - newBottomLeftCoords[1];
		}
		
		// Draw the picture transparent and without interpolation
		gc.setAlpha(120);		
		gc.setInterpolation(SWT.NONE);
		gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, newUpperRightCoords[0], newUpperRightCoords[1], width, height);
		gc.setInterpolation(SWT.DEFAULT);
		gc.setAlpha(255);
	}
	
	private Image createImage() {
		Image image = new Image(Display.getDefault(), 4, 4);
		GC gc = new GC(image);
		gc.setForeground(new org.eclipse.swt.graphics.Color(Display.getDefault(),0,0,0));
		gc.setBackground(new org.eclipse.swt.graphics.Color(Display.getDefault(),255,0,0));
		Color[][] colors = createColorArray(4, 4);
		
		
		for(int i = 0; i < colors.length; i++) {
			for(int j = 0; j < colors[i].length; j++) {
				gc.setBackground(colors[i][j]);
				gc.fillRectangle(i, j, 1, 1);	
			}
		}
		
		return image;
	}
	
	private Color[][] createColorArray(int x, int y) {
		// TODO: Calculate size
		Color[][] colors = new Color[x][y];
		int red = 0;
		int green = 0;
		
		for(int i = 0; i < colors.length; i++) {
			for(int j = 0; j < colors[i].length; j++) {
				colors[i][j] = new Color(Display.getDefault(),100,100,100);
			}
		}
		
		for(Tuple<?> tuple : data) {
			// TODO: Do this right, not just testing
			if((double) tuple.getAttribute(0) == 51.0 ) {
				red += (int) tuple.getAttribute(2);
				if(red >= 255)
					red = 0;
				colors[0][0] = new Color(Display.getDefault(),red,0,0);
			} else if ((double) tuple.getAttribute(0) == 48.0){
				green+= (int) tuple.getAttribute(2);
				if(green >= 255)
					green = 0;
				colors[2][2] = new Color(Display.getDefault(),0,green,0);
			}
		}
		
		return colors;
	}
	
	private double getTotalWidth() {
		// Calculate with lng
		return 100.0;
	}
	
	private double getTotalHight() {
		// Calculate with lat
		return 100.0;
	}
	
	/**
	 * Hopefully the LayerUpdater will fill the tuples in here, so we can build a picture
	 * from this data
	 */
	@Override
	public void addTuple(Tuple<?> tuple) {
		System.out.println("Tupel in der Heatmap angekommen: " + tuple.toString());
		data.add(tuple);
		// Maybe we should redraw the image
	}

}
