package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvZero;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.PolarGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class ToPolarGrid extends AbstractFunction<PolarGrid> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5134168940606752861L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFSpatialDatatype.SPATIAL_GEOMETRY,
					SDFSpatialDatatype.SPATIAL_POLYGON },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE } };

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
							+ " argument(s): A geometry, the x and y coordinates, the radius, the cellangle, and the cellradius.");
		} else {
			return accTypes[argPos];
		}
	}

	@Override
	public String getSymbol() {
		return "ToPolarGrid";
	}

	@Override
	public PolarGrid getValue() {
		final Geometry geometry = (Geometry) this.getInputValue(0);
		final Double x = this.getNumericalInputValue(1);
		final Double y = this.getNumericalInputValue(2);
		final Integer radius = this.getNumericalInputValue(3).intValue();
		final Double cellangle = this.getNumericalInputValue(4);
		final Double cellradius = this.getNumericalInputValue(5);

		final Coordinate[] coordinates = geometry.getCoordinates();

		final PolarGrid grid = new PolarGrid(new Coordinate(x, y), radius,
				cellangle, cellradius);
		IplImage image = IplImage.create(
				opencv_core.cvSize(grid.radius, grid.angle),
				opencv_core.IPL_DEPTH_64F, 1);

		cvZero(image);
		opencv_core.cvSet(image, OpenCVUtil.UNKNOWN);

		CvPoint point = new CvPoint(0, 0);
		for (int i = 0; i < coordinates.length; i++) {
			Coordinate coordinate = coordinates[i];
			double length = Math.sqrt(Math.pow(coordinate.x, 2)
					+ Math.pow(coordinate.y, 2));
			if (length <= radius) {
				double theta = Math.atan2(coordinate.y, coordinate.x);
				int indexR = (int) (length / cellradius);
				for (int r = 0; r <= indexR; r++) {
					point.put(r, image.height() - (int) (theta / cellangle));
					if (r < indexR) {
						cvRectangle(image, point, point, OpenCVUtil.OBSTACLE,
								CV_FILLED, 8, 0);
					} else {
						cvRectangle(image, point, point, OpenCVUtil.FREE,
								CV_FILLED, 8, 0);
					}
				}
			}
		}
		// for (int i = 0; i < coordinates.length; i++) {
		// Coordinate coordinate = coordinates[i];
		// double length = Math.sqrt(Math.pow(coordinate.x, 2)
		// + Math.pow(coordinate.y, 2));
		// double theta = Math.atan2(coordinate.y, coordinate.x);
		// grid.fill(255);
		// for (double r = 0.0; r <= length; r += cellradius) {
		// if (r >= length) {
		// grid.set(r, theta, 100);
		// } else {
		// grid.set(r, theta, 0);
		// }
		// }
		// }
		OpenCVUtil.imageToGrid(image, grid);

		image.release();
		image = null;
		return grid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.POLARGRID;
	}

}
