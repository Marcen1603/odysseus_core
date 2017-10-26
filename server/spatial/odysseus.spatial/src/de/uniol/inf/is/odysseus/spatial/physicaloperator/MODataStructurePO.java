package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectIndexOld;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.datatype.LocationMeasurement;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MODataStructureAO;

public class MODataStructurePO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private MovingObjectIndexOld dataStructure;

	private int geometryPosition;
	private int idPosition;

	private String geometryAttribute;
	private String idAttribute;

	public MODataStructurePO(MODataStructureAO ao) {
		this.geometryAttribute = ao.getGeometryAttribute();
		this.idAttribute = ao.getIdAttribute();

		this.geometryPosition = ao.getInputSchema().findAttributeIndex(this.geometryAttribute);
		this.idPosition = ao.getInputSchema().findAttributeIndex(this.idAttribute);
		this.dataStructure = MovingObjectDataStructureProvider.getInstance().getOrCreateDataStructure(
				ao.getDataStructureName(), ao.getDataStructureType(), geometryPosition,
				ao.getDistancePerMovingObject());
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void process_next(T object, int port) {

		double latitude = 0;
		double longitude = 0;
		String id = "";

		if (object.isSchemaLess()) {
			// Probably a KeyValue object
			if (object instanceof KeyValueObject) {
				KeyValueObject objectAsKeyValue = (KeyValueObject) object;
				Geometry geometry = ((GeometryWrapper) objectAsKeyValue.getAttribute(this.geometryAttribute))
						.getGeometry();
				// We always use lat / lng
				latitude = geometry.getCentroid().getX();
				longitude = geometry.getCentroid().getY();

				id = (String) objectAsKeyValue.getAttribute(this.idAttribute);
			}
		} else {
			// We have a schema, use it
			if (object instanceof Tuple) {
				Tuple objectAsTuple = (Tuple) object;
				if (objectAsTuple.getAttribute(this.geometryPosition) instanceof GeometryWrapper) {
					Geometry geometry = ((GeometryWrapper) objectAsTuple.getAttribute(this.geometryPosition))
							.getGeometry();

					// We always use lat / lng
					latitude = geometry.getCentroid().getX();
					longitude = geometry.getCentroid().getY();
				}

				Object idObject = objectAsTuple.getAttribute(this.idPosition);
				if (idObject instanceof Long) {
					id = String.valueOf((Long) idObject);
				} else {
					id = (String) objectAsTuple.getAttribute(this.idPosition);
				}

			}
		}

		LocationMeasurement locationMeasurement = new LocationMeasurement(latitude, longitude, 0, 0,
				object.getMetadata().getStart(), id);

		// Save the tuple in a dataStructure
		this.dataStructure.add(locationMeasurement, object);

		// And put out a tuple with the name of the dataStructure
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
		tuple.setAttribute(0, dataStructure.getName());
		tuple.setMetadata(object.getMetadata());
		if (tuple instanceof IStreamObject<?>)
			transfer((T) tuple);
	}

}
