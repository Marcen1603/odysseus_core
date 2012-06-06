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

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.grid.common.GridUtil;
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
			{ SDFGridDatatype.GRID },
			{ SDFSpatialDatatype.SPATIAL_POLAR_COORDINATE },
			{ SDFSpatialDatatype.SPATIAL_COORDINATE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.INTEGER }, { SDFDatatype.DOUBLE } };
	private static final int THREADS = Runtime.getRuntime()
			.availableProcessors() * 2 + 1;

	@Override
	public int getArity() {
		return 6;
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
							+ " argument(s): A cartesian grid, the polar coordinates, the origin, the transform angle, the polar radius, and the polar cellsize.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "MergeOccupancyGrid";
	}

	@Override
	public CartesianGrid getValue() {
		CartesianGrid cartesianGrid = (CartesianGrid) this.getInputValue(0);
		final PolarCoordinate[] coordinates = (PolarCoordinate[]) this
				.getInputValue(1);
		final Coordinate origin = (Coordinate) this.getInputValue(2);
		final Double angle = this.getNumericalInputValue(3);
		final Integer radius = this.getNumericalInputValue(4).intValue();
		final Double cellradius = this.getNumericalInputValue(5);

		CartesianGrid mergedGrid = mergePolarGrid(cartesianGrid, origin, angle,
				coordinates, radius, cellradius);
		cartesianGrid.release();
		return mergedGrid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

	private CartesianGrid mergePolarGrid(CartesianGrid grid,
			Coordinate polarGridOrigin, double transformAngle,
			PolarCoordinate[] coordinates, int radialCells, double cellRadius) {

		CartesianGrid mergedGrid = grid.clone();

		double cellAngle = Math.abs(coordinates[0].a - coordinates[1].a);
		double offsetX = polarGridOrigin.x - grid.origin.x;
		double offsetY = polarGridOrigin.y - grid.origin.y;

		// Set scan area in the mergedGrid to free
		CvPoint resetPolar = new CvPoint(coordinates.length + 2);

		resetPolar.position(0).x((int) (offsetX / grid.cellsize))
				.y((int) (offsetY / grid.cellsize));
		for (int i = 0; i < coordinates.length; i++) {
			PolarCoordinate coordinate = coordinates[i];
			double theta = getTheta(coordinate.a, transformAngle);
			double radius = radialCells * cellRadius;
			if (radius > coordinate.r) {
				radius = coordinate.r;
			}
			int x = (int) ((offsetX + (radius * Math.cos(Math.toRadians(theta)))) / grid.cellsize);
			int y = (int) ((offsetY + (radius * Math.sin(Math.toRadians(theta)))) / grid.cellsize);

			resetPolar.position(i + 1).x(x).y(y);
		}
		resetPolar.position(coordinates.length + 1)
				.x((int) (offsetX / grid.cellsize))
				.y((int) (offsetY / grid.cellsize));

		cvFillPoly(mergedGrid.getImage(), resetPolar,
				new int[] { coordinates.length + 2 }, 1,
				opencv_core.cvScalarAll(GridUtil.FREE), 4, 0);
		resetPolar.deallocate();

		// Calculate the probability for each laser beam in parallel
		int num = coordinates.length / THREADS;
		ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);
		for (int i = 0; i < coordinates.length; i += num) {
			threadPool.execute(new UpdateGridTask(grid, mergedGrid,
					polarGridOrigin, transformAngle, Arrays.copyOfRange(
							coordinates, i,
							Math.min(coordinates.length, i + num)),
					radialCells, cellRadius, cellAngle));
		}

		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set the position of the laser scanner as free
		mergedGrid.set((int) (offsetX / grid.cellsize),
				(int) (offsetY / grid.cellsize), GridUtil.FREE);
		return mergedGrid;
	}

	/**
	 * Get the variance of the laser beam depending on the distance to radius
	 * 
	 * @param distance
	 * @param radius
	 * @return
	 */
	private double getProbability(double distance, double radius) {
		// Distribution distribution = new Distribution(distance, 4.2);
		if ((Math.abs(distance - radius) <= 4.2) && (radius > 50)
				&& (radius < 5000)) {
			return 1.0 / 0.084;
		} else if ((radius > 5000)) {
			return 0.5;
		} else {
			return 0.0;
		}
	}

	/**
	 * Get the angle in the range of [0,360] degree
	 * 
	 * @param angle
	 * @param transformAngle
	 * @return
	 */
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

	/**
	 * Update the given cell with the maximum of the given value and the
	 * existing value in the cartesian grid
	 * 
	 * @param x
	 * @param y
	 * @param grid
	 * @param value
	 */
	private synchronized void updateCell(int x, int y, CartesianGrid grid,
			double value) {
		double oldValue = 1.0 - Math.exp(-grid.get(x, y));
		double logValue = Math.log(1.0 - Math.max(value, oldValue));
		grid.set(x, y, -logValue);

	}

	/**
	 * Runnable task to update the cartesian grid with the new scan from the
	 * laser scanner
	 * 
	 * @author Christian Kuka
	 * 
	 */
	private class UpdateGridTask implements Runnable {
		private PolarCoordinate[] coordinates;
		private final int radialCells;
		private final double cellRadius;
		private final double transformAngle;
		private final double cellAngle;

		private final CartesianGrid cartesianGrid;
		private final CartesianGrid mergedGrid;
		private final Coordinate origin;

		public UpdateGridTask(CartesianGrid cartesianGrid,
				CartesianGrid mergedGrid, Coordinate origin,
				double transformAngle, PolarCoordinate[] coordinates,
				int radialCells, double cellRadius, double cellAngle) {
			this.radialCells = radialCells;
			this.cellRadius = cellRadius;
			this.transformAngle = transformAngle;
			this.cartesianGrid = cartesianGrid;
			this.mergedGrid = mergedGrid;
			this.cellAngle = cellAngle;
			this.coordinates = coordinates;
			this.origin = origin;

		}

		@Override
		public void run() {
			double[] jointDistribution = new double[this.radialCells];
			double beam[] = new double[this.radialCells];
			double offsetX = origin.x - cartesianGrid.origin.x;
			double offsetY = origin.y - cartesianGrid.origin.y;
			for (PolarCoordinate coordinate : this.coordinates) {
				Arrays.fill(jointDistribution, 0.0);
				double probability = 1.0;
				double probabilitySum = 0.0;

				double theta = getTheta(coordinate.a, this.transformAngle);

				double cosAngle = Math.cos(Math.toRadians(theta
						- (cellAngle / 2)));
				double sinAngle = Math.sin(Math.toRadians(theta
						- (cellAngle / 2)));

				double cosNextAngle = Math.cos(Math.toRadians(theta
						+ (cellAngle / 2)));
				double sinNextAngle = Math.sin(Math.toRadians(theta
						+ (cellAngle / 2)));

				int[][] polarMapping = new int[this.radialCells][4];
				for (int r = 0; r < this.radialCells; r++) {
					double radius = r * this.cellRadius;
					double value = 0.0;

					int gridX1 = (int) ((offsetX + (radius * cosAngle)) / cartesianGrid.cellsize);
					int gridY1 = (int) ((offsetY + (radius * sinAngle)) / cartesianGrid.cellsize);
					int gridX2 = (int) ((offsetX + (radius * cosNextAngle)) / cartesianGrid.cellsize);
					int gridY2 = (int) ((offsetY + (radius * sinNextAngle)) / cartesianGrid.cellsize);

					int gridX3 = (int) ((offsetX + (radius + cellRadius)
							* cosNextAngle) / cartesianGrid.cellsize);
					int gridY3 = (int) ((offsetY + (radius + cellRadius)
							* sinNextAngle) / cartesianGrid.cellsize);
					int gridX4 = (int) ((offsetX + (radius + cellRadius)
							* cosAngle) / cartesianGrid.cellsize);
					int gridY4 = (int) ((offsetY + (radius + cellRadius)
							* sinAngle) / cartesianGrid.cellsize);

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

					for (int x = minX; x <= maxX; x++) {
						for (int y = minY; y <= maxY; y++) {
							value = Math.max(value, 1.0 - Math
									.exp(-this.cartesianGrid.get(x, y)));
						}
					}
					beam[r] = value;
					jointDistribution[r] = probability * value
							* getProbability(coordinate.r, radius);
					probability *= (1.0 - value);
					probabilitySum += jointDistribution[r];

					polarMapping[r][0] = minX;
					polarMapping[r][1] = minY;
					polarMapping[r][2] = maxX;
					polarMapping[r][3] = maxY;
				}
				double totalProbability = 0.0;
				for (int r = 0; r < this.radialCells; r++) {
					double radius = r * this.cellRadius;
					if (radius <= coordinate.r) {
						double normalized;
						if (probabilitySum == 0.0) {
							normalized = 0.0;
						} else {
							normalized = jointDistribution[r] / probabilitySum;
						}
						double newValue = normalized + beam[r]
								* totalProbability;
						int minX = polarMapping[r][0];
						int minY = polarMapping[r][1];
						int maxX = polarMapping[r][2];
						int maxY = polarMapping[r][3];
						for (int x = minX; x <= maxX; x++) {
							for (int y = minY; y <= maxY; y++) {
								updateCell(x, y, mergedGrid, newValue);
							}
						}
						totalProbability += normalized;
					} else {
						break;
					}
				}

			}
		}
	}
}
