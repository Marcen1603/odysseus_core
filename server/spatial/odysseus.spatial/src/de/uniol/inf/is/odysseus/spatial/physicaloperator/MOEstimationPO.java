package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.GeoHashMODataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOEstimationAO;

public class MOEstimationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private static final int DATA_PORT = 0;
	private static final int ENRICH_PORT = 1;

	private int pointInTimePosition;
	private IMovingObjectDataStructure index;

	private int idAttributeIndex;
	private int geometryAttributeIndex;

	public MOEstimationPO(MOEstimationAO ao) {
		this.geometryAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getGeometryAttribute());
		this.idAttributeIndex = ao.getInputSchema(DATA_PORT).findAttributeIndex(ao.getIdAttribute());
		this.pointInTimePosition = ao.getInputSchema(ENRICH_PORT).findAttributeIndex(ao.getPointInTimeAttribute());

		// TODO Name and "length" is not correct here.
		this.index = new GeoHashMODataStructure("EstimationPO" + this.hashCode(), this.geometryAttributeIndex, 1000);
	}

	@Override
	protected void process_next(T object, int port) {
		if (port == 0) {
			processTrajectoryTuple(object);
		} else if (port == 1) {
			processTimeTuple(object);
		}
	}

	private void processTrajectoryTuple(T object) {
		Geometry geometry = ((GeometryWrapper) object.getAttribute(this.geometryAttributeIndex)).getGeometry();
		String id = "";
		if (object.getAttribute(this.idAttributeIndex) instanceof Long) {
			id = String.valueOf((Long) object.getAttribute(this.idAttributeIndex));
		} else if (object.getAttribute(this.idAttributeIndex) instanceof Integer) {
			id = String.valueOf((Integer) object.getAttribute(this.idAttributeIndex));
		} else if (object.getAttribute(this.idAttributeIndex) instanceof String) {
			id = (String) object.getAttribute(this.idAttributeIndex);
		}

		LocationMeasurement locationMeasurement = new LocationMeasurement(geometry.getCoordinate().x,
				geometry.getCoordinate().y, 0, 0, object.getMetadata().getStart(), id);
		this.index.add(locationMeasurement, object);
	}

	@SuppressWarnings("unchecked")
	private void processTimeTuple(T object) {

		// Get the point in time to which the moving objects need to be
		// predicted
		long pointInTime = 0;
		if (object.getAttribute(this.pointInTimePosition) instanceof Long) {
			pointInTime = object.getAttribute(this.pointInTimePosition);
		}

		// As a first attempt get a list with all known moving objects
		List<String> allIds = new ArrayList<>();
		allIds.addAll(this.index.getAllMovingObjectIds());

		// And put out a tuple with the name of the dataStructure
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, pointInTime);
		tuple.setAttribute(1, allIds);
		tuple.setMetadata(object.getMetadata());
		transfer((T) tuple);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Clean up index
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
