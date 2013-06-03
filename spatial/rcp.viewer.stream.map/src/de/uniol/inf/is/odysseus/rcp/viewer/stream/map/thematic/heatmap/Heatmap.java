package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.DataSet;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;

public class Heatmap extends RasterLayer {

	ScreenManager screenManager;
	RasterLayerConfiguration config;
	Envelope heatMapArea;
	
	private LayerUpdater layerUpdater;
	
	public Heatmap(RasterLayerConfiguration configuration, ScreenManager screenManager) {
		super(configuration);
		this.screenManager = screenManager;
		this.config = configuration;
	}
	
	@Override
	public void draw(GC gc) {
		initHeatMapArea(53.77, 7.00, 47.7, 13.7);
		Image img = createImage();
		// Upper left of Germany, bottom right of Germany - have to replace by upper left of incoming data
		drawImage(gc, img);		
	}
	
	private void initHeatMapArea(double lat1, double lng1, double lat2, double lng2) {
		ScreenTransformation transformation = screenManager.getTransformation();
		int srid = config.getSrid();		
		int[] newUpperRightCoords = transformation.transformCoord(new Coordinate(lng1,lat1), srid);
		int[] newBottomLeftCoords = transformation.transformCoord(new Coordinate(lng2,lat2), srid);	
		
		// Create an Envelope from this coordinates
		// Maybe it helps us not to get in trouble with negative coordinates
		heatMapArea = new Envelope(newUpperRightCoords[0], newBottomLeftCoords[0], newUpperRightCoords[1], newBottomLeftCoords[1]);
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
	private void drawImage(GC gc, Image img) {
		// Draw the picture transparent and without interpolation
		gc.setAlpha(120);
		gc.setInterpolation(SWT.NONE);
		gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height,
				(int) heatMapArea.getMinX(), (int) heatMapArea.getMinY(),
				(int) heatMapArea.getWidth(), (int) heatMapArea.getHeight());
		gc.setInterpolation(SWT.DEFAULT);
		gc.setAlpha(255);
	}
	
	private Image createImage() {
		Image image = new Image(Display.getDefault(), 10, 10);
		GC gc = new GC(image);
		gc.setForeground(new org.eclipse.swt.graphics.Color(Display.getDefault(),0,0,0));
		gc.setBackground(new org.eclipse.swt.graphics.Color(Display.getDefault(),255,0,0));
		Color[][] colors = createColorArray(10, 10);		
		
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

		// Fill with standard-grey
		for(int i = 0; i < colors.length; i++) {
			for(int j = 0; j < colors[i].length; j++) {
				colors[i][j] = new Color(Display.getDefault(),100,100,100);
			}
		}
		
		// The tuples are now stored in the LayerUpdater to reduce redundant data
		int geometryAttributeIndex = 0;			// Here should be a point
		Envelope searchEnv = new Envelope();
		searchEnv.init(new Coordinate(49.7, 11.7)); // Maybe somewhere in Germany
		// Get everything
		searchEnv.expandToInclude(new Coordinate(Integer.MIN_VALUE, Integer.MAX_VALUE));	// Top left
		searchEnv.expandToInclude(new Coordinate(Integer.MAX_VALUE, Integer.MIN_VALUE)); 	// Bottom right
		List<?> data = layerUpdater.query(searchEnv, geometryAttributeIndex);
		
		int[][] valueSum = new int[x][y];
		int maxSum = 0;
		int minSum = 0;
		
		for(Object dataSet : data) {
			
			// Get the data from the Tuple (Where it is and value)
			Tuple tuple = ((DataSet) dataSet).getTuple();
			GeometryCollection geoColl = (GeometryCollection) tuple.getAttribute(0);
			Point point = geoColl.getCentroid();
			int value = (int) tuple.getAttribute(1);
			
			// Calculate, where this belongs in the heatmap
			ScreenTransformation transformation = screenManager.getTransformation();
			int srid = config.getSrid();		
			int[] transformedPoint = transformation.transformCoord(point.getCoordinate(), srid);
			
			Envelope tempEnv = new Envelope(new Coordinate(transformedPoint[0], transformedPoint[1]));
			
			// If this point is not in the heatMapArea -> expand heatMapArea
			heatMapArea.expandToInclude(tempEnv);
			
			double partsWidth = (int) heatMapArea.getWidth() / (x - 1);
			int posX = (int) ((tempEnv.getMaxX() - heatMapArea.getMinX()) / partsWidth);
			
			double partsHeight = (int) heatMapArea.getHeight() / (y - 1);
			int posY = (int) ((tempEnv.getMaxY() - heatMapArea.getMinY()) / partsHeight);
			
			// Add the value to the sum of this tile in the heatmap
			valueSum[posX][posY] += value;
			
			// Get the maximum and minimum sum
			if(valueSum[posX][posY] > maxSum)
				maxSum = valueSum[posX][posY];
			if(valueSum[posX][posY] < minSum)
				minSum = valueSum[posX][posY];
		}
		
		// Fill the colors-array with
		// the right color for the valueSum
		for (int ix = 0; ix < colors.length; ix++) {
			for (int iy = 0; iy < colors[ix].length; iy++) {
				colors[ix][iy] = getColorForValue(valueSum[ix][iy], minSum,
						maxSum, new Color(Display.getDefault(), 255, 0, 0),
						new Color(Display.getDefault(), 0, 255, 0));
			}
		}
		
		return colors;
	}
	
	/**
	 * Calculates the color for the value between the minColor and the maxColor
	 * @param value value of the tile you want to get the color for
	 * @param min minimum value of the heatmap
	 * @param max maximum value of the heatmap
	 * @param minColor Color for the minimum value in the heatmap, e.g. green
	 * @param maxColor Color for the maximum value in the heatmap, e.g. red
	 * @return Color of the value
	 */
	private Color getColorForValue(double value, double min, double max,
			Color minColor, Color maxColor) {

		// We don't want negative solutions
		if(min < 0) {
			max += (min * (-1));
			value += (min * (-1));
			min = 0;
		}
		
		// If we don't have any values
		double valueWeight = 0;
		double weightMinColor = 1;
		
		// We don't want to divide by 0
		if ((min + max) != 0) {
			valueWeight = value / (min + max); // weight max color
			weightMinColor = 1 - valueWeight;
		}
		
		// Mix the colors
		double r = weightMinColor * minColor.getRed() + valueWeight
				* maxColor.getRed();
		double g = weightMinColor * minColor.getGreen() + valueWeight
				* maxColor.getGreen();
		double b = weightMinColor * minColor.getBlue() + valueWeight
				* maxColor.getBlue();
		Color color = new Color(Display.getDefault(), (int) r, (int) g, (int) b);

		return color;
	}

	@Override
	public void setLayerUpdater(LayerUpdater layerUpdater) {
		this.layerUpdater = layerUpdater;
	}
	
}
