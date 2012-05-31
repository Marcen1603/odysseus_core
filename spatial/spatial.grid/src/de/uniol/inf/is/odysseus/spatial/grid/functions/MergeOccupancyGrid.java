/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_core.cvFillPoly;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFilter2D;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.context.store.types.MultiElementStore;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Merge occupancy grids using bayesian updater
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
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
	private static final int THREADS = 6;
	private static final double MAX_VALUE = 0.999999;
	private static final double MIN_VALUE = 1.0 - MAX_VALUE;
	private static final double FREE = Math.log(1.0 - MIN_VALUE);
	private static final double UNKNOWN = Math.log(1.0 - 0.5);
	private static final double MAX_RANGE = Math.log(1.0 - MAX_VALUE) * 12.0;

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
		Map<Long, int[]> polarMapping = null;
		IContextStore<Tuple<? extends ITimeInterval>> contextStore;
		if (!ContextStoreManager.storeExists(contextStoreName)) {
			SDFSchema schema = new SDFSchema(
					"ContextStore:" + contextStoreName, new SDFAttribute(
							"contextStore", "grid", SDFGridDatatype.GRID),
					new SDFAttribute("contextStore", "map", SDFDatatype.OBJECT));
			IContextStore<Tuple<? extends ITimeInterval>> store = new MultiElementStore<Tuple<? extends ITimeInterval>>(
					contextStoreName, schema, 1);
			try {
				ContextStoreManager.addStore(contextStoreName, store);
			} catch (ContextManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		contextStore = ContextStoreManager.getStore(contextStoreName);

		List<? extends Tuple<? extends ITimeInterval>> values = contextStore
				.getAllValues();

		if ((!values.isEmpty()) && (values.size() > 0)
				&& (values.get(0) != null)) {
			Tuple<? extends ITimeInterval> value = values.get(0);
			cartesianGrid = value.getAttribute(0);
			polarMapping = value.getAttribute(1);

		} else {
			cartesianGrid = createEmptyGrid(new Coordinate(x, y), width, depth,
					cartesianGridCellsize);
			polarMapping = preCalcPolarToCartesianCoordinates(cartesianGrid,
					polarGridOrigin, angle, coordinates, polarGridRadius,
					polarGridCellradius);

		}

		CartesianGrid mergedGrid = mergePolarGrid(cartesianGrid,
				polarGridOrigin, angle, coordinates, polarGridRadius,
				polarGridCellradius, polarMapping);

		Object[] retObj = new Object[] { mergedGrid, polarMapping };
		Tuple<ITimeInterval> ret = new Tuple<ITimeInterval>(retObj);
		final TimeInterval metadata = new TimeInterval(new PointInTime(
				System.currentTimeMillis()));
		ret.setMetadata(metadata);
		contextStore.insertValue(ret);

		return mergedGrid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

	private Map<Long, int[]> preCalcPolarToCartesianCoordinates(
			CartesianGrid cartesianGrid, Coordinate polarGridOrigin,
			double transformAngle, PolarCoordinate[] coordinates,
			int radialCells, double cellRadius) {
		double offsetX = polarGridOrigin.x - cartesianGrid.origin.x;
		double offsetY = polarGridOrigin.y - cartesianGrid.origin.y;
		double cellAngle = Math.abs(coordinates[0].a - coordinates[1].a);
		Map<Long, int[]> polarToCartesianMapping = new HashMap<Long, int[]>();

		for (PolarCoordinate coordinate : coordinates) {
			double theta = getTheta(coordinate.a, transformAngle);

			double cosAngle = Math.cos(Math.toRadians(theta - (cellAngle / 2)));
			double sinAngle = Math.sin(Math.toRadians(theta - (cellAngle / 2)));

			double cosNextAngle = Math.cos(Math.toRadians(theta
					+ (cellAngle / 2)));
			double sinNextAngle = Math.sin(Math.toRadians(theta
					+ (cellAngle / 2)));

			int t = (int) (theta / cellAngle);
			for (int r = 0; r < radialCells; r++) {
				double radius = r * cellRadius;

				int gridX1 = (int) ((offsetX + (radius * cosAngle)) / cartesianGrid.cellsize);
				int gridY1 = (int) ((offsetY + (radius * sinAngle)) / cartesianGrid.cellsize);
				int gridX2 = (int) ((offsetX + (radius * cosNextAngle)) / cartesianGrid.cellsize);
				int gridY2 = (int) ((offsetY + (radius * sinNextAngle)) / cartesianGrid.cellsize);

				int gridX3 = (int) ((offsetX + (radius + cellRadius)
						* cosNextAngle) / cartesianGrid.cellsize);
				int gridY3 = (int) ((offsetY + (radius + cellRadius)
						* sinNextAngle) / cartesianGrid.cellsize);
				int gridX4 = (int) ((offsetX + (radius + cellRadius) * cosAngle) / cartesianGrid.cellsize);
				int gridY4 = (int) ((offsetY + (radius + cellRadius) * sinAngle) / cartesianGrid.cellsize);

				int minX = Math.min(Math.min(gridX1, gridX2),
						Math.min(gridX3, gridX4));
				int maxX = Math.max(Math.max(gridX1, gridX2),
						Math.max(gridX3, gridX4));
				int minY = Math.min(Math.min(gridY1, gridY2),
						Math.min(gridY3, gridY4));
				int maxY = Math.max(Math.max(gridY1, gridY2),
						Math.max(gridY3, gridY4));
				minX = minX < 0 ? 0 : minX;
				minY = minY < 0 ? 0 : minY;

				long key = (((long) t) << 32) | r;
				polarToCartesianMapping.put(key, new int[4]);
				int[] gridCoordinates = polarToCartesianMapping.get(key);

				gridCoordinates[0] = minX;
				gridCoordinates[1] = minY;
				gridCoordinates[2] = maxX;
				gridCoordinates[3] = maxY;
			}
		}
		return polarToCartesianMapping;
	}

	private CartesianGrid mergePolarGrid(CartesianGrid cartesianGrid,
			Coordinate polarGridOrigin, double transformAngle,
			PolarCoordinate[] coordinates, int radialCells, double cellRadius,
			Map<Long, int[]> polarMapping) {

		CvMat kernel = CvMat.create(3, 3, opencv_core.IPL_DEPTH_64F, 1);

		kernel.put(0, 0, 1.0);
		kernel.put(0, 1, 1.0);
		kernel.put(0, 2, 1.0);
		kernel.put(1, 0, 1.0);
		kernel.put(1, 1, 0.0);
		kernel.put(1, 2, 1.0);
		kernel.put(2, 0, 1.0);
		kernel.put(2, 1, 1.0);
		kernel.put(2, 2, 1.0);

		cvFilter2D(cartesianGrid.getImage(), cartesianGrid.getImage(), kernel,
				new CvPoint(-1, -1));
		kernel.deallocate();

		CartesianGrid mergedGrid = cartesianGrid.clone();

		double cellAngle = Math.abs(coordinates[0].a - coordinates[1].a);
		// double[] jointDistribution = new double[radialCells];
		// double beam[] = new double[radialCells];

		double offsetX = polarGridOrigin.x - cartesianGrid.origin.x;
		double offsetY = polarGridOrigin.y - cartesianGrid.origin.y;

		CvPoint resetPolar = new CvPoint(coordinates.length + 2);

		resetPolar.position(0).x((int) (offsetX / cartesianGrid.cellsize))
				.y((int) (offsetY / cartesianGrid.cellsize));

		for (int i = 0; i < coordinates.length; i++) {
			PolarCoordinate coordinate = coordinates[i];
			double theta = getTheta(coordinate.a, transformAngle);

			int t = (int) (theta / cellAngle);
			long polarKey = (((long) t) << 32)
					| ((coordinate.r / cartesianGrid.cellsize) < radialCells ? (int) (coordinate.r / cartesianGrid.cellsize)
							: radialCells - 1);
			resetPolar.position(i + 1).x(polarMapping.get(polarKey)[2])
					.y(polarMapping.get(polarKey)[3]);
		}
		resetPolar.position(coordinates.length + 1)
				.x((int) (offsetX / cartesianGrid.cellsize))
				.y((int) (offsetY / cartesianGrid.cellsize));

		cvFillPoly(mergedGrid.getImage(), resetPolar,
				new int[] { coordinates.length + 2 }, 1,
				opencv_core.cvScalarAll(FREE / MAX_RANGE), 4, 0);
		resetPolar.deallocate();

		int num = coordinates.length / THREADS;
		ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);
		for (int i = 0; i < coordinates.length; i += num) {
			threadPool.execute(new UpdateGridTask(cartesianGrid, mergedGrid,
					transformAngle, Arrays.copyOfRange(coordinates, i,
							Math.min(coordinates.length, i + num)),
					radialCells, cellRadius, polarMapping, cellAngle));
		}

		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mergedGrid.set(polarGridOrigin.x - cartesianGrid.origin.x,
				polarGridOrigin.y - cartesianGrid.origin.y, FREE / MAX_RANGE);

		for (int x = 0; x < mergedGrid.width; x++) {
			for (int y = 0; y < mergedGrid.height; y++) {
				double value = mergedGrid.get(x, y);
				if (value > MAX_VALUE) {
					mergedGrid.set(x, y, MAX_VALUE);
				} else if (value < MIN_VALUE) {
					mergedGrid.set(x, y, MIN_VALUE);
				}
			}
		}
		return mergedGrid;
	}

	private CartesianGrid createEmptyGrid(Coordinate coordinate, Integer width,
			Integer height, Double cellsize) {
		CartesianGrid grid = new CartesianGrid(coordinate, width, height,
				cellsize);
		grid.fill(UNKNOWN / MAX_RANGE);
		return grid;
	}

	private double getProbability(double distance, double radius) {
		if ((Math.abs(distance - radius) <= 4.2) && (radius > 50)
				&& (radius < 5000)) {
			return 1.0 / 0.084;
		} else if ((radius > 5000)) {
			return 0.5;
		} else {
			return 0.0;
		}
	}

	private double getTheta(double angle, double transformAngle) {
		double theta = angle - transformAngle;
		if (theta < 0.0) {
			theta += 360.0;
		}
		if (theta >= 360.0) {
			theta -= ((int) (theta / 360.0)) * 360.0;
		}
		return theta;
	}

	private synchronized void updateCell(int minX, int minY, int maxX,
			int maxY, CartesianGrid grid, double value) {
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				double oldValue = 1.0 - Math.exp(grid.get(x, y) * MAX_RANGE);

				grid.set(x, y, Math.log(1.0 - Math.max(value, oldValue))
						/ MAX_RANGE);
			}
		}
	}

	private class UpdateGridTask implements Runnable {
		private PolarCoordinate[] coordinates;
		private final int radialCells;
		private final double cellRadius;
		private final double transformAngle;
		private final double cellAngle;
		private final Map<Long, int[]> polarMapping;
		private final CartesianGrid cartesianGrid;
		private final CartesianGrid mergedGrid;

		public UpdateGridTask(CartesianGrid cartesianGrid,
				CartesianGrid mergedGrid, double transformAngle,
				PolarCoordinate[] coordinates, int radialCells,
				double cellRadius, Map<Long, int[]> polarMapping,
				double cellAngle) {
			this.radialCells = radialCells;
			this.cellRadius = cellRadius;
			this.transformAngle = transformAngle;
			this.polarMapping = polarMapping;
			this.cartesianGrid = cartesianGrid;
			this.mergedGrid = mergedGrid;
			this.cellAngle = cellAngle;
			this.coordinates = coordinates;

		}

		@Override
		public void run() {
			double[] jointDistribution = new double[this.radialCells];
			double beam[] = new double[this.radialCells];
			for (PolarCoordinate coordinate : this.coordinates) {
				if (coordinate.r > 43.8) {
					Arrays.fill(jointDistribution, 0.0);
					double probability = 1.0;
					double probabilitySum = 0.0;

					double theta = getTheta(coordinate.a, this.transformAngle);

					int t = (int) (theta / this.cellAngle);
					for (int r = 0; r < this.radialCells; r++) {
						double radius = r * this.cellRadius;

						double value = 0.0;

						long key = (((long) t) << 32) | r;
						int[] gridCoordinates = this.polarMapping.get(key);

						int minX = gridCoordinates[0];
						int minY = gridCoordinates[1];
						int maxX = gridCoordinates[2];
						int maxY = gridCoordinates[3];

						for (int x = minX; x <= maxX; x++) {
							for (int y = minY; y <= maxY; y++) {
								value = Math.max(
										value,
										1.0 - Math.exp(this.cartesianGrid.get(
												x, y) * MAX_RANGE));
							}
						}
						beam[r] = value;
						jointDistribution[r] = probability * value
								* getProbability(coordinate.r, radius);
						probability *= (1.0 - value);
						probabilitySum += jointDistribution[r];
					}
					double totalProbability = 0.0;
					for (int r = 0; r < this.radialCells; r++) {
						double radius = r * this.cellRadius;
						if (radius <= coordinate.r) {
							double normalized;
							if (probabilitySum == 0.0) {
								normalized = 0.0;
							} else {
								normalized = jointDistribution[r]
										/ probabilitySum;
							}
							double newValue = normalized + beam[r]
									* totalProbability;
							if (newValue > MAX_VALUE) {
								newValue = MAX_VALUE;
							}
							if (newValue < MIN_VALUE) {
								newValue = MIN_VALUE;
							}
							long key = (((long) t) << 32) | r;
							int[] gridCoordinates = this.polarMapping.get(key);

							int minX = gridCoordinates[0];
							int minY = gridCoordinates[1];
							int maxX = gridCoordinates[2];
							int maxY = gridCoordinates[3];
							updateCell(minX, minY, maxX, maxY, mergedGrid,
									newValue);
							totalProbability += normalized;
						} else {
							break;
						}
					}
				}
			}
		}
	}
}
