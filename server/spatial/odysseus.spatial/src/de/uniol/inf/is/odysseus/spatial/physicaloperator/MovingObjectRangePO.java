package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.datatype.ResultElement;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MovingObjectRangeAO;

public class MovingObjectRangePO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;
	private int geometryPosition;
	private int idPosition;
	private boolean idPositionSet = false;
	private double range;

	public MovingObjectRangePO(MovingObjectRangeAO ao) {
		this.dataStructure = MovingObjectDataStructureProvider.getInstance()
				.getDataStructure(ao.getDataStructureName());
		this.geometryPosition = ao.getInputSchema(0).findAttributeIndex(ao.getGeometryAttribute());
		if (ao.getIdAttribute() != null && !ao.getIdAttribute().isEmpty()) {
			this.idPosition = ao.getInputSchema(0).findAttributeIndex(ao.getIdAttribute());
			this.idPositionSet = true;
		}
		this.range = ao.getRange();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T tuple, int port) {

		// Get the geometry
		Object o = tuple.getAttribute(this.geometryPosition);
		Geometry geometry = null;

		if (o instanceof GeometryWrapper) {
			geometry = ((GeometryWrapper) o).getGeometry();
		}

		// Get the id of the moving object (if it is used)
		String movingObjectId = null;
		if (this.idPositionSet) {
			movingObjectId = tuple.getAttribute(this.idPosition);
		}

		// Calculate the results
		Map<String, List<ResultElement>> resultMap = this.dataStructure.queryCircle(geometry, this.range,
				new TimeInterval(tuple.getMetadata().getStart(), tuple.getMetadata().getEnd()), movingObjectId);

		// Send the result, but only if there are neighbors
		if (resultMap.size() > 0) {
			Gson gson = new Gson();
			String json = gson.toJson(resultMap);
			Tuple<?> newTuple = tuple.append(json);
			transfer((T) newTuple);
		}
	}

}
