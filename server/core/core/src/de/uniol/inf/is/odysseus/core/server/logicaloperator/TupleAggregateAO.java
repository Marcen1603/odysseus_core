package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "TupleAggregate", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.ADVANCED }, doc = "Select from all elements of a window on with the given method", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/TupleAggregate")
public class TupleAggregateAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 258769992760830658L;

	SDFAttribute attribute;
	String method;

	// Todo: Make registry
	static List<String> possMethods;
	{
		possMethods = new LinkedList<>();
		possMethods.add("MIN");
		possMethods.add("MAX");
		possMethods.add("FIRST");
		possMethods.add("LAST");
	}
	
	public TupleAggregateAO(TupleAggregateAO op) {
		super(op);
		this.attribute = op.attribute;
		this.method = op.method;
	}

	public TupleAggregateAO() {
	}

	@Parameter(name="attribute", type = ResolvedSDFAttributeParameter.class, optional = true, doc="Attribute on which the method is evaluated")
	public void setAttribute(SDFAttribute attribute) {
		this.attribute = attribute;
	}
	
	public SDFAttribute getAttribute() {
		return attribute;
	}
	
	@Parameter(name="method", type = StringParameter.class, optional = false, doc="Method to use (MIN, MAX, LAST, FIRST)", possibleValues="getMethodTypes")
	public void setMethod(String method) {
		this.method = method;
	}
	
	public List<String> getMethodTypes() {
		return possMethods;
	}
	
	public String getMethod() {
		return method;
	}
	
	@Override
	public TupleAggregateAO clone() {
		return new TupleAggregateAO(this);
	}

}
