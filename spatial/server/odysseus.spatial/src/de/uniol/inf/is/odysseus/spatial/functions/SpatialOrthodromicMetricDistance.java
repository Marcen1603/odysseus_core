package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.spatial.utilities.MetricSpatialUtils;

public class SpatialOrthodromicMetricDistance extends AbstractFunction<Double> {

	private static final long serialVersionUID = 5069131276988060986L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSpatialDatatype.SPATIAL_POINT },
			{ SDFSpatialDatatype.SPATIAL_POINT } };

	public SpatialOrthodromicMetricDistance() {
		super("SpatialOrthodromicMetricDistance", 2, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		Point spatialPoint1 = ((GeometryWrapper) this.getInputValue(0)).getGeometry().getCentroid();
		Point spatialPoint2 = ((GeometryWrapper) this.getInputValue(1)).getGeometry().getCentroid();
		double distance = MetricSpatialUtils.getInstance().calculateDistance(spatialPoint1.getCoordinate(),
				spatialPoint2.getCoordinate());
		return distance;
	}

}
