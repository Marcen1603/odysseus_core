package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.heatmap;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.DataSet;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.HeatmapLayerConfiguration;

public class Heatmap extends RasterLayer {

	private static final long serialVersionUID = -4394403246757791617L;
	private ScreenManager screenManager;
	private HeatmapLayerConfiguration config;
	private Envelope heatMapArea;
	private Envelope zoomArea;
	private double minValue;
	private double maxValue;
	private int totalNumberOfElement;
	private double totalValue;
	private LayerUpdater layerUpdater;

	public Heatmap(HeatmapLayerConfiguration configuration) {
		super(configuration);
		this.config = configuration;
		minValue = 0;
		maxValue = 0;
		zoomArea = new Envelope();
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema,
			SDFAttribute attribute) {
		this.screenManager = screenManager;
		this.srid = screenManager.getSRID();
	}

	@Override
	public void draw(GC gc) {
		Color minColor = config.getMinColor();
		Color maxColor = config.getMaxColor();
		Image img = createImage(config.getNumTilesWidth(),
				config.getNumTilesHeight(), minColor, maxColor);
		drawImage(gc, img, config.isInterpolation());
	}

	/**
	 * Draws an image in the given area
	 * 
	 * @param gc
	 * @param img
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 */
	private void drawImage(GC gc, Image img, boolean interpolation) {
		// Draw the picture transparent and without interpolation
		gc.setAlpha(config.getAlpha());
		if (!interpolation)
			gc.setInterpolation(SWT.NONE);
		gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height,
				(int) heatMapArea.getMinX(), (int) heatMapArea.getMinY(),
				(int) heatMapArea.getWidth(), (int) heatMapArea.getHeight());
		gc.setInterpolation(SWT.DEFAULT);
		gc.setAlpha(255);
	}

	/**
	 * Creates an image which can be drawn on the display Image has numberX *
	 * numberY pixels
	 * 
	 * @param numberX
	 *            pixels in x-direction
	 * @param numberY
	 *            pixels in y-direction
	 * @return Image for the heatmap-overlay with right colors
	 */
	private Image createImage(int numberX, int numberY, Color minColor,
			Color maxColor) {
		Image image = new Image(Display.getDefault(), numberX, numberY);
		GC gc = new GC(image);
		gc.setForeground(new org.eclipse.swt.graphics.Color(Display
				.getDefault(), 0, 0, 0));
		gc.setBackground(new org.eclipse.swt.graphics.Color(Display
				.getDefault(), 255, 0, 0));
		Color[][] colors = createColorArray(numberX, numberY, minColor,
				maxColor);

		for (int i = 0; i < colors.length; i++) {
			for (int j = 0; j < colors[i].length; j++) {
				gc.setBackground(colors[i][j]);
				gc.fillRectangle(i, j, 1, 1);
			}
		}

		return image;
	}

	private Color[][] createColorArray(int x, int y, Color minColor,
			Color maxColor) {
		Color[][] colors = new Color[x][y];

		// Fill with standard-grey
		for (int i = 0; i < colors.length; i++) {
			for (int j = 0; j < colors[i].length; j++) {
				colors[i][j] = new Color(Display.getDefault(), 100, 100, 100);
			}
		}

		// The tuples are now stored in the LayerUpdater to reduce redundant
		// data
		Envelope searchEnv = new Envelope();
		searchEnv.init(new Coordinate(49.7, 11.7)); // Maybe somewhere in
													// Germany
		// Get everything
		searchEnv.expandToInclude(new Coordinate(Integer.MIN_VALUE,
				Integer.MAX_VALUE)); // Top left
		searchEnv.expandToInclude(new Coordinate(Integer.MAX_VALUE,
				Integer.MIN_VALUE)); // Bottom right
		List<?> data = layerUpdater.query(searchEnv,
				config.getGeometricAttributePosition());
		int[][] valueSum = new int[x][y];
		int maxSum = 0;
		int minSum = 0;

		// Create heatMapArea with the given tuples
		// (We could let this out and give an area by the user, but
		// this seems to be more user-friendly)
		ScreenTransformation transformation = screenManager.getTransformation();
		int srid = config.getSrid();
		if (config.isAutoPosition()) {
			// If the user wants the layer to be auto-positioned
			heatMapArea = new Envelope();
			zoomArea = new Envelope();
			for (Object dataSet : data) {

				// Get the data from the Tuple (Where it is and value)
				Tuple<?> tuple = ((DataSet) dataSet).getTuple();
				GeometryCollection geoColl = (GeometryCollection) tuple
						.getAttribute(0);
				Point point = geoColl.getCentroid();

				// Calculate, where this belongs in the heatmap

				int[] transformedPoint = transformation.transformCoord(
						point.getCoordinate(), srid);

				Envelope tempEnv = new Envelope(new Coordinate(
						transformedPoint[0], transformedPoint[1]));

				// If this point is not in the heatMapArea -> expand heatMapArea
				heatMapArea.expandToInclude(tempEnv);
				zoomArea.expandToInclude(point.getCoordinate());
			}
		} else {
			// Position is set by the user
			// Transform the given area to the screen
			zoomArea = new Envelope(new Coordinate(config.getLngSW(),
					config.getLatSW()), new Coordinate(config.getLngNE(),
					config.getLatNE()));
			int[] southWest = transformation.transformCoord(new Coordinate(
					config.getLngSW(), config.getLatSW()), srid); // SouthWest
			int[] northEast = transformation.transformCoord(new Coordinate(
					config.getLngNE(), config.getLatNE()), srid); // northEast
			heatMapArea = new Envelope(southWest[0], northEast[0],
					southWest[1], northEast[1]);
		}

		// If we have just one point -> Expand, so we can see something
		// on the map
		if (heatMapArea.getWidth() == 0) {
			heatMapArea.expandBy(20);
		}

		totalNumberOfElement = data.size();
		int tempTotalValue = 0;
		for (Object dataSet : data) {

			// Get the data from the Tuple (Where it is and value)
			Tuple<?> tuple = ((DataSet) dataSet).getTuple();
			GeometryCollection geoColl = (GeometryCollection) tuple
					.getAttribute(config.getGeometricAttributePosition());
			Point point = geoColl.getCentroid();

			double value = 0;
			try {
				value = (int) tuple.getAttribute(config.getValueAttributePosition());
			} catch (ClassCastException e) {
				// Ok, not an int, then it's a double
				value = (double) tuple.getAttribute(config.getValueAttributePosition());
			}

			// Calculate, where this belongs in the heatmap
			int[] transformedPoint = transformation.transformCoord(
					point.getCoordinate(), srid);

			Envelope tempEnv = new Envelope(new Coordinate(transformedPoint[0],
					transformedPoint[1]));

			if (heatMapArea.covers(tempEnv)) {
				// Just calculate with this point if its in the area
				// (maybe the area the user gave us does not include every point
				// in the data)
				// +1 for the points which are exactly on the border
				// or if we just have a point as area (just one tuple)
				double partsWidth = (heatMapArea.getWidth() + 1) / x;
				int posX = (int) ((tempEnv.getMaxX() - heatMapArea.getMinX()) / partsWidth);

				double partsHeight = (heatMapArea.getHeight() + 1) / y;
				int posY = (int) ((tempEnv.getMaxY() - heatMapArea.getMinY()) / partsHeight);

				// Add the value to the sum of this tile in the heatmap
				valueSum[posX][posY] += value;
				// and to the total value
				tempTotalValue += value;

				// Get the maximum and minimum sum
				if (valueSum[posX][posY] > maxSum) {
					maxSum = valueSum[posX][posY];
					this.maxValue = maxSum;
				}

				if (valueSum[posX][posY] < minSum) {
					minSum = valueSum[posX][posY];
					this.minValue = minSum;
				}

			}

		}

		totalValue = tempTotalValue;

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
	 * 
	 * @param value
	 *            value of the tile you want to get the color for
	 * @param min
	 *            minimum value of the heatmap
	 * @param max
	 *            maximum value of the heatmap
	 * @param minColor
	 *            Color for the minimum value in the heatmap, e.g. green
	 * @param maxColor
	 *            Color for the maximum value in the heatmap, e.g. red
	 * @return Color of the value
	 */
	private Color getColorForValue(double value, double min, double max,
			Color minColor, Color maxColor) {

		// We don't want negative solutions
		if (min < 0) {
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

		// Ensure, that all values are allowed
		if (r < 0)
			r = 0;
		if (r > 255)
			r = 255;
		if (g < 0)
			g = 0;
		if (g > 255)
			g = 255;
		if (b < 0)
			b = 0;
		if (b > 255)
			b = 255;

		Color color = new Color(Display.getDefault(), (int) r, (int) g, (int) b);
		return color;
	}

	@Override
	public void setLayerUpdater(LayerUpdater layerUpdater) {
		this.layerUpdater = layerUpdater;
	}

	/**
	 * Returns the minimal value of all tiles
	 * 
	 * @return
	 */
	public double getMinValue() {
		return minValue;
	}

	/**
	 * Returns the maximal value of all tiles
	 * 
	 * @return
	 */
	public double getMaxValue() {
		return maxValue;
	}

	/**
	 * Returns the sum of all values of points which are in the chosen area
	 * 
	 * @return
	 */
	public double getTotalValue() {
		return totalValue;
	}

	/**
	 * Returns the total number of Elements (from the datastream) which are in
	 * the chosen area
	 * 
	 * @return
	 */
	public double getTotalNumberOfElements() {
		return totalNumberOfElement;
	}

	/**
	 * Returns the average value of an element
	 * 
	 * @return
	 */
	public double getAverageValueOfElement() {
		return totalValue / totalNumberOfElement;
	}

	/**
	 * Returns the average sum of an tile
	 * 
	 * @return
	 */
	public double getAverageValueOfTile() {
		return totalValue / getNumberOfTiles();
	}

	public double getNumberOfTiles() {
		return config.getNumTilesHeight() * config.getNumTilesWidth();
	}

	/**
	 * For "zoomToLayer"
	 */
	@Override
	public Envelope getEnvelope() {
		CoordinateTransform ct = screenManager.getTransformation().getCoordinateTransform(4326, this.srid);
		Envelope geoEnv = config.getCoverage();
		// Doesn't work - don't know why, I guess the transformation has a problem with 4326
//		if (zoomArea != null) {
//			geoEnv = zoomArea;
//		}
		ProjCoordinate src1 = new ProjCoordinate(geoEnv.getMaxX(), geoEnv.getMaxY());
		ProjCoordinate src2 = new ProjCoordinate(geoEnv.getMinX(), geoEnv.getMinY());
		ProjCoordinate dst1 = new ProjCoordinate();
		ProjCoordinate dst2 = new ProjCoordinate();
		ct.transform(src1, dst1);
		ct.transform(src2, dst2);
		Envelope coverage = new Envelope(dst1.x, dst2.x, dst1.y, dst2.y);
		
		return coverage;
	}

	/**
	 * Sets the configuration for this layer
	 * 
	 * @param config
	 */
	public void setConfiguration(HeatmapLayerConfiguration config) {
		this.config = config;
	}

	public HeatmapLayerConfiguration getConfig() {
		return config;
	}
}
