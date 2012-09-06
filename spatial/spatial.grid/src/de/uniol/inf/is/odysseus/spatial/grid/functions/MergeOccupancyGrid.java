/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.grid.common.GridUtil;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Distribution;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Merge occupancy grids using bayesian updater
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MergeOccupancyGrid extends AbstractFunction<Grid> {
    /**
	 * 
	 */
    private static final long                  serialVersionUID = 2107668987337394612L;
    public static final SDFDatatype[][]        accTypes         = new SDFDatatype[][] { { SDFGridDatatype.GRID },
            { SDFSpatialDatatype.SPATIAL_POLAR_COORDINATE }, { SDFSpatialDatatype.SPATIAL_COORDINATE },
            { SDFDatatype.DOUBLE }, { SDFDatatype.INTEGER }, { SDFDatatype.DOUBLE } };
    private static final int                   THREADS          = Runtime.getRuntime().availableProcessors();
    private static final Map<Double, Double[]> lookup           = new HashMap<Double, Double[]>();

    @Override
    public int getArity() {
        return 6;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
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
    public Grid getValue() {
        Grid cartesianGrid = (Grid) this.getInputValue(0);
        final PolarCoordinate[] coordinates = (PolarCoordinate[]) this.getInputValue(1);
        final Coordinate origin = (Coordinate) this.getInputValue(2);
        final Double angle = this.getNumericalInputValue(3);
        final Integer radius = this.getNumericalInputValue(4).intValue();
        final Double cellradius = this.getNumericalInputValue(5);

        Grid mergedGrid = mergePolarGrid(cartesianGrid, origin, angle, coordinates, radius, cellradius);

        return mergedGrid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFGridDatatype.GRID;
    }

    private Grid mergePolarGrid(Grid grid, Coordinate polarGridOrigin, double transformAngle,
            PolarCoordinate[] coordinates, int radialCells, double cellRadius) {

        Grid mergedGrid = grid.clone();
        IplImage image = OpenCVUtil.gridToImage(mergedGrid);

        double cellAngle = Math.abs(coordinates[0].a - coordinates[1].a);
        double offsetX = polarGridOrigin.x - grid.origin.x;
        double offsetY = polarGridOrigin.y - grid.origin.y;

        // Set scan area in the mergedGrid to free
        CvPoint resetPolar = new CvPoint(coordinates.length + 2);
        resetPolar.position(0).x((int) (offsetX / grid.cellsize)).y((int) (offsetY / grid.cellsize));
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
        resetPolar.position(coordinates.length + 1).x((int) (offsetX / grid.cellsize))
                .y((int) (offsetY / grid.cellsize));

        cvFillPoly(image, resetPolar, new int[] { coordinates.length + 2 }, 1, opencv_core.cvScalarAll(GridUtil.FREE),
                4, 0);
        resetPolar.deallocate();

        // Calculate the probability for each laser beam in parallel
        int num = coordinates.length / THREADS;
        ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);
        OpenCVUtil.imageToGrid(image, mergedGrid);
        for (int i = 0; i < coordinates.length; i += num) {
            threadPool.execute(new UpdateGridTask(grid, mergedGrid, polarGridOrigin, transformAngle, Arrays
                    .copyOfRange(coordinates, i, Math.min(coordinates.length, i + num)), radialCells, cellRadius,
                    cellAngle));
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Set the position of the laser scanner as free
        int originX = (int) (offsetX / grid.cellsize);
        int originY = (int) (offsetY / grid.cellsize);
        if ((originX >= 0) && (originX < grid.width) && (originY >= 0) && (originY < grid.height)) {
            mergedGrid.set(originX, originY, GridUtil.FREE);
        }
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
        Distribution distribution = new Distribution(distance, 4.2);
        return distribution.getValue(radius);
        // if ((Math.abs(distance - radius) <= 4.2) && (radius > 50)
        // && (radius < 5000)) {
        // return 1.0 / 0.084;
        // } else if ((radius > 5000)) {
        // return 0.5;
        // } else {
        // return 0.0;
        // }
    }

    /**
     * Get the angle in the range of [0,360] degree
     * 
     * @param angle
     * @param transformAngle
     * @return
     */
    private double getTheta(double angle, double transformAngle) {
        double theta = angle + transformAngle;
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
    private synchronized void updateCell(int fromX, int toX, int fromY, int toY, Grid grid, double value) {
        for (int y = fromY; y <= toY; y++) {
            for (int x = fromX; x <= toX; x++) {
                double oldValue = grid.get(x, y);
                double maxValue = Math.max(value, oldValue);
                if (maxValue > GridUtil.MAX_VALUE) {
                    maxValue = GridUtil.MAX_VALUE;
                }
                if (maxValue < GridUtil.MIN_VALUE) {
                    maxValue = GridUtil.MIN_VALUE;
                }
                grid.set(x, y, maxValue);
            }
        }
    }

    /**
     * Runnable task to update the cartesian grid with the new scan from the
     * laser scanner
     * 
     * @author Christian Kuka
     */
    private class UpdateGridTask implements Runnable {
        private PolarCoordinate[] coordinates;
        private final int         radialCells;
        private final double      cellRadius;
        private final double      transformAngle;
        private final double      cellAngle;

        private final Grid        cartesianGrid;
        private final Grid        mergedGrid;
        private final Coordinate  origin;

        public UpdateGridTask(Grid grid, Grid mergedGrid, Coordinate origin, double transformAngle,
                PolarCoordinate[] coordinates, int radialCells, double cellRadius, double cellAngle) {
            this.radialCells = radialCells;
            this.cellRadius = cellRadius;
            this.transformAngle = transformAngle;
            this.cartesianGrid = grid;
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
                if (coordinate.r > cartesianGrid.cellsize / 2) {
                    Arrays.fill(jointDistribution, 0.0);
                    double probability = 1.0;
                    double probabilitySum = 0.0;

                    double theta = getTheta(coordinate.a, this.transformAngle);
                    synchronized (lookup) {
                        if (!lookup.containsKey(theta)) {
                            lookup.put(
                                    theta,
                                    new Double[] { Math.cos(Math.toRadians(theta - (cellAngle / 2))),
                                            Math.sin(Math.toRadians(theta - (cellAngle / 2))),
                                            Math.cos(Math.toRadians(theta + (cellAngle / 2))),
                                            Math.sin(Math.toRadians(theta + (cellAngle / 2))) });
                        }
                    }
                    Double[] angles = lookup.get(theta);
                    double cosAngle = angles[0];
                    double sinAngle = angles[1];
                    double cosNextAngle = angles[2];
                    double sinNextAngle = angles[3];

                    int[][] polarMapping = new int[this.radialCells][4];
                    for (int r = 0; r < this.radialCells; r++) {
                        double radius = r * this.cellRadius;
                        double value = 0.0;

                        int gridX1 = (int) ((offsetX + (radius * cosAngle)) / cartesianGrid.cellsize);
                        int gridY1 = (int) ((offsetY + (radius * sinAngle)) / cartesianGrid.cellsize);
                        int gridX2 = (int) ((offsetX + (radius * cosNextAngle)) / cartesianGrid.cellsize);
                        int gridY2 = (int) ((offsetY + (radius * sinNextAngle)) / cartesianGrid.cellsize);

                        int gridX3 = (int) ((offsetX + (radius + cellRadius) * cosNextAngle) / cartesianGrid.cellsize);
                        int gridY3 = (int) ((offsetY + (radius + cellRadius) * sinNextAngle) / cartesianGrid.cellsize);
                        int gridX4 = (int) ((offsetX + (radius + cellRadius) * cosAngle) / cartesianGrid.cellsize);
                        int gridY4 = (int) ((offsetY + (radius + cellRadius) * sinAngle) / cartesianGrid.cellsize);

                        int minX = Math.min(Math.min(gridX1, gridX2), Math.min(gridX3, gridX4));
                        int maxX = Math.max(Math.max(gridX1, gridX2), Math.max(gridX3, gridX4));
                        int minY = Math.min(Math.min(gridY1, gridY2), Math.min(gridY3, gridY4));
                        int maxY = Math.max(Math.max(gridY1, gridY2), Math.max(gridY3, gridY4));
                        minX = minX < 0 ? 0 : minX;
                        minY = minY < 0 ? 0 : minY;

                        maxX = maxX >= cartesianGrid.width ? cartesianGrid.width - 1 : maxX;
                        maxY = maxY >= cartesianGrid.height ? cartesianGrid.height - 1 : maxY;

                        for (int x = minX; x <= maxX; x++) {
                            for (int y = minY; y <= maxY; y++) {
                                value = Math.max(value, this.cartesianGrid.get(x, y));
                            }
                        }
                        beam[r] = value;
                        jointDistribution[r] = probability * value * getProbability(coordinate.r, radius);
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
                        if (radius <= coordinate.r + cartesianGrid.cellsize) {
                            double normalized;
                            if (probabilitySum == 0.0) {
                                normalized = 0.0;
                            }
                            else {
                                normalized = jointDistribution[r] / probabilitySum;
                            }
                            double newValue = normalized + beam[r] * totalProbability;
                            int minX = polarMapping[r][0];
                            int minY = polarMapping[r][1];
                            int maxX = polarMapping[r][2];
                            int maxY = polarMapping[r][3];

                            updateCell(minX, maxX, minY, maxY, mergedGrid, newValue);

                            totalProbability += normalized;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
    }
}
