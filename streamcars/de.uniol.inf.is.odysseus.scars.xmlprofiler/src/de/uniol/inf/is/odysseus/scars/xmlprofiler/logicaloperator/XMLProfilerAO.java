package de.uniol.inf.is.odysseus.scars.xmlprofiler.logicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class XMLProfilerAO<M extends IProbability & ITimeInterval & IConnectionContainer & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>> extends UnaryLogicalOp {

	String operatorName;

	private static final long serialVersionUID = 1L;

	public XMLProfilerAO() {
		super();
	}

	public XMLProfilerAO(XMLProfilerAO<M> clone) {
		super(clone);
		this.operatorName = clone.operatorName;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new XMLProfilerAO<M>(this);
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

}
