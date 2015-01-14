package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.state.RelationalStateMapPOState;

public class RelationalStateMapPO<T extends IMetaAttribute> extends
		RelationalMapPO<T> implements IStatefulPO {
	Logger LOG = LoggerFactory.getLogger(RelationalStateMapPO.class);
	
	private Map<Long, LinkedList<Tuple<T>>> groupsLastObjects = new HashMap<>();
	final private IGroupProcessor<Tuple<T>, Tuple<T>> groupProcessor;
	private int maxHistoryElements = 0;

	public RelationalStateMapPO(SDFSchema inputSchema,
			SDFExpression[] expressions, boolean allowNullInOutput,
			IGroupProcessor<Tuple<T>, Tuple<T>> groupProcessor,
			boolean evaluateOnPunctuation, boolean suppressErrors) {
		// MUST USE THIS WAY, else maxHistoryElements is always 0 :-)
		super(inputSchema, allowNullInOutput, evaluateOnPunctuation, suppressErrors);
		this.groupProcessor = groupProcessor;
		init(inputSchema, expressions);
	}

	@Override
	public LinkedList<Tuple<T>> preProcess(Tuple<T> object) {
		Long groupId = groupProcessor.getGroupID(object);
		LinkedList<Tuple<T>> lastObjects = groupsLastObjects.get(groupId);

		if (lastObjects == null) {
			lastObjects = new LinkedList<>();
			groupsLastObjects.put(groupId, lastObjects);
		}

		if (lastObjects.size() > maxHistoryElements) {
			lastObjects.removeLast();
		}
		lastObjects.addFirst(object);
		return lastObjects;
	}

	@Override
	public VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
		if (curAttribute.getNumber() > 0) {
			int pos = curAttribute.getNumber();
			if (pos > maxHistoryElements) {
				maxHistoryElements = pos + 1;
			}
			int index = schema.indexOf(curAttribute);
			return new VarHelper(index, pos);
		} else {
			return super.initAttribute(schema, curAttribute);
		}
	}

	@Override
	public Tuple<T> determineObjectForExpression(Tuple<T> object,
			LinkedList<Tuple<T>> lastObjects, int i, int j) {
		Tuple<T> obj = null;
		if (lastObjects.size() > this.variables[i][j].objectPosToUse) {
			obj = lastObjects.get(this.variables[i][j].objectPosToUse);
		}
		return obj;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {

		if (!(ipo instanceof RelationalStateMapPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		RelationalStateMapPO<T> rmpo = (RelationalStateMapPO<T>) ipo;

		if ((this.groupProcessor == null && rmpo.groupProcessor != null)
				|| !this.groupProcessor.equals(rmpo.groupProcessor)) {
			return false;
		}
		return super.isSemanticallyEqual(ipo);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		IGroupProcessor<Tuple<T>, Tuple<T>> g = this.groupProcessor;
		synchronized (g) {
			g.init();
		}
	}

	@Override
	public Serializable getState() {
		RelationalStateMapPOState<T> state = new RelationalStateMapPOState<T>();
		state.setGroupsLastObjects(groupsLastObjects);
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable serializable) {
		try {
			RelationalStateMapPOState<T> state = (RelationalStateMapPOState<T>) serializable;
			groupsLastObjects = state.getGroupsLastObjects();
		} catch (Throwable T) {
			LOG.error("The serializable state to set for the RelationalStateMapPO is not a valid RelationalStateMapPOState!");
		}
	}

}
