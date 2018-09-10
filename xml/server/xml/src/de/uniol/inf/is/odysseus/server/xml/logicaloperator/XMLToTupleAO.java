package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ToTupleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "XMLToTuple", doc = "Translates XML objects to tuples", category = {LogicalOperatorCategory.TRANSFORM })
public class XMLToTupleAO extends ToTupleAO {
	private static final long serialVersionUID = -5737495514565877599L;

	private Map<String, String> expressions = new HashMap<>();

	public XMLToTupleAO() {
		super();
	}

	public XMLToTupleAO(XMLToTupleAO ao) {
		super(ao);
		this.expressions = new HashMap<>(ao.expressions);
	}

	@Parameter(type = OptionParameter.class, name = "expressions", optional = true, isList = true, doc = "")
	public void setOptions(List<Option> value) {
		value.stream().forEach(o -> {
			expressions.put(o.getName(), o.getValue());
		});
	}

	
	@Override
	public XMLToTupleAO clone() {
		return new XMLToTupleAO(this);
	}

	public IMetaAttribute getLocalMetaAttribute() {
		return null;
	}

	public boolean readMetaData() {
		return false;
	}

	public Map<String, String> getExpressions() {
		return expressions;
	}
	

}
