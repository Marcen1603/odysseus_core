package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.state.RelationalStateMapPOState;
import de.uniol.inf.is.odysseus.relational.IProvidesMaxHistoryElements;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalStateExpression;

public class RelationalStateMapPO<T extends IMetaAttribute> extends RelationalMapPO<T> implements IStatefulPO {
	Logger LOG = LoggerFactory.getLogger(RelationalStateMapPO.class);

	final private GroupedHistoryStore<Tuple<T>> history;

	public RelationalStateMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean allowNullInOutput,
			IGroupProcessor<Tuple<T>, Tuple<T>> groupProcessor, boolean evaluateOnPunctuation,
			boolean expressionsUpdateable, boolean suppressErrors,
			boolean keepInput, int[] restrictList) {
		// MUST USE THIS WAY, else maxHistoryElements is always 0 :-)
		super(inputSchema, allowNullInOutput, evaluateOnPunctuation, expressionsUpdateable, suppressErrors, keepInput,
				restrictList);
		history = new GroupedHistoryStore<Tuple<T>>(groupProcessor);
		init(inputSchema, expressions);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void init(SDFSchema schema, SDFExpression[] expr) {
		this.expressions = new RelationalStateExpression[expr.length];
		for (int i = 0; i < expr.length; ++i) {
			this.expressions[i] = new RelationalStateExpression<T>(expr[i].clone());
			this.expressions[i].initVars(schema);
		}
		history.determineMaxHistoryElements((IProvidesMaxHistoryElements[]) expressions);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {

		if (!(ipo instanceof RelationalStateMapPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		RelationalStateMapPO<T> rmpo = (RelationalStateMapPO<T>) ipo;
		if (!this.history.equals(rmpo.history)) {
			return false;
		}

		return super.isSemanticallyEqual(ipo);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		history.init();
	}

	@Override
	public List<Tuple<T>> preProcess(Tuple<T> object) {
		return history.process(object);
	}

	@Override
	public IOperatorState getState() {
		RelationalStateMapPOState<T> state = new RelationalStateMapPOState<T>();
		state.setGroupsLastObjects(history.getGroupsLastObjects());
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setStateInternal(Serializable serializable) {
		try {
			RelationalStateMapPOState<T> state = (RelationalStateMapPOState<T>) serializable;
			history.setGroupsLastObjects(state.getGroupsLastObjects());
		} catch (Throwable T) {
			LOG.error(
					"The serializable state to set for the RelationalStateMapPO is not a valid RelationalStateMapPOState!");
		}
	}

}
