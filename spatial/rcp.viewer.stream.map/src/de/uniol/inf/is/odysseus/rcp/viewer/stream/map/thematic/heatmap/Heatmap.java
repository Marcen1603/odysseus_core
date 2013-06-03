package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap;

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
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;

public class Heatmap extends RasterLayer {

	
	private static final long serialVersionUID = -4394403246757791617L;
	ScreenManager screenManager;
	RasterLayerConfiguration config;
	Envelope heatMapArea;
	
	private LayerUpdater layerUpdater;
	
	public Heatmap(HeatmapLayerConfiguration configuration, ScreenManager screenManager) {
		super(configuration);
		this.screenManager = screenManager;
		this.config = configuration;
	}
	
	@Override
	public void draw(GC gc) {
		Color minColor = new Color(Display.getDefault(), 0, 255, 0);
		Color maxColor = new Color(Display.getDefault(), 255, 0, 0);
		Image img = createImage(20, 20, minColor, maxColor);
		drawImage(gc, img, false);		
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
	private void drawImage(GC gc, Image img, boolean interpolation) {
		// Draw the picture transparent and without interpolation
		gc.setAlpha(120);
		if(!interpolation)
			gc.setInterpolation(SWT.NONE);
		gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height,
				(int) heatMapArea.getMinX(), (int) heatMapArea.getMinY(),
				(int) heatMapArea.getWidth(), (int) heatMapArea.getHeight());
		gc.setInterpolation(SWT.DEFAULT);
		gc.setAlpha(255);
	}
	
	/**
	 * Creates an image which can be drawn on the display
	 * Image has numberX * numberY pixels
	 * @param numberX pixels in x-direction
	 * @param numberY pixels in y-direction
	 * @return Image for the heatmap-overlay with right colors
	 */
	private Image createImage(int numberX, int numberY, Color minColor, Color maxColor) {
		Image image = new Image(Display.getDefault(), numberX, numberY);
		GC gc = new GC(image);
		gc.setForeground(new org.eclipse.swt.graphics.Color(Display.getDefault(),0,0,0));
		gc.setBackground(new org.eclipse.swt.graphics.Color(Display.getDefault(),255,0,0));
		Color[][] colors = createColorArray(numberX, numberY, minColor, maxColor);		
		
		for(int i = 0; i < colors.length; i++) {
			for(int j = 0; j < colors[i].length; j++) {
				gc.setBackground(colors[i][j]);
				gc.fillRectangle(i, j, 1, 1);	
			}
		}
		
		return image;
	}
	
	private Color[][] createColorArray(int x, int y, Color minColor, Color maxColor) {
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
		
		// Create heatMapArea with the given tuples
		// (We could let this out and give an area by the user, but
		// this seems to be more user-friendly)
		heatMapArea = new Envelope();
		for (Object dataSet : data) {
			
			// Get the data from the Tuple (Where it is and value)
			Tuple<?> tuple = ((DataSet) dataSet).getTuple();
			GeometryCollection geoColl = (GeometryCollection) tuple
					.getAttribute(0);
			Point point = geoColl.getCentroid();

			// Calculate, where this belongs in the heatmap
			ScreenTransformation transformation = screenManager
					.getTransformation();
			int srid = config.getSrid();
			int[] transformedPoint = transformation.transformCoord(
					point.getCoordinate(), srid);

			Envelope tempEnv = new Envelope(new Coordinate(transformedPoint[0],
					transformedPoint[1]));

			// If this point is not in the heatMapArea -> expand heatMapArea
			heatMapArea.expandToInclude(tempEnv);
		}
		
		// If we have just one point -> Expand, so we can see something
		// on the map
		if(heatMapArea.getWidth() == 0) {
			heatMapArea.expandBy(20);
		}
		
		for(Object dataSet : data) {
			
			// Get the data from the Tuple (Where it is and value)
			Tuple<?> tuple = ((DataSet) dataSet).getTuple();
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
			
			 // +1 for the points which are exactly on the border
			// or if we just have a point as area (just one tuple)
			double partsWidth = (heatMapArea.getWidth() + 1) / x;
			int posX = (int) ((tempEnv.getMaxX() - heatMapArea.getMinX()) / partsWidth);
			
			double partsHeight = (heatMapArea.getHeight() + 1) / y;
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
						maxSum, minColor, maxColor);
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
