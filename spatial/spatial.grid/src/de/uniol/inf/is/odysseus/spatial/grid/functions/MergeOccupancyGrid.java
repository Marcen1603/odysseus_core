package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.cvFillPoly;
import static com.googlecode.javacv.cpp.opencv_core.cvFillConvexPoly;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;

import java.util.Arrays;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.spatial.grid.common.GridUtil;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.model.Distribution;
import de.uniol.inf.is.odysseus.spatial.grid.model.PolarGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;

public class MergeOccupancyGrid extends AbstractFunction<CartesianGrid> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2107668987337394612L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFSpatialDatatype.SPATIAL_POINT }, { SDFDatatype.DOUBLE },
			{ SDFSpatialDatatype.SPATIAL_POLAR_COORDINATE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.INTEGER }, { SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.STRING } };

	@Override
	public int getArity() {
		return 11;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(
					this.getSymbol()
							+ " has only "
							+ this.getArity()
							+ " argument(s): A geometry, the x and y coordinates, the width and height, and the cellsize.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "MergeOccupancyGrid";
	}

	@Override
	public CartesianGrid getValue() {
		final Coordinate polarGridOrigin = (Coordinate) this.getInputValue(0);
		final Double angle = this.getNumericalInputValue(1);
		final PolarCoordinate[] coordinates = (PolarCoordinate[]) this
				.getInputValue(2);
		final Double x = this.getNumericalInputValue(3);
		final Double y = this.getNumericalInputValue(4);
		Integer width = this.getNumericalInputValue(5).intValue();
		Integer depth = this.getNumericalInputValue(6).intValue();
		final Double cartesianGridCellsize = this.getNumericalInputValue(7);
		final Integer polarGridRadius = this.getNumericalInputValue(8)
				.intValue();
		final Double polarGridCellradius = this.getNumericalInputValue(9);
		String contextStoreName = this.getInputValue(10);

		CartesianGrid cartesianGrid = null;

		IContextStore<Tuple<? extends ITimeInterval>> contextStore;
		if (ContextStoreManager.storeExists(contextStoreName)) {
			contextStore = ContextStoreManager.getStore(contextStoreName);

			List<Tuple<? extends ITimeInterval>> values = contextStore
					.getLastValues();

			if ((!values.isEmpty()) && (values.size() > 0)
					&& (values.get(0) != null)) {
				Tuple<? extends ITimeInterval> value = values.get(0);
				cartesianGrid = value.getAttribute(0);
			}
			if (cartesianGrid == null) {
				cartesianGrid = createEmptyGrid(new Coordinate(x, y), width,
						depth, cartesianGridCellsize);
			}
		} else {
			cartesianGrid = createEmptyGrid(new Coordinate(x, y), width, depth,
					cartesianGridCellsize);
		}
		mergePolarGrid(cartesianGrid, polarGridOrigin, angle, coordinates,
				polarGridRadius, polarGridCellradius);

		return cartesianGrid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

	private CartesianGrid mergePolarGrid(CartesianGrid cartesianGrid,
			Coordinate polarGridOrigin, double transformAngle,
			PolarCoordinate[] coordinates, int radialCells, double cellRadius) {

		double offsetX = polarGridOrigin.x - cartesianGrid.origin.x;
		double offsetY = polarGridOrigin.y - cartesianGrid.origin.y;

		IplImage mergedImage = IplImage.create(
				opencv_core.cvSize(cartesianGrid.width, cartesianGrid.depth),
				opencv_core.IPL_DEPTH_64F, 1);

		IplImage image = IplImage.create(
				opencv_core.cvSize(cartesianGrid.width, cartesianGrid.depth),
				opencv_core.IPL_DEPTH_64F, 1);

		OpenCVUtil.gridToImage(cartesianGrid, mergedImage);
		OpenCVUtil.gridToImage(cartesianGrid, image);

		double cellAngle = Math.abs(coordinates[0].a - coordinates[3].a);
		PolarGrid polarGrid = new PolarGrid(polarGridOrigin, radialCells,
				cellAngle, cellRadius);
		polarGrid.fill(0.0);

		double polarRadius = polarGrid.radius * cellRadius;
		double[] jointDistribution = new double[polarGrid.radius];

		final CvRect roiRect = new CvRect();
		final CvRect maskRect = new CvRect();
		for (PolarCoordinate coordinate : coordinates) {
			if (coordinate.r > 0.0) {
				Arrays.fill(jointDistribution, 0.0);
				double probability = 1.0;
				double probabilitySum = 0.0;
				double theta = coordinate.a - transformAngle;
				if (theta < 0.0) {
					theta += 360.0;
				}
				if (theta >= 360.0) {
					theta -= ((int) (theta / 360.0)) * 360.0;
				}

				int thetaIndex = (int) (theta / cellAngle);

				double cosAngle = Math.cos(Math.toRadians(theta));
				double sinAngle = Math.sin(Math.toRadians(theta));

				double cosHalfAngle = Math.cos(Math.toRadians(theta + cellAngle
						/ 2));
				double sinHalfAngle = Math.sin(Math.toRadians(theta + cellAngle
						/ 2));

				double cosNextAngle = Math.cos(Math
						.toRadians(theta + cellAngle));
				double sinNextAngle = Math.sin(Math
						.toRadians(theta + cellAngle));

				for (int r = 0; r < polarGrid.radius; r++) {
					double radius = r * cellRadius;

					double value = polarGrid.get(r, thetaIndex);
					Coordinate p1 = new Coordinate(offsetX + radius * cosAngle,
							offsetY + radius * sinAngle);

					Coordinate p2 = new Coordinate(offsetX + radius
							* cosNextAngle, offsetY + radius * sinNextAngle);

					double tmpR = (radius + cellRadius) * cosHalfAngle;

					Coordinate p3 = new Coordinate(offsetX + tmpR
							* cosNextAngle, offsetY + tmpR * sinNextAngle);

					Coordinate p4 = new Coordinate(offsetX + tmpR * cosAngle,
							offsetY + tmpR * sinAngle);

					int gridX1 = (int) ((p1.x) / cartesianGrid.cellsize);
					int gridY1 = (int) ((p1.y) / cartesianGrid.cellsize);
					int gridX2 = (int) ((p2.x) / cartesianGrid.cellsize);
					int gridY2 = (int) ((p2.y) / cartesianGrid.cellsize);
					int gridX3 = (int) ((p3.x) / cartesianGrid.cellsize);
					int gridY3 = (int) ((p3.y) / cartesianGrid.cellsize);
					int gridX4 = (int) ((p4.x) / cartesianGrid.cellsize);
					int gridY4 = (int) ((p4.y) / cartesianGrid.cellsize);

					int minX = Math.min(Math.min(gridX1, gridX2),
							Math.min(gridX3, gridX4));
					int maxX = Math.max(Math.max(gridX1, gridX2),
							Math.max(gridX3, gridX4));
					int minY = Math.min(Math.min(gridY1, gridY2),
							Math.min(gridY3, gridY4));
					int maxY = Math.max(Math.max(gridY1, gridY2),
							Math.max(gridY3, gridY4));
					if ((minX == maxX) && (minY == maxY)) {
						value = Math.max(cartesianGrid.get(minX, minY), value);
					} else {

						roiRect.x(minX < 0 ? 0 : minX);
						roiRect.y(image.height() - minY < 0 ? 0 : image
								.height() - minY);
						int roiWidth = maxX - minX + 1;
						int roiHeight = maxY - minY + 1;

						roiWidth = (roiRect.x() + roiWidth) > image.width() ? image
								.width() - roiRect.x()
								: roiWidth;
						roiHeight = (roiRect.y() + roiHeight) > image.height() ? image
								.height() - roiRect.y()
								: roiHeight;
						roiRect.width(roiWidth);
						roiRect.height(roiHeight);
						maskRect.x(0);
						maskRect.y(0);
						maskRect.width(roiRect.width());
						maskRect.height(roiRect.height());
						if ((roiRect.width() > 0) && (roiRect.height() > 0)) {
							IplImage mask = IplImage.create(
									opencv_core.cvSize(roiRect.width(),
											roiRect.height()),
									opencv_core.IPL_DEPTH_8U, 1);

							opencv_core.cvZero(mask);
							CvPoint cell = new CvPoint(4);
							cell.position(0).x((int) (gridX1 - minX))
									.y(mask.height() - (int) (gridY1 - minY));
							cell.position(1).x((int) (gridX2 - minX))
									.y(mask.height() - (int) (gridY2 - minY));
							cell.position(2).x((int) (gridX3 - minX))
									.y(mask.height() - (int) (gridY3 - minY));
							cell.position(3).x((int) (gridX4 - minX))
									.y(mask.height() - (int) (gridY4 - minY));
							double[] minVal = new double[1];
							double[] maxVal = new double[1];
							CvPoint minLoc = new CvPoint();
							CvPoint maxLoc = new CvPoint();
							try {
								cvFillPoly(mask, cell, new int[] { 4 }, 1,
										opencv_core.cvScalarAll(255), 4, 0);
								opencv_core.cvSetImageROI(mask, maskRect);
								opencv_core.cvSetImageROI(image, roiRect);

								opencv_core.cvMinMaxLoc(image, minVal, maxVal,
										minLoc, maxLoc, mask);
							} catch (Exception e) {
								System.out.println("roi> " + roiRect
										+ "    mask > " + maskRect);
							}
							opencv_core.cvResetImageROI(image);
							opencv_core.cvResetImageROI(mask);
							value = Math.max(maxVal[0], value);
							minLoc.deallocate();
							maxLoc.deallocate();
							cell.deallocate();

							mask.release();
							mask = null;
						}
					}
					polarGrid.set(r, thetaIndex, value);

					jointDistribution[r] = probability * value
							* getProbability(coordinate.r, radius);
					probability *= (1.0 - value);
					probabilitySum += jointDistribution[r];
				}
				if (coordinate.r > polarRadius) {
					probabilitySum += (coordinate.r / cellRadius)
							- polarGrid.radius;
				}
				double totalProbability = 0.0;
				for (int r = 0; r < polarGrid.radius; r++) {
					double radius = r * cellRadius;

					double normalized = jointDistribution[r] / probabilitySum;

					double newValue = normalized + polarGrid.get(r, thetaIndex)
							* totalProbability;

					CvPoint cell = getPolarPoints(mergedImage,
							cartesianGrid.cellsize, offsetX, offsetY, radius,
							theta, cellRadius, cellAngle);
					cvFillPoly(mergedImage, cell, new int[] { 6 }, 1,
							opencv_core.cvScalarAll(newValue), 4, 0);
					cell.deallocate();

					totalProbability += normalized;
				}
			}
		}
		roiRect.deallocate();
		maskRect.deallocate();
		OpenCVUtil.imageToGrid(mergedImage, cartesianGrid);
		image.release();
		image = null;
		mergedImage.release();
		mergedImage = null;
		return cartesianGrid;
	}

	private CvPoint getPolarPoints(IplImage image, double cellsize,
			double offsetX, double offsetY, double radius, double angle,
			double cellRadius, double cellAngle) {

		CvPoint polarGridCell = new CvPoint(6);
		double cosAngle = Math.cos(Math.toRadians(angle));
		double sinAngle = Math.sin(Math.toRadians(angle));

		double cosHalfAngle = Math.cos(Math.toRadians(angle + cellAngle / 2));
		double sinHalfAngle = Math.sin(Math.toRadians(angle + cellAngle / 2));

		double cosNextAngle = Math.cos(Math.toRadians(angle + cellAngle));
		double sinNextAngle = Math.sin(Math.toRadians(angle + cellAngle));

		polarGridCell
				.position(0)
				.x((int) ((offsetX + radius * cosAngle) / cellsize))
				.y(image.height()
						- (int) ((offsetY + radius * sinAngle) / cellsize));

		polarGridCell
				.position(1)
				.x((int) ((offsetX + radius * cosHalfAngle) / cellsize))
				.y(image.height()
						- (int) ((offsetY + radius * sinHalfAngle) / cellsize));

		polarGridCell
				.position(2)
				.x((int) ((offsetX + radius * cosNextAngle) / cellsize))
				.y(image.height()
						- (int) ((offsetY + radius * sinNextAngle) / cellsize));

		polarGridCell
				.position(3)
				.x((int) ((offsetX + (radius + cellRadius) * cosNextAngle) / cellsize))
				.y(image.height()
						- (int) ((offsetY + (radius + cellRadius)
								* sinNextAngle) / cellsize));

		polarGridCell
				.position(4)
				.x((int) ((offsetX + (radius + cellRadius) * cosHalfAngle) / cellsize))
				.y(image.height()
						- (int) ((offsetY + (radius + cellRadius)
								* sinHalfAngle) / cellsize));

		polarGridCell
				.position(5)
				.x((int) ((offsetX + (radius + cellRadius) * cosAngle) / cellsize))
				.y(image.height()
						- (int) ((offsetY + (radius + cellRadius) * sinAngle) / cellsize));
		return polarGridCell;
	}

	private CartesianGrid createEmptyGrid(Coordinate coordinate, Integer width,
			Integer depth, Double cellsize) {
		CartesianGrid grid = new CartesianGrid(coordinate, width, depth,
				cellsize);
		grid.fill(0.5);
		return grid;
	}

	private double getProbability(double distance, double radius) {
		Distribution distribution = new Distribution(distance, 0.42);
		if ((Math.abs(distance - radius) <= 4.2) && (radius > 50)
				&& (radius < 5000)) {
			return 1.0 / 0.084;
		} else if (!((radius > 50) && (radius < 5000))) {
			return 1.0;
		} else {
			return 0.0;
			// return distribution.getValue(radius);
		}
	}

}
