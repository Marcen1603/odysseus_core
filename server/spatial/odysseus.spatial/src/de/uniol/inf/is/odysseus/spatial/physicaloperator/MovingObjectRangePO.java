package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import java.util.Map;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectIndexOld;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.datatype.SpatioTemporalQueryResult;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MovingObjectRangeAO;

public class MovingObjectRangePO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private MovingObjectIndexOld dataStructure;
	private int idPosition;
	private double range;

	public MovingObjectRangePO(MovingObjectRangeAO ao) {
		this.dataStructure = MovingObjectDataStructureProvider.getInstance()
				.getDataStructure(ao.getDataStructureName());
		this.idPosition = ao.getInputSchema(0).findAttributeIndex(ao.getIdAttribute());
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

		// Get the id of the moving object (if it is used)
		String movingObjectId = null;
		Object idObject = tuple.getAttribute(this.idPosition);
		if (idObject instanceof Long) {
			movingObjectId = String.valueOf((Long) idObject);
		} else {
			movingObjectId = tuple.getAttribute(this.idPosition);
		}

		// Calculate the results
		Map<String, SpatioTemporalQueryResult> queryCircle = this.dataStructure.queryCircle(movingObjectId, this.range);

		// Send the result, but only if there are neighbors
		if (queryCircle.size() > 0) {
			Gson gson = new Gson();
			String json = gson.toJson(queryCircle);
			Tuple<?> newTuple = tuple.append(json);
			transfer((T) newTuple);
		}
	}
}
