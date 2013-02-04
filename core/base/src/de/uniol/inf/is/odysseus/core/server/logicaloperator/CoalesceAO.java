package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name = "COALESCE", minInputPorts = 1, maxInputPorts = 1)
public class CoalesceAO extends AggregateAO {

	private static final long serialVersionUID = 6314887685476173038L;

	public CoalesceAO() {
	}
	
	public CoalesceAO(CoalesceAO coalesceAO) {
		super(coalesceAO);
	}

	@Parameter(name = "ATTR", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		for (SDFAttribute a : attributes) {
			addGroupingAttribute(a);
		}
	}
	
	@Override
	public boolean isValid() {
		if (getGroupingAttributes().size() > 0 && getPredicate() != null){
			addError(new IllegalParameterException(
					"can't use attributes and predicate at the same time"));
			return false;
		}
			
		return super.isValid();
	}
	
	@Override
	public CoalesceAO clone() {
		return new CoalesceAO(this);
	}
}
