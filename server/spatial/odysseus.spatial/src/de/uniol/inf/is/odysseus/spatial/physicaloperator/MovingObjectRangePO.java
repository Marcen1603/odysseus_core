package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

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

public class MovingObjectRangePO<T extends Tuple<?>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;
	private int geometryPosition;
	private double range;

	public MovingObjectRangePO(MovingObjectRangeAO ao) {
		this.dataStructure = MovingObjectDataStructureProvider.getInstance()
				.getDataStructure(ao.getDataStructureName());
		this.geometryPosition = ao.getInputSchema(0).findAttributeIndex(ao.getGeometryAttribute());
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
		Object o = tuple.getAttribute(this.geometryPosition);
		GeometryWrapper wrapper = null;
		Tuple<ITimeInterval> timeTuple = (Tuple<ITimeInterval>) tuple;

		Gson gson = new Gson();
		String json = "";
		if (o instanceof GeometryWrapper) {
			wrapper = (GeometryWrapper) o;
			Map<String, List<ResultElement>> resultMap = this.dataStructure.queryCircle(wrapper.getGeometry(),
					this.range, new TimeInterval(timeTuple.getMetadata().getStart(), timeTuple.getMetadata().getEnd()));
			json = gson.toJson(resultMap);
		}
		Tuple<?> newTuple = tuple.append(json);
		transfer((T) newTuple);
	}

}
